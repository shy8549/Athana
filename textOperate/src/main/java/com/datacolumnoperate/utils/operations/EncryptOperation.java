package com.datacolumnoperate.utils.operations;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description: EncryptOperation
 * @className: EncryptOperation
 * @date:2024/12/6 14:22
 */

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;

public class EncryptOperation implements Operation {
    @Override
    public String execute(String input, Map<String, String> params) throws Exception {
        String method = params.get("method");
        String key = params.get("key");

        if (!"AES".equalsIgnoreCase(method)) {
            throw new UnsupportedOperationException("Unsupported encryption method: " + method);
        }

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }
}

