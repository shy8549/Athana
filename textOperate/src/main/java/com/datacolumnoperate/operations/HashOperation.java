package com.datacolumnoperate.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * HashOperation 用于生成数据的哈希值。
 *
 * 支持的哈希算法由参数中提供的 "algorithm" 决定，例如：
 * - "SHA-256": 使用 SHA-256 哈希算法生成固定长度的散列值。
 * - "MD5": 使用 MD5 哈希算法生成固定长度的散列值。
 *
 * 可选的参数 "key" 用于在输入数据前附加密钥，以提高哈希结果的安全性。
 *
 * 配置参数示例（在 JSON 配置中 OperationConfig 的 params 部分）：
 * - 对于 SHA-256 哈希：
 *   "params": { "algorithm": "SHA-256" }
 * - 对于 MD5 哈希并附加密钥：
 *   "params": { "algorithm": "MD5", "key": "mySecretKey" }
 *
 * 错误处理：
 * - 如果未指定 "algorithm"，将抛出 IllegalArgumentException。
 * - 如果发生其他异常（如不支持的哈希算法），将记录错误日志并抛出 RuntimeException。
 */
public class HashOperation implements Operation {
    // 定义日志记录器，用于记录操作的日志信息
    private static final Logger logger = LoggerFactory.getLogger(HashOperation.class);

    /**
     * 执行哈希操作。
     *
     * @param input  要处理的输入字符串（列数据的原始值）
     * @param params 包含必要参数的 Map，包括：
     *               - "algorithm": 指定哈希算法（如 "SHA-256", "MD5"）
     *               - "key": 可选参数，附加到输入字符串之前以提高安全性
     *
     * @return 哈希值的十六进制字符串表示
     *
     * @throws IllegalArgumentException 如果未指定哈希算法
     * @throws RuntimeException 如果哈希操作失败
     */
    @Override
    public String execute(String input, Map<String, String> params) {
        // 获取 "algorithm" 参数，指定要使用的哈希算法
        String algorithm = params.get("algorithm"); // 如 "SHA-256", "MD5"
        // 可选的 "key" 参数，用于附加到输入字符串之前
        String key = params.get("key");

        // 验证是否指定了哈希算法
        if (algorithm == null) {
            logger.error("Hash algorithm not specified.");
            throw new IllegalArgumentException("Hash algorithm not specified.");
        }

        try {
            // 获取指定的哈希算法实例
            MessageDigest md = MessageDigest.getInstance(algorithm);

            // 如果提供了密钥，则将密钥与输入数据拼接，否则仅使用输入数据
            String data = (key != null) ? (key + input) : input;

            // 执行哈希运算
            byte[] digest = md.digest(data.getBytes("UTF-8"));

            // 将哈希结果转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            // 返回最终的哈希字符串
            return sb.toString();
        } catch (Exception e) {
            // 如果哈希过程中发生异常，记录错误日志并抛出运行时异常
            logger.error("Error during HASH operation.", e);
            throw new RuntimeException("HASH operation failed.", e);
        }
    }
}

