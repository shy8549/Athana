package com.tong.datamaker;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;


public class GeneratorAndWriter_Schedule {
    private static final int TOTAL_ROWS = 1_000_000;
    private static final String FILE_NAME = "test.csv";
    private static final int NUMBER_OF_THREADS = 10; // 控制线程数量
    private static final int QUEUE_CAPACITY = 1000; // 控制队列大小
    private static final int BATCH_WRITE_SIZE = 1000; // 批量写入大小
    private static final BlockingQueue<String[]> dataQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private static final AtomicInteger writtenCount = new AtomicInteger(0);

    private static final Logger log = Logger.getLogger(com.tong.datamaker.GeneratorAndWriter_Schedule.class);

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        ExecutorService generateExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        ExecutorService writeExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            generateExecutor.submit(new DataGenerator(TOTAL_ROWS / NUMBER_OF_THREADS, i));
            log.info("submit DataGenerator THREAD_" + i);
        }

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            writeExecutor.submit(new DataWriter(i));
            log.info("submit DataWriter THREAD_" + i);
        }

        generateExecutor.shutdown();
        writeExecutor.shutdown();

        /*try {
            generateExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            writeExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error(e);
        }*/

        long endTime = System.currentTimeMillis();
        log.info(" Data generation and write to csv completed. Time taken: " + (endTime - startTime) + " ms");
    }

    static class DataGenerator implements Runnable {
        private final int rowsToGenerate;
        private final int threadId; // 线程标识符

        DataGenerator(int rowsToGenerate, int threadId) {
            this.rowsToGenerate = rowsToGenerate;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            log.info("DataGenerator THREAD_" + threadId + " started.");
            for (int i = 0; i < rowsToGenerate; i++) {
                try {
                    String [] rowData = {HttpDataMaker.generateFakeDataRow()};
                    dataQueue.put(rowData);
                    // 打印生成进度信息
                    if (i % 1000 == 0) {
                        double progress = ((double) i / rowsToGenerate) * 100;
                        log.info("THREAD_"+threadId+ " Data generation progress: "+ i + " rows!");
                        log.info("THREAD_"+threadId+ " Data generation progress: "+ progress +"%");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            long endTime = System.currentTimeMillis();
            log.info("THREAD_" + threadId + " Data generation completed. Time taken: " + (endTime - startTime) + " ms");
        }
    }

    static class DataWriter implements Runnable {
        private final int threadId; // 线程标识符

        DataWriter(int threadId) {
            this.threadId = threadId;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            log.info("DataWriter THREAD_" + threadId + " started.");
            try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_NAME, writtenCount.get() == 0),
                    '|', // 分隔符
                    CSVWriter.NO_QUOTE_CHARACTER, // 包围符
                    '\\', // CSVWriter.DEFAULT_ESCAPE_CHARACTER 默认是双引号 转义字符
                    "")) {
                List<String[]> batch = new ArrayList<>();
                while (writtenCount.get() < TOTAL_ROWS) {
                    dataQueue.drainTo(batch, BATCH_WRITE_SIZE);
                    if (!batch.isEmpty()) {
                        writer.writeAll(batch);
                        writtenCount.addAndGet(batch.size());
                        int currentWrittenCount = writtenCount.addAndGet(batch.size());
                        batch.clear();

                        // 打印写入进度

                        if (writtenCount.get() % 10000 == 0 || currentWrittenCount == TOTAL_ROWS) {
                            double progress = 100.0 * currentWrittenCount / TOTAL_ROWS;
                            log.info("THREAD_"+ threadId +": Written " + currentWrittenCount + " rows to CSV (" + progress +"% completed) ");
                        }
                    }
                }
            } catch (IOException e) {
                log.error(e);
            }
            long endTime = System.currentTimeMillis();
//            log.info("Time taken for writing to CSV: " + (endTime - startTime) + " ms");
            log.info("THREAD_" + threadId + " Time taken for writing to CSV Time taken: " + (endTime - startTime) + " ms");
        }
    }
}
