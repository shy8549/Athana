package com.datacolumnoperate.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * RegexReplaceOperation 用于根据正则表达式替换指定列中的内容。
 *
 * 该类实现了 `Operation` 接口，通过读取配置参数中的正则表达式（`regex`）和替换内容（`replacement`），
 * 对输入字符串执行匹配和替换操作。
 *
 * 应用场景：
 * - 数据清洗：根据正则表达式过滤掉敏感信息或格式化数据。
 * - 内容替换：将匹配特定模式的内容替换为指定文本（如将电话号码中间部分替换为 "****"）。
 *
 * 配置参数：
 * - `regex`：必需参数，指定正则表达式，用于匹配输入字符串中的目标内容。
 * - `replacement`：必需参数，指定替换内容，将匹配内容替换为该值。
 *
 * 示例：
 * 假设列值为 `"123-4567-890"`, 配置如下：
 * - `regex`: `\\d{3}-\\d{4}`
 * - `replacement`: `"***-****"`
 * 处理结果为：`"***-****-890"`
 *
 * 错误处理：
 * - 如果缺少 `regex` 或 `replacement` 参数，抛出 `IllegalArgumentException`。
 * - 如果正则替换过程中出现异常，记录错误日志并抛出 `RuntimeException`。
 */
public class RegexReplaceOperation implements Operation {
    // 定义日志记录器，用于记录操作的日志信息
    private static final Logger logger = LoggerFactory.getLogger(RegexReplaceOperation.class);

    /**
     * 执行正则替换操作。
     *
     * @param input  原始输入字符串。通常是列中的值，例如 `"123-4567-890"`。
     * @param params 操作参数。必须包含以下键值对：
     *               - `regex`：用于匹配的正则表达式。
     *               - `replacement`：用于替换匹配内容的字符串。
     * @return 替换后的字符串。例如，输入 `"123-4567-890"`，
     *         配置 `regex: \\d{3}-\\d{4}` 和 `replacement: ***-****`，返回结果为 `"***-****-890"`。
     * @throws IllegalArgumentException 如果缺少 `regex` 或 `replacement` 参数。
     * @throws RuntimeException 如果替换操作失败。
     */
    @Override
    public String execute(String input, Map<String, String> params) {
        // 从参数中获取正则表达式和替换内容
        String regex = params.get("regex");
        String replacement = params.get("replacement");

        // 验证参数是否完整
        if (regex == null || replacement == null) {
            logger.error("Missing 'regex' or 'replacement' parameter for REGEX_REPLACE operation.");
            throw new IllegalArgumentException("Missing 'regex' or 'replacement' parameter for REGEX_REPLACE operation.");
        }

        try {
            // 使用正则表达式进行替换
            return input.replaceAll(regex, replacement);
        } catch (Exception e) {
            // 捕获替换过程中的任何异常，记录错误日志并抛出运行时异常
            logger.error("Error during REGEX_REPLACE operation.", e);
            throw new RuntimeException("REGEX_REPLACE operation failed.", e);
        }
    }
}

