package com.datacolumnoperate.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * HashOperation 用于生成数据的哈希值。
 */
public class HashOperation implements Operation {
    private static final Logger logger = LoggerFactory.getLogger(HashOperation.class);

    @Override
    public String execute(String input, Map<String, String> params) {
        String algorithm = params.get("algorithm"); // 如 "SHA-256", "MD5"
        String key = params.get("key"); // 可选

        if (algorithm == null) {
            logger.error("Hash algorithm not specified.");
            throw new IllegalArgumentException("Hash algorithm not specified.");
        }

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String data = (key != null) ? (key + input) : input;
            byte[] digest = md.digest(data.getBytes("UTF-8"));

            // 将摘要转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            logger.error("Error during HASH operation.", e);
            throw new RuntimeException("HASH operation failed.", e);
        }
    }
}
