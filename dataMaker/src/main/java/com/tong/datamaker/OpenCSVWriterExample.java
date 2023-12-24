package com.tong.datamaker;

import com.opencsv.CSVWriter;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class OpenCSVWriterExample {

    private static final Logger log = Logger.getLogger(com.tong.datamaker.OpenCSVWriterExample.class);

    /*public static void main(String[] args) {

        // CSV文件路径
        String filePath = "multithreaded_opencsv_data.csv";

        // 生成测试数据
        int rowCount = 100000;
        System.out.println("Generating test data...");
        StringBuilder testData = generateTestData(rowCount);
        System.out.println("Test data generation completed.");

        // 写入数据到CSV文件，并输出生成进度和耗时
        try {
            long startTime = System.currentTimeMillis();
            writeDataToCSV(filePath, testData.toString(), 10);
            long endTime = System.currentTimeMillis();

            System.out.println("Data has been written to CSV file successfully.");
            System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
        } catch (IOException | InterruptedException e) {
            log.info(e);
        }
    }*/

    private static StringBuilder generateTestData(int rowCount) {
        System.out.println("Generating test data...");
        StringBuilder testData = new StringBuilder();

        for (int i = 0; i < rowCount; i++) {
            // 生成一行测试数据，字段之间使用逗号分隔
            testData.append(HttpDataMaker.generateFakeDataRow());
            // 添加其他字段...

            testData.append("\n"); // 换行符

            // 打印生成进度信息
            if (i % 100 == 0) {
                double progress = ((double) i / rowCount) * 100;
                System.out.printf("Data generation progress: %.2f%%\n", progress);
            }
        }

        log.info("Test data generation completed.");
        return testData;
    }

    private static String generateRandomField() {
        // 此处可以根据需要生成不同类型的随机数据
        return "RandomData";
    }

    private static void writeDataToCSV(String filePath, String data, int numThreads)
            throws IOException, InterruptedException {
        try (FileWriter fileWriter = new FileWriter(filePath);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            // 将字符串数据拆分成行和列
            String[] lines = data.split("\n");

            // 使用CountDownLatch等待所有线程完成写入
            CountDownLatch latch = new CountDownLatch(numThreads);

            long totalLines = lines.length;
            int linesPerThread = lines.length / numThreads;

            log.info("Writing data to CSV file...");

            // 启动多个线程并发写入
            for (int i = 0; i < numThreads; i++) {
                int startIndex = i * linesPerThread;
                int endIndex = (i == numThreads - 1) ? lines.length : (i + 1) * linesPerThread;

                Thread writerThread = new Thread(() -> {
                    for (int j = startIndex; j < endIndex; j++) {
                        String[] fields = lines[j].split(",");
                        csvWriter.writeNext(fields);

                        // 打印写入进度信息
                        if (j % 100000 == 0) {
                            double progress = ((double) j / totalLines) * 100;
                            log.info("Data writing progress: "+ progress + " !!");
                        }
                    }

                    latch.countDown(); // 线程完成写入，计数减一
                });

                writerThread.start();
            }

            latch.await(); // 等待所有线程完成写入
            log.info("Data writing completed.");
        }
    }
}
