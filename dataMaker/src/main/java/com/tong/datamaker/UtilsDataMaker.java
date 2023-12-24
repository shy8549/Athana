package com.tong.datamaker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;


public class UtilsDataMaker {

    private static final Logger log = Logger.getLogger(UtilsDataMaker.class);
    public static <T> T getRandomItem(List<T> itemList) {
        if (itemList == null || itemList.isEmpty()) {
            throw new IllegalArgumentException("Collection must not be empty or null");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(itemList.size());
        return itemList.get(randomIndex);
    }

    public static String getRandomString(String... stringOptions) {
        if (stringOptions == null || stringOptions.length == 0) {
            throw new IllegalArgumentException("At least one option must be provided");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(stringOptions.length);
        return stringOptions[randomIndex];
    }

    // 参数为13位格式的时间yyyyMMddHHmmssSSS,返回微秒
    public static long generateTimestampMicroseconds(String startTime, String endTime){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            // Parse start and end times
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);

            // Calculate the time difference in microseconds
            long microsecondsDifference = TimeUnit.MILLISECONDS.toMicros(
                    endDate.getTime() - startDate.getTime()
            );

            // Generate a random timestamp within the time range
            long randomMicroseconds = ThreadLocalRandom.current().nextLong(microsecondsDifference + 1);
            return startDate.getTime() * 1000 + randomMicroseconds;

        } catch (ParseException e) {
            log.info(e.getMessage());
            return 0; // Handle parsing errors
        }
    }

    // 参数为13位格式的时间yyyyMMddHHmmssSSS,返回毫秒
    public static long generateTimestampMillisecond(String startTime, String endTime){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            // Parse start and end times
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);

            // Calculate the time difference in microseconds
            long microsecondsDifference = TimeUnit.MILLISECONDS.toMicros(
                    endDate.getTime() - startDate.getTime()
            );

            // Generate a random timestamp within the time range
            return startDate.getTime() ;

        } catch (ParseException e) {
            log.info(e.getMessage());
            return 0; // Handle parsing errors
        }
    }
}
