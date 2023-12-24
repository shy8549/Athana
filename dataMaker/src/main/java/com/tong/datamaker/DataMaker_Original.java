package com.tong.datamaker;

import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;


public class DataMaker_Original {

    private static final Logger log = Logger.getLogger(DataMaker_Original.class);

    private static synchronized void writeToCSV(String filename,int num,String xdr) {

        try (FileWriter writer = new FileWriter(filename, true)) {
            // Append the line to the CSV file
//            writer.append(line).append("\n");
            if (xdr.equals("http")){
                for (int i = 0; i < num; i++) {
                    writer.write(HttpDataMaker.generateFakeDataRow());
                }
            }else if (xdr.equals("gen")){
                for (int i = 0; i < num; i++) {
                    writer.write(GenDataMaker.generateFakeDataRow());
                }
            }else {
                System.out.println("参数错误，请填 http 或  gen"); ;
            }

            System.out.println("Test data generated and written to " + filename);
        } catch (IOException e) {
            log.info(e);
        }
    }

    private static void writeDataToCSV(String filePath, String[] data) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "rw");
             FileChannel channel = file.getChannel()) {

            // 计算需要的内存映射大小
            int dataSize = calculateDataSize(data);
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, dataSize);

            // 将数据写入内存映射文件
            for (String line : data) {
                buffer.put(line.getBytes(StandardCharsets.UTF_8));
                buffer.put((byte) '\n'); // 换行符
            }
        }
    }

    private static int calculateDataSize(String[] data) {
        int dataSize = 0;
        for (String line : data) {
            dataSize += line.getBytes(StandardCharsets.UTF_8).length + 1; // 加上换行符的长度
        }
        return dataSize;
    }


    public static void main(String[] args) {

        if (args.length!=4) {
            log.info("Wrong parameters！！！  Please enter correct parameters such as: filename rowNumber xdrName(http,gen) concurrent ");
        }else {
            String filename = args[0];
            int num = Integer.parseInt(args[1]);
            String xdr = args[2];
            int thread = Integer.parseInt(args[3]);
            // Create an executor service with a fixed number of threads
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            // Submit tasks to the executor service； 10个task，每个numRows行数据
            for (int i = 0; i < thread; i++) {
                executorService.submit(() -> writeToCSV(filename,num,xdr));
//            executorService.submit(HttpDataMaker::writeToCSV);
                System.out.println("Thread " + Thread.currentThread().getId() + ",Data " + i);
            }

            // Shutdown the executor service
            executorService.shutdown();
        }
    }



    /*public static void main(String[] args) {

        String randomString = getRandomString("a","b","c","d");
        System.out.println(randomString);
        Faker faker = new Faker(new Locale("zh_CN"));
        System.out.println(faker.university());
        System.out.println(faker.number().numberBetween(2,8));
        System.out.println("https://" + faker.internet().domainName() + "/example-path");
        System.out.println(faker.phoneNumber().phoneNumber());
        System.out.println(faker.phoneNumber().cellPhone());
        System.out.println(faker.bothify("25000a6400"+"#########?????????"+"633a"));
        System.out.println(faker.regexify("[A-F0-9]{32}"));
        System.out.println("86"+faker.regexify("[0-9]{6}")+"#"+faker.regexify("[A-F0-9]{64}"));
        System.out.println(faker.internet().ipV4Address());
        System.out.println(faker.internet().ipV6Address());
        System.out.println(faker.regexify("[0-9]{10}"));
        System.out.println(generateTimestampMillisecond("20231219103455","20231219173455"));
        System.out.println(faker.number().numberBetween(200,800));
        System.out.println(String.format("%05d", faker.number().numberBetween(1,99)));
        System.out.println("www." + faker.internet().domainName() + ".com");
        System.out.println("https://" + faker.internet().domainName() + ".com/" + faker.regexify("[a-f0-9]{8}"));
        System.out.println(generateTimestampMillisecond("20231219103455234","20231219173455611"));
    }*/

}
