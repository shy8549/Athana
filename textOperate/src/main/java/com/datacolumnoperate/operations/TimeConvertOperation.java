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
 * 功能描述：
 * - 支持将时间字符串从特定的格式（如 `"yyyyMMddHHmmss"`) 转换为另一种格式（如 `"yyyy-MM-dd HH:mm:ss"`）。
 * - 支持毫秒时间戳（`"MS"`) 与时间格式化字符串之间的相互转换。
 *
 * 应用场景：
 * - 日志处理：将日志时间从一种格式转换为另一种格式。
 * - 数据清洗：统一不同数据源中时间格式的标准。
 *
 * 配置参数：
 * - `inputFormat`（必需）：输入时间的格式。支持：
 *   - `"MS"`：输入为毫秒时间戳。
 *   - 自定义格式化字符串（如 `"yyyyMMddHHmmss"`）。
 * - `outputFormat`（必需）：输出时间的格式。支持：
 *   - `"MS"`：输出为毫秒时间戳。
 *   - 自定义格式化字符串（如 `"yyyy-MM-dd HH:mm:ss"`）。
 *
 * 示例：
 * 输入时间字符串：`"20231210153000"`
 * 配置：
 * - `inputFormat`: `"yyyyMMddHHmmss"`
 * - `outputFormat`: `"yyyy-MM-dd HH:mm:ss"`
 * 输出结果：`"2023-12-10 15:30:00"`
 *
 * 错误处理：
 * - 如果 `inputFormat` 或 `outputFormat` 缺失，抛出 `TimeConvertException`。
 * - 如果输入格式解析失败，或输出格式转换失败，记录错误日志并抛出 `TimeConvertException`。
 */
public class TimeConvertOperation implements Operation {
    private static final Logger logger = LoggerFactory.getLogger(TimeConvertOperation.class);

    /**
     * 执行时间格式转换操作。
     *
     * @param input  输入的时间字符串。例如：`"20231210153000"` 或毫秒时间戳 `"1702213800000"`。
     * @param params 操作参数。必须包含以下键值对：
     *               - `inputFormat`：输入时间的格式（如 `"yyyyMMddHHmmss"` 或 `"MS"`）。
     *               - `outputFormat`：输出时间的格式（如 `"yyyy-MM-dd HH:mm:ss"` 或 `"MS"`）。
     * @return 转换后的时间字符串。例如：输入 `"20231210153000"`，
     *         配置 `inputFormat: yyyyMMddHHmmss` 和 `outputFormat: yyyy-MM-dd HH:mm:ss`，
     *         返回结果为 `"2023-12-10 15:30:00"`。
     * @throws TimeConvertException 如果 `inputFormat` 或 `outputFormat` 参数缺失，
     *                              或时间解析/格式化失败。
     */
    @Override
    public String execute(String input, Map<String, String> params) {
        // 从参数中获取输入格式和输出格式
        String inputFormat = params.get("inputFormat");
        String outputFormat = params.get("outputFormat");

        // 验证参数是否完整
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
            // 捕获所有异常，记录错误日志并抛出自定义异常
            logger.error("Error during TIME_CONVERT operation.", e);
            throw new TimeConvertException("TIME_CONVERT operation failed.", e);
        }
    }

    /**
     * 将输入时间字符串根据 `inputFormat` 解析为 `Instant` 对象。
     *
     * @param input       输入的时间字符串。例如：`"20231210153000"` 或毫秒时间戳 `"1702213800000"`。
     * @param inputFormat 输入时间的格式。支持：
     *                    - `"MS"`：表示毫秒时间戳。
     *                    - 时间格式化字符串（如 `"yyyyMMddHHmmss"`）。
     * @return `Instant` 对象，表示解析后的时间点。
     * @throws TimeConvertException 如果输入格式解析失败或参数无效。
     */
    private Instant parseInputTime(String input, String inputFormat) {
        try {
            if ("MS".equalsIgnoreCase(inputFormat)) {
                // 输入为毫秒时间戳
                long ms = Long.parseLong(input);
                return Instant.ofEpochMilli(ms);
            } else {
                // 输入为指定的时间格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(inputFormat);
                // 使用系统默认时区将 LocalDateTime 转换为 Instant
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
     *
     * @param instant      表示时间点的 `Instant` 对象。
     * @param outputFormat 输出时间的格式。支持：
     *                     - `"MS"`：输出为毫秒时间戳。
     *                     - 时间格式化字符串（如 `"yyyy-MM-dd HH:mm:ss"`）。
     * @return 格式化后的时间字符串。
     * @throws TimeConvertException 如果输出格式转换失败或参数无效。
     */
    private String formatOutputTime(Instant instant, String outputFormat) {
        try {
            if ("MS".equalsIgnoreCase(outputFormat)) {
                // 输出为毫秒时间戳
                return String.valueOf(instant.toEpochMilli());
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
