package com.datacolumnoperate.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * SubstringOperation 用于提取字符串的子部分。
 *
 * 该类实现了 `Operation` 接口，根据配置参数中指定的起始索引（`start`）和结束索引（`end`），
 * 从输入字符串中提取指定范围的子字符串。
 *
 * 应用场景：
 * - 提取固定长度的字段值，例如从日期字符串中提取年、月、日。
 * - 从固定位置截取数据，例如处理用户标识符、代码片段等。
 *
 * 配置参数：
 * - `start`：必需参数，指定子字符串的起始索引（包含该位置，索引从 0 开始）。
 * - `end`：必需参数，指定子字符串的结束索引（不包含该位置）。
 *   注意：索引值必须满足 `0 <= start <= end <= input.length()`。
 *
 * 示例：
 * 假设列值为 `"2023-12-10"`, 配置如下：
 * - `start`: `0`
 * - `end`: `4`
 * 处理结果为：`"2023"`
 *
 * 错误处理：
 * - 如果 `start` 或 `end` 参数缺失，抛出 `IllegalArgumentException`。
 * - 如果 `start` 或 `end` 的格式不是有效的整数，抛出 `IllegalArgumentException`。
 * - 如果索引范围无效（如 `start > end` 或 `end > input.length()`），记录警告日志并返回原始字符串。
 * - 如果出现其他异常，记录错误日志并抛出 `RuntimeException`。
 */
public class SubstringOperation implements Operation {
    // 定义日志记录器，用于记录操作的日志信息
    private static final Logger logger = LoggerFactory.getLogger(SubstringOperation.class);

    /**
     * 执行子字符串提取操作。
     *
     * @param input  原始输入字符串。通常是列中的值，例如 `"2023-12-10"`。
     * @param params 操作参数。必须包含以下键值对：
     *               - `start`：子字符串的起始索引（包含该索引，索引从 0 开始）。
     *               - `end`：子字符串的结束索引（不包含该索引）。
     * @return 提取的子字符串。例如，输入 `"2023-12-10"`，
     *         配置 `start: 0` 和 `end: 4`，返回结果为 `"2023"`。
     *         如果索引范围无效，则返回原始字符串。
     * @throws IllegalArgumentException 如果 `start` 或 `end` 参数缺失或格式无效。
     * @throws RuntimeException 如果操作过程中发生其他错误。
     */
    @Override
    public String execute(String input, Map<String, String> params) {
        // 从参数中获取起始和结束索引
        String startStr = params.get("start");
        String endStr = params.get("end");

        // 验证参数是否完整
        if (startStr == null || endStr == null) {
            logger.error("Missing 'start' or 'end' parameter for SUBSTRING operation.");
            throw new IllegalArgumentException("Missing 'start' or 'end' parameter for SUBSTRING operation.");
        }

        try {
            // 将起始和结束索引从字符串转换为整数
            int start = Integer.parseInt(startStr);
            int end = Integer.parseInt(endStr);

            // 验证索引范围是否有效
            if (start < 0 || end > input.length() || start > end) {
                logger.warn("Invalid substring indices: start={}, end={}, input.length={}", start, end, input.length());
                return input; // 返回原始字符串或根据需求自定义处理逻辑
            }

            // 提取子字符串并返回
            return input.substring(start, end);
        } catch (NumberFormatException e) {
            // 捕获索引格式错误异常并抛出更友好的错误提示
            logger.error("Invalid 'start' or 'end' parameter format for SUBSTRING operation.", e);
            throw new IllegalArgumentException("Invalid 'start' or 'end' parameter format for SUBSTRING operation.", e);
        } catch (Exception e) {
            // 捕获其他异常，记录错误日志并抛出运行时异常
            logger.error("Error during SUBSTRING operation.", e);
            throw new RuntimeException("SUBSTRING operation failed.", e);
        }
    }
}

