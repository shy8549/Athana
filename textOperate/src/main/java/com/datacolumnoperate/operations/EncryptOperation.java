package com.datacolumnoperate.operations;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

/**
 * EncryptOperation 类负责执行列数据的加密或哈希操作。
 *
 * 支持的操作类型由参数中提供的 "method" 决定：
 * - AES：需要 16 字节密钥（例如 "mySecretKey12345" 刚好16字节），使用 "AES/CBC/PKCS5Padding" 模式和16字节的 IV（初始向量）进行加密。
 * - DES：需要 8 字节密钥（例如 "myDESKey" 刚好8字节），使用 "DES/CBC/PKCS5Padding" 模式和8字节的 IV 进行加密。
 * - MD5：对输入字符串进行 MD5 哈希处理，不需要密钥，只返回 32 位十六进制哈希字符串。
 *
 * 配置参数示例（在 JSON 配置中 OperationConfig 的 params 部分）：
 * - 对于 AES 加密：
 *   "params": { "method": "AES", "key": "mySecretKey12345" }
 * - 对于 DES 加密：
 *   "params": { "method": "DES", "key": "myDESKey" }
 * - 对于 MD5 哈希：
 *   "params": { "method": "MD5" }
 *
 * 当输入的加密方法不在支持范围内，抛出 UnsupportedOperationException。
 * 当密钥不满足长度要求或 method 未指定时，抛出 IllegalArgumentException。
 * 对于其他异常（如加密过程中的异常），统一包装成 RuntimeException 并携带原始异常信息，以便于调试。
 */
public class EncryptOperation implements Operation {

    /**
     * 执行加密或哈希操作。
     *
     * @param input 要处理的输入字符串（列数据的原始值）
     * @param params 包含必要参数的 Map，包括：
     *               - "method": 指定加密或哈希方法（AES、DES、MD5）
     *               - "key": 对于 AES 和 DES 加密方法需要指定密钥
     *
     * @return 加密或哈希后的字符串结果
     *
     * @throws IllegalArgumentException 如果 method 未指定或密钥长度不符合要求
     * @throws UnsupportedOperationException 如果使用了不支持的加密方法
     * @throws RuntimeException 如果加密或哈希过程发生其他错误
     */
    @Override
    public String execute(String input, Map<String, String> params) {
        // 从参数中获取加密方法和密钥
        String method = params.get("method");
        String key = params.get("key");

        // 验证 method 是否存在
        if (method == null) {
            throw new IllegalArgumentException("Encryption method not specified.");
        }

        try {
            switch (method) {
                case "AES":
                    // AES加密要求16字节密钥
                    if (key == null || key.length() != 16) {
                        throw new IllegalArgumentException("AES encryption requires a 16-byte key.");
                    }
                    Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                    SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
//                    IvParameterSpec aesIV = new IvParameterSpec(new byte[16]); // 16字节IV，全部为0字节
                    aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
                    byte[] aesEncrypted = aesCipher.doFinal(input.getBytes());
                    return Base64.getEncoder().encodeToString(aesEncrypted);

                case "DES":
                    // DES加密要求8字节密钥
                    if (key == null || key.length() != 8) {
                        throw new IllegalArgumentException("DES encryption requires an 8-byte key.");
                    }
                    Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                    SecretKeySpec desKey = new SecretKeySpec(key.getBytes(), "DES");
                    IvParameterSpec desIV = new IvParameterSpec(new byte[8]); // 8字节IV，全部为0字节
                    desCipher.init(Cipher.ENCRYPT_MODE, desKey, desIV);
                    byte[] desEncrypted = desCipher.doFinal(input.getBytes());
                    return Base64.getEncoder().encodeToString(desEncrypted);

                case "MD5":
                    // MD5是一种哈希操作，不需要密钥
                    MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                    byte[] md5Hash = md5Digest.digest(input.getBytes());
                    StringBuilder md5Hex = new StringBuilder();
                    for (byte b : md5Hash) {
                        md5Hex.append(String.format("%02x", b));
                    }
                    return md5Hex.toString();

                default:
                    // 不支持的加密方法
                    throw new UnsupportedOperationException("Unsupported encryption method: " + method);
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            // 对期望的异常类型（UnsupportedOperationException、IllegalArgumentException）直接抛出，不进行包装
            throw e;
        } catch (Exception e) {
            // 对其他异常（如加密过程中的失败）进行包装，以便日志中可看到根本原因
            throw new RuntimeException("Encryption operation failed.", e);
        }
    }
}
