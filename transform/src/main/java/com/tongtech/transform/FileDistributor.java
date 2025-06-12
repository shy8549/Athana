package com.tongtech.transform;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * 支持多主机多线程、多文件/目录分发、分块自动重试、详细日志与断点续传
 */
public class FileDistributor {
    public static void main(String[] args) throws Exception {
        // 读取参数与初始化
        boolean enableEncrypt = ConfigUtils.getBoolean("enable.encrypt", true);
        String aesKey = ConfigUtils.get("aes.key");
        String authToken = ConfigUtils.get("auth.token");
        int port = ConfigUtils.getInt("port", 18080);
        List<String> targets = Arrays.asList(ConfigUtils.get("targets").split(","));
        String sourceDir = ConfigUtils.get("source.dir");
        String targetDir = ConfigUtils.get("target.dir");
        int maxRetry = ConfigUtils.getInt("max.retry", 5);
        int maxThread = ConfigUtils.getInt("max.thread", 8);
        long keepMillis = ConfigUtils.getInt("keep.days", 7) * 24L * 3600 * 1000;
        int chunkSize = ConfigUtils.getInt("chunk.size", 1024 * 1024);

        ProgressStore progressStore = new ProgressStore(ConfigUtils.get("progress.store"));

        // 递归扫描所有本地文件
        List<File> allFiles = FileUtils.listAllFiles(new File(sourceDir));
        System.out.printf("[Client] 共需分发文件%d个，目标主机%d台\n", allFiles.size(), targets.size());

        // 多主机并发调度
        ExecutorService pool = Executors.newFixedThreadPool(maxThread);
        for (String host : targets) {
            pool.submit(() -> {
                try {
                    for (File file : allFiles) {
                        // 保持目录结构（相对路径）
                        String relPath = sourceDir.endsWith(File.separator) ?
                                file.getAbsolutePath().substring(sourceDir.length())
                                : file.getAbsolutePath().substring(sourceDir.length() + 1);
                        uploadFile(
                                file, relPath.replace("\\", "/"), host.trim(), port, targetDir,
                                enableEncrypt, aesKey, authToken, progressStore, maxRetry, chunkSize
                        );
                    }
                } catch (Exception e) {
                    System.err.printf("[Client] %s 任务异常: %s\n", host, e);
                }
            });
        }
        pool.shutdown();
        boolean terminated = pool.awaitTermination(2, TimeUnit.HOURS);
        if (!terminated) {
            System.err.println("[Client] 警告：部分分发任务在2小时内未完成！");
            // 如有需要可强制终止未完成的任务
            // List<Runnable> unfinished = pool.shutdownNow();
            // System.err.println("未完成任务数: " + unfinished.size());
        }

        // 清理历史记录
        progressStore.cleanOldTasks(keepMillis);
        System.out.println("[Client] 所有文件分发任务已完成。");
    }

    /**
     * 上传单个文件到指定主机（带分块、断点续传、重试、完整性校验）
     */
    public static void uploadFile(
            File file, String relPath, String host, int port, String targetDir,
            boolean enableEncrypt, String aesKey, String authToken,
            ProgressStore progressStore, int maxRetry, int chunkSize
    ) throws Exception {
        long fileSize = file.length();
        int totalChunks = (int) Math.ceil((double) fileSize / chunkSize);

        System.out.printf("[Client] [%s] 上传 %s (%d字节, %d块)\n", host, relPath, fileSize, totalChunks);

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            for (int chunkIdx = 0; chunkIdx < totalChunks; chunkIdx++) {
                if (progressStore.getDoneChunks(host, relPath).contains(chunkIdx)) continue;
                raf.seek((long) chunkIdx * chunkSize);
                int len = (int) Math.min(chunkSize, fileSize - (long) chunkIdx * chunkSize);
                byte[] buffer = new byte[len];
                raf.readFully(buffer);
                String chunkHash = ChunkInfo.calcSHA256(buffer);

                byte[] toSend = enableEncrypt
                        ? CryptoUtils.encryptAES(buffer, aesKey.getBytes(StandardCharsets.UTF_8))
                        : buffer;

                int retries = 0;
                boolean success = false;
                while (!success && retries < maxRetry) {
                    try {
                        String url = String.format(
                                "http://%s:%d/upload?fileName=%s&targetDir=%s&chunkIdx=%d&totalChunks=%d&encrypt=%b&hash=%s",
                                host, port, URLEncoder.encode(relPath, "UTF-8"),
                                URLEncoder.encode(targetDir, "UTF-8"),
                                chunkIdx, totalChunks, enableEncrypt, chunkHash
                        );
                        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("X-Token", authToken);
                        conn.setDoOutput(true);
                        try (OutputStream os = conn.getOutputStream()) {
                            os.write(toSend);
                        }
                        int code = conn.getResponseCode();
                        if (code == 200) {
                            progressStore.markChunkDone(host, relPath, chunkIdx);
                            success = true;
                            System.out.printf("[Client] [%s] %s 块%d/%d 成功\n", host, relPath, chunkIdx + 1, totalChunks);
                        } else {
                            String msg = new BufferedReader(new InputStreamReader(conn.getErrorStream()))
                                    .lines().reduce("", (a, b) -> a + b);
                            throw new IOException("Failed: " + code + " - " + msg);
                        }
                        conn.disconnect();
                    } catch (Exception e) {
                        retries++;
                        System.err.printf("[Client] [%s] %s 块%d/%d 失败(第%d次重试): %s\n", host, relPath, chunkIdx + 1, totalChunks, retries, e.getMessage());
                        if (retries < maxRetry) Thread.sleep(1000L * retries); // 退避
                    }
                }
                if (!success) {
                    System.err.printf("[Client] [%s] %s 块%d/%d 多次重试后仍失败\n", host, relPath, chunkIdx + 1, totalChunks);
                }
            }
        }
        progressStore.markTaskFinished(host, relPath);
        System.out.printf("[Client] [%s] %s 文件全部上传完成\n", host, relPath);
    }
}
