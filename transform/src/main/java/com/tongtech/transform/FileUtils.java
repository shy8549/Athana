package com.tongtech.transform;

import java.io.*;
import java.util.*;

public class FileUtils {
    // 合并所有分块为目标文件
    public static void mergeChunks(File partDir, File target, int totalChunks) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(target)) {
            for (int i = 0; i < totalChunks; i++) {
                File chunk = new File(partDir, "chunk_" + i);
                try (FileInputStream fis = new FileInputStream(chunk)) {
                    byte[] buf = new byte[8192];
                    int len;
                    while ((len = fis.read(buf)) != -1) fos.write(buf, 0, len);
                }
            }
        }
        // 删除分块文件和目录
        for (File f : Objects.requireNonNull(partDir.listFiles())) {
            if (!f.delete()) {
                System.err.printf("[Server] 分块文件删除失败: %s\n", f.getAbsolutePath());
            }
        }
        if (!partDir.delete()) {
            System.err.printf("[Server] 分块目录删除失败: %s\n", partDir.getAbsolutePath());
        }

    }

    // 递归列举所有文件（不包含目录）
    public static List<File> listAllFiles(File dir) {
        List<File> files = new ArrayList<>();
        if (dir.isFile()) {
            files.add(dir);
            return files;
        }
        File[] list = dir.listFiles();
        if (list != null) {
            for (File f : list) {
                if (f.isDirectory()) files.addAll(listAllFiles(f));
                else files.add(f);
            }
        }
        return files;
    }
}

