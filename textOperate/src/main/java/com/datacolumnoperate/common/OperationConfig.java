package com.datacolumnoperate.common;

import java.util.Map;

/**
 * OperationConfig 类用于描述对特定列所要执行的一种特定操作以及该操作所需的参数。
 *
 * 在程序中，每个列的处理配置（ColumnOperationConfig）中包含多个 OperationConfig 对象，
 * 每个 OperationConfig 表示对该列执行的某个独立操作（例如加密、正则替换、子字符串提取、时间转换等）。
 *
 * OperationConfig 包含两个核心信息：
 * 1. type：操作类型，通过枚举 OperationType 表示（如 ENCRYPT、REGEX_REPLACE、SUBSTRING、TIME_CONVERT、HASH 等）。
 *    程序在实际执行时会通过类型选择对应的 Operation 实现类。
 * 2. params：操作所需的参数，以键值对形式存储。例如：
 *    - 对于 ENCRYPT 操作，params 可能包含 {"method": "AES", "key": "mySecretKey12345"}。
 *    - 对于 REGEX_REPLACE 操作，params 可能包含 {"regex": "\\d{3}-\\d{4}", "replacement": "****-****"}。
 *    - 对于 SUBSTRING 操作，params 可能包含 {"start": "0", "end": "5"}。
 *
 * 在程序运行中，根据 JSON 配置文件映射得到的 OperationConfig 对象提供了执行操作时所需的详细信息，
 * 避免在代码中硬编码参数。同时，如果未来要支持新类型的操作，只需在配置中添加对应的 type 和 params，
 * 而无需修改已有的代码逻辑（只需要在 OperationFactory 中绑定新的操作类型与实现类）。
 */
public class OperationConfig {
    /**
     * type 表示操作类型，是一个枚举值（OperationType）。
     * 程序会根据此类型选择对应的 Operation 实现类执行操作。
     */
    private OperationType type;

    /**
     * params 是一个字符串键值对的 Map，用于存储此操作所需的所有参数。
     * 不同的操作类型会使用不同的参数键值。
     * 例如：
     * - 对 ENCRYPT 操作：{"method": "AES", "key": "mySecretKey12345"}
     * - 对 REGEX_REPLACE 操作：{"regex": "\\d{3}-\\d{4}", "replacement": "****-****"}
     */
    private Map<String, String> params;

    /**
     * 获取操作类型
     *
     * @return OperationType 枚举值，表示此操作的类型
     */
    public OperationType getType() {
        return type;
    }

    /**
     * 设置操作类型
     *
     * @param type OperationType 枚举值，表示此操作的类型
     */
    public void setType(OperationType type) {
        this.type = type;
    }

    /**
     * 获取此操作的参数表
     *
     * @return 一个 Map，其中键和值均为 String，用于存储此操作所需的参数
     */
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * 设置此操作的参数表
     *
     * @param params 一个字符串键值对 Map，用于指定此操作所需的参数
     */
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 返回 OperationConfig 对象的字符串表示形式，用于调试和日志输出。
     *
     * @return 包含 type 和 params 信息的字符串
     */
    @Override
    public String toString() {
        return "OperationConfig{" +
                "type=" + type +
                ", params=" + params +
                '}';
    }
}

