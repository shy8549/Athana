package com.datacolumnoperate.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * RegexReplaceOperation 用于根据正则表达式替换指定列中的内容。
 */
public class RegexReplaceOperation implements Operation {
    private static final Logger logger = LoggerFactory.getLogger(RegexReplaceOperation.class);

    /**
     * 执行正则替换操作。
     *
     * @param input  原始输入字符串。
     * @param params 操作参数，包括 "regex" 和 "replacement"。
     * @return 替换后的字符串。
     */
    @Override
    public String execute(String input, Map<String, String> params) {
        String regex = params.get("regex");
        String replacement = params.get("replacement");

        if (regex == null || replacement == null) {
            logger.error("Missing 'regex' or 'replacement' parameter for REGEX_REPLACE operation.");
            throw new IllegalArgumentException("Missing 'regex' or 'replacement' parameter for REGEX_REPLACE operation.");
        }

        try {
            // 使用正则表达式进行替换
            return input.replaceAll(regex, replacement);
        } catch (Exception e) {
            logger.error("Error during REGEX_REPLACE operation.", e);
            throw new RuntimeException("REGEX_REPLACE operation failed.", e);
        }
    }
}
