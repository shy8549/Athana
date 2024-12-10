package com.datacolumnoperate.common;

import java.util.Map;

/**
 * 操作配置类，定义操作的类型和参数。
 */
public class OperationConfig {
    private OperationType type;
    private Map<String, String> params;

    // Getters and Setters

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "OperationConfig{" +
                "type=" + type +
                ", params=" + params +
                '}';
    }
}
