package com.tongtech.transform;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.TypeReference;

public class ProgressStore {
    private final File storeFile;
    private final ConcurrentHashMap<String, ProgressInfo> progressMap = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ObjectMapper mapper = new ObjectMapper();

    // 记录每个文件在每台主机的进度
    public static class ProgressInfo {
        public Set<Integer> doneChunks = new HashSet<>();
        public long lastUpdate = System.currentTimeMillis();
        public boolean finished = false;
    }

    public ProgressStore(String fileName) throws IOException {
        this.storeFile = new File(fileName);
        if (storeFile.exists()) {
            try (FileInputStream fis = new FileInputStream(storeFile)) {
                Map<String, ProgressInfo> temp = mapper.readValue(fis, new TypeReference<Map<String, ProgressInfo>>(){});
                progressMap.putAll(temp);
            }
        }
    }

    private String key(String host, String file) {
        return host + "|" + file;
    }

    public void markChunkDone(String host, String file, int chunkIdx) throws IOException {
        lock.writeLock().lock();
        try {
            ProgressInfo info = progressMap.computeIfAbsent(key(host, file), k -> new ProgressInfo());
            info.doneChunks.add(chunkIdx);
            info.lastUpdate = System.currentTimeMillis();
            save();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Set<Integer> getDoneChunks(String host, String file) {
        lock.readLock().lock();
        try {
            ProgressInfo info = progressMap.get(key(host, file));
            if (info == null) return new HashSet<>();
            return new HashSet<>(info.doneChunks);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void markTaskFinished(String host, String file) throws IOException {
        lock.writeLock().lock();
        try {
            ProgressInfo info = progressMap.get(key(host, file));
            if (info != null) {
                info.finished = true;
                info.lastUpdate = System.currentTimeMillis();
                save();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    // 清理保留时间外的历史记录
    public void cleanOldTasks(long keepMillis) throws IOException {
        lock.writeLock().lock();
        try {
            long now = System.currentTimeMillis();
            Iterator<Map.Entry<String, ProgressInfo>> it = progressMap.entrySet().iterator();
            while (it.hasNext()) {
                ProgressInfo info = it.next().getValue();
                if (info.finished && (now - info.lastUpdate > keepMillis)) {
                    it.remove();
                }
            }
            save();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void save() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(storeFile)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(fos, progressMap);
        }
    }
}
