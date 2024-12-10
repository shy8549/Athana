package com.datacolumnoperate.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * SubstringOperation 用于提取字符串的子部分。
 */
public class SubstringOperation implements Operation {
    private static final Logger logger = LoggerFactory.getLogger(SubstringOperation.class);

    @Override
    public String execute(String input, Map<String, String> params) {
        String startStr = params.get("start");
        String endStr = params.get("end");

        if (startStr == null || endStr == null) {
            logger.error("Missing 'start' or 'end' parameter for SUBSTRING operation.");
            throw new IllegalArgumentException("Missing 'start' or 'end' parameter for SUBSTRING operation.");
        }

        try {
            int start = Integer.parseInt(startStr);
            int end = Integer.parseInt(endStr);

            if (start < 0 || end > input.length() || start > end) {
                logger.warn("Invalid substring indices: start={}, end={}, input.length={}", start, end, input.length());
                return input; // 返回原始字符串或根据需求处理
            }

            return input.substring(start, end);
        } catch (NumberFormatException e) {
            logger.error("Invalid 'start' or 'end' parameter format for SUBSTRING operation.", e);
            throw new IllegalArgumentException("Invalid 'start' or 'end' parameter format for SUBSTRING operation.", e);
        } catch (Exception e) {
            logger.error("Error during SUBSTRING operation.", e);
            throw new RuntimeException("SUBSTRING operation failed.", e);
        }
    }
}
