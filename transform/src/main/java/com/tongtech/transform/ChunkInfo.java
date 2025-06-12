package com.tongtech.transform;

import java.io.Serializable;
import java.security.MessageDigest;

public class ChunkInfo implements Serializable {
    public String fileName;
    public int chunkIdx;
    public int totalChunks;
    public long chunkSize;
    public long fileSize;
    public long offset;
    public String hash;

    public ChunkInfo() {}

    public ChunkInfo(String fileName, int chunkIdx, int totalChunks, long chunkSize, long fileSize, long offset, String hash) {
        this.fileName = fileName;
        this.chunkIdx = chunkIdx;
        this.totalChunks = totalChunks;
        this.chunkSize = chunkSize;
        this.fileSize = fileSize;
        this.offset = offset;
        this.hash = hash;
    }

    // 计算块的SHA-256哈希
    public static String calcSHA256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
