package com.datacolumnoperate.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * TimeConvertOperation 用于将时间字符串从一种格式转换为另一种格式。
 */
public class TimeConvertOperation implements Operation {
    private static final Logger logger = LoggerFactory.getLogger(TimeConvertOperation.class);

    @Override
    public String execute(String input, Map<String, String> params) {
        String inputFormat = params.get("inputFormat");
        String outputFormat = params.get("outputFormat");

        if (inputFormat == null || outputFormat == null) {
            logger.error("Missing 'inputFormat' or 'outputFormat' parameter for TIME_CONVERT operation.");
            throw new IllegalArgumentException("Missing 'inputFormat' or 'outputFormat' parameter for TIME_CONVERT operation.");
        }

        try {
            // 解析输入时间
            Instant instant = parseInputTime(input, inputFormat);

            // 根据输出格式生成结果
            return formatOutputTime(instant, outputFormat);
        } catch (Exception e) {
            logger.error("Error during TIME_CONVERT operation.", e);
            throw new RuntimeException("TIME_CONVERT operation failed.", e);
        }
    }

    /**
     * 将输入时间字符串根据inputFormat解析为Instant对象
     *
     * @param input       输入的时间字符串
     * @param inputFormat 输入格式，可以是"MS"或时间格式化字符串(如"yyyyMMddHHmmss")
     * @return Instant 对象
     */
    private Instant parseInputTime(String input, String inputFormat) {
        if ("MS".equalsIgnoreCase(inputFormat)) {
            // 输入为毫秒时间戳
            long ms = Long.parseLong(input);
            return Instant.ofEpochMilli(ms);
        } else {
            // 输入为指定的时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(inputFormat);
            // 使用系统默认时区转换为Instant
            LocalDateTime ldt = LocalDateTime.parse(input, formatter);
            return ldt.atZone(ZoneId.systemDefault()).toInstant();
        }
    }

    /**
     * 将Instant按照输出格式转成字符串
     *
     * @param instant      时间点
     * @param outputFormat 输出格式，可为"MS"或时间格式字符串(如"yyyy-MM-dd HH:mm:ss")
     * @return 格式化后的时间字符串
     */
    private String formatOutputTime(Instant instant, String outputFormat) {
        if ("MS".equalsIgnoreCase(outputFormat)) {
            // 输出为毫秒时间戳
            return String.valueOf(instant.toEpochMilli());
        } else {
            // 使用指定的时间格式进行输出
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return ldt.format(formatter);
        }
    }
}
