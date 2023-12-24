package com.tong.datamaker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadedDataGeneration {
    /*public static void main(String[] args) {
        int rowCount = 10000;
        int numThreads = 10;

        System.out.println("Generating test data using " + numThreads + " threads...");

        try {
            long startTime = System.currentTimeMillis();
            List<String> testData = generateTestData(rowCount, numThreads);
            long endTime = System.currentTimeMillis();

            System.out.println("Test data generation completed.");
            System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }*/

    private static List<String> generateTestData(int rowCount, int numThreads)
            throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Future<List<String>>> futures = new ArrayList<>();

        int linesPerThread = rowCount / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * linesPerThread;
            int endIndex = (i == numThreads - 1) ? rowCount : (i + 1) * linesPerThread;

            Callable<List<String>> dataGenerationTask = new DataGenerationTask(startIndex, endIndex);
            futures.add(executorService.submit(dataGenerationTask));
        }

        List<String> testData = new ArrayList<>();

        for (Future<List<String>> future : futures) {
            testData.addAll(future.get());
        }

        executorService.shutdown();
        return testData;
    }

    static class DataGenerationTask implements Callable<List<String>> {
        private final int startIndex;
        private final int endIndex;

        DataGenerationTask(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public List<String> call() {
            List<String> lines = new ArrayList<>();

            for (int i = startIndex; i < endIndex; i++) {
                // 生成一行测试数据，字段之间使用逗号分隔
                String line = generateRandomField() + "," + generateRandomField() + "," /* 添加其他字段... */;
                lines.add(line);
            }

            return lines;
        }

        private String generateRandomField() {
            // 此处可以根据需要生成不同类型的随机数据
            return "RandomData";
        }
    }
}
