package com.datacolumnoperate.utils.operations;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description: Operation
 * @className: Operation
 * @date:2024/12/6 14:20
 */
public interface Operation {
    String execute(String input, java.util.Map<String, String> params) throws Exception;
}
