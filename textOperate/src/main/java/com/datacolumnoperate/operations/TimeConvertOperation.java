package com.datacolumnoperate.operations;

import com.datacolumnoperate.exceptions.TimeConvertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * TimeConvertOperation 用于将时间字符串从一种格式转换为另一种格式。
 *
 * 支持以下时间格式之间的转换：
 * - 毫秒（MS），微秒（WS），纳秒（NS）
 * - 标准日期时间格式（如 `yyyy-MM-dd HH:mm:ss`，`yyyyMMddHHmmss` 等）
 */
public class TimeConvertOperation implements Operation {
    private static final Logger logger = LoggerFactory.getLogger(TimeConvertOperation.class);

    private static final String MS = "MS";
    private static final String WS = "WS";
    private static final String NS = "NS";

    /**
     * 执行时间格式转换操作。
     *
     * @param input  输入的时间字符串。例如：`"20231210153000"` 或毫秒时间戳 `"1702213800000"`。
     * @param params 操作参数。必须包含以下键值对：
     *               - `inputFormat`：输入时间的格式（如 `"yyyyMMddHHmmss"` 或 `"MS"`）。
     *               - `outputFormat`：输出时间的格式（如 `"yyyy-MM-dd HH:mm:ss"` 或 `"MS"`）。
     * @return 转换后的时间字符串。
     * @throws TimeConvertException 如果 `inputFormat` 或 `outputFormat` 参数缺失，或时间解析/格式化失败。
     */
    @Override
    public String execute(String input, Map<String, String> params) {
        String inputFormat = params.get("inputFormat");
        String outputFormat = params.get("outputFormat");

        if (inputFormat == null || outputFormat == null) {
            logger.error("Missing 'inputFormat' or 'outputFormat' parameter for TIME_CONVERT operation.");
            throw new TimeConvertException("Missing 'inputFormat' or 'outputFormat' parameter for TIME_CONVERT operation.");
        }

        try {
            // 解析输入时间
            Instant instant = parseInputTime(input, inputFormat);

            // 根据输出格式生成结果
            return formatOutputTime(instant, outputFormat);
        } catch (Exception e) {
            logger.error("Error during TIME_CONVERT operation.", e);
            throw new TimeConvertException("TIME_CONVERT operation failed.", e);
        }
    }

    /**
     * 将输入时间字符串根据 `inputFormat` 解析为 `Instant` 对象。
     */
    private Instant parseInputTime(String input, String inputFormat) {
        try {
            if (MS.equalsIgnoreCase(inputFormat)) {
                // 毫秒时间戳
                long ms = Long.parseLong(input);
                return Instant.ofEpochMilli(ms);
            } else if (WS.equalsIgnoreCase(inputFormat)) {
                // 微秒时间戳
                long ws = Long.parseLong(input);
                return Instant.ofEpochSecond(ws / 1000000, (ws % 1000000) * 1000);
            } else if (NS.equalsIgnoreCase(inputFormat)) {
                // 纳秒时间戳
                long ns = Long.parseLong(input);
                return Instant.ofEpochSecond(ns / 1000000000, ns % 1000000000);
            } else {
                // 使用指定的时间格式进行解析
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(inputFormat);
                LocalDateTime ldt = LocalDateTime.parse(input, formatter);
                return ldt.atZone(ZoneId.systemDefault()).toInstant();
            }
        } catch (Exception e) {
            logger.error("Failed to parse input time with format: {}", inputFormat, e);
            throw new TimeConvertException("Failed to parse input time with format: " + inputFormat, e);
        }
    }

    /**
     * 将 `Instant` 按照 `outputFormat` 转换为字符串。
     */
    private String formatOutputTime(Instant instant, String outputFormat) {
        try {
            if (MS.equalsIgnoreCase(outputFormat)) {
                // 输出为毫秒时间戳
                return String.valueOf(instant.toEpochMilli());
            } else if (WS.equalsIgnoreCase(outputFormat)) {
                // 输出为微秒时间戳
                long microseconds = instant.getEpochSecond() * 1000000 + instant.getNano() / 1000;
                return String.valueOf(microseconds);
            } else if (NS.equalsIgnoreCase(outputFormat)) {
                // 输出为纳秒时间戳
                long nanoseconds = instant.getEpochSecond() * 1000000000 + instant.getNano();
                return String.valueOf(nanoseconds);
            } else {
                // 使用指定的时间格式进行输出
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);
                LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                return ldt.format(formatter);
            }
        } catch (Exception e) {
            logger.error("Failed to format output time with format: {}", outputFormat, e);
            throw new TimeConvertException("Failed to format output time with format: " + outputFormat, e);
        }
    }
}
