package com.datacolumnoperate.utils.operations;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description: SubstringOperation
 * @className: SubstringOperation
 * @date:2024/12/6 14:22
 */

import java.util.Map;

public class SubstringOperation implements Operation {
    @Override
    public String execute(String input, Map<String, String> params) throws Exception {
        int start = Integer.parseInt(params.get("start"));
        int end = Integer.parseInt(params.get("end"));
        if (start < 0 || end > input.length() || start > end) {
            throw new IllegalArgumentException("Invalid substring indices for input: " + input);
        }
        return input.substring(start, end);
    }
}

