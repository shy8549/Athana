package com.datacolumnoperate.operations;

import java.util.Map;

/**
 * Operation 接口，定义所有操作的共同行为。
 *
 * 此接口是所有列操作的基础，每种操作都需要实现该接口，并提供具体的执行逻辑。
 *
 * 应用场景：
 * - 对列值进行加密（如 AES 或 MD5 哈希）。
 * - 执行正则替换操作。
 * - 提取子字符串。
 * - 转换时间格式。
 *
 * 接口设计：
 * - `execute` 方法接收输入字符串以及相关参数，返回处理后的字符串。
 * - 实现类根据具体的操作类型对输入值进行处理。
 *
 * 实现方式：
 * 1. 每个具体操作需要实现 `Operation` 接口，并提供其独特的逻辑。
 * 2. 使用依赖注入（如 Google Guice），可在运行时根据配置动态加载所需的操作类型。
 *
 * 示例：
 * 假设有一个 `EncryptOperation` 类实现了此接口：
 * - 输入字符串：`"hello"`
 * - 参数：`{"method": "AES", "key": "mySecretKey12345"}`
 * - 输出字符串：`"encrypted_string"`
 */
public interface Operation {
    /**
     * 执行操作。
     *
     * @param input  输入字符串。通常是从文件中读取的一列的原始值。
     *               例如：对输入数据 `"hello"` 执行加密操作。
     * @param params 操作参数。使用键值对形式存储，包含操作所需的所有配置信息。
     *               例如：
     *               - 对于加密操作：`{"method": "AES", "key": "mySecretKey12345"}`
     *               - 对于正则替换操作：`{"regex": "\\d{3}-\\d{4}", "replacement": "****-****"}`
     *               - 对于子字符串操作：`{"start": "0", "end": "5"}`
     * @return 处理后的字符串。返回值根据具体操作而定，例如加密后的字符串或正则替换后的结果。
     *         例如：输入 `"hello"`，返回 `"encrypted_string"`。
     * @throws Exception 如果操作过程中发生错误，抛出异常以提示调用者处理。
     *                   常见异常包括：
     *                   - `IllegalArgumentException`：参数不完整或无效。
     *                   - `RuntimeException`：操作逻辑失败（如加密错误或正则不匹配）。
     */
    String execute(String input, Map<String, String> params) throws Exception;
}



