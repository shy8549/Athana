package com.datacolumnoperate.common;

/**
 * OperationType 枚举定义了所有支持的操作类型。
 */
public enum OperationType {
    ENCRYPT,
    HASH,
    REGEX_REPLACE,
    SUBSTRING,
    TIME_CONVERT,
    UNSUPPORTED // 新增的枚举值，用于测试不支持的操作类型
}


