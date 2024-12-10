package com.datacolumnoperate.operations;

import java.util.Map;

/**
 * Operation 接口，定义所有操作的共同行为。
 */
public interface Operation {
    /**
     * 执行操作。
     *
     * @param input  输入字符串
     * @param params 操作参数
     * @return 处理后的字符串
     * @throws Exception 操作过程中可能抛出的异常
     */
    String execute(String input, Map<String, String> params) throws Exception;
}


