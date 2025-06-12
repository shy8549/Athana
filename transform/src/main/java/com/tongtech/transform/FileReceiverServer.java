package com.tongtech.transform;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileReceiverServer {
    private static final int PORT = ConfigUtils.getInt("port", 18080);
    private static final String AUTH_TOKEN = ConfigUtils.get("auth.token");
    private static final String AES_KEY = ConfigUtils.get("aes.key");
    private static final boolean ENABLE_ENCRYPT = ConfigUtils.getBoolean("enable.encrypt", true);

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/upload", new UploadHandler());
        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(16));
        server.start();
        System.out.println("[Server] FileReceiverServer started on port " + PORT);
    }

    // HTTP上传分块处理
    static class UploadHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
            Headers headers = exchange.getRequestHeaders();
            String token = headers.getFirst("X-Token");
            if (!AUTH_TOKEN.equals(token)) {
                exchange.sendResponseHeaders(401, 0);
                exchange.getResponseBody().close();
                System.err.printf("[Server] %s token校验失败！\n", clientIp);
                return;
            }

            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            String fileName = params.get("fileName");
            String targetDir = params.get("targetDir");
            int chunkIdx = Integer.parseInt(params.get("chunkIdx"));
            int totalChunks = Integer.parseInt(params.get("totalChunks"));
            boolean isEncrypt = Boolean.parseBoolean(params.getOrDefault("encrypt", String.valueOf(ENABLE_ENCRYPT)));
            String clientHash = params.get("hash");

            InputStream is = exchange.getRequestBody();
            byte[] incoming = readAll(is);
            byte[] chunkData = isEncrypt
                    ? CryptoUtils.decryptAES(incoming, AES_KEY.getBytes(StandardCharsets.UTF_8))
                    : incoming;

            String actualHash = ChunkInfo.calcSHA256(chunkData);
            if (!actualHash.equalsIgnoreCase(clientHash)) {
                exchange.sendResponseHeaders(400, 0);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write("Hash check failed".getBytes());
                }
                System.err.printf("[Server] %s 文件 %s 第%d块 HASH校验失败\n", clientIp, fileName, chunkIdx);
                return;
            }

            File dir = new File(targetDir, fileName + ".parts");
            if (!dir.exists() && !dir.mkdirs()) {
                System.err.printf("[Server] 目录创建失败: %s\n", dir.getAbsolutePath());
                exchange.sendResponseHeaders(500, 0);
                exchange.getResponseBody().close();
                return;
            }

            File chunkFile = new File(dir, "chunk_" + chunkIdx);
            try (FileOutputStream fos = new FileOutputStream(chunkFile)) {
                fos.write(chunkData);
            }
            System.out.printf("[Server] %s 文件 %s 第%d块接收完成\n", clientIp, fileName, chunkIdx);

            // 合并全部分块
            if (Objects.requireNonNull(dir.listFiles()).length == totalChunks) {
                FileUtils.mergeChunks(dir, new File(targetDir, fileName), totalChunks);
                System.out.printf("[Server] %s 文件 %s 合并成功，写入 %s\n", clientIp, fileName, new File(targetDir, fileName));
            }
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        }

        private byte[] readAll(InputStream in) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
            return out.toByteArray();
        }
        private Map<String, String> parseQuery(String query) {
            Map<String, String> map = new HashMap<>();
            if (query == null) return map;
            for (String s : query.split("&")) {
                String[] kv = s.split("=");
                if (kv.length == 2) map.put(kv[0], kv[1]);
            }
            return map;
        }
    }
}
