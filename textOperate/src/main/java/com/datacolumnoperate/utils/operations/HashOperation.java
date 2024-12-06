package com.datacolumnoperate.utils.operations;

import java.security.MessageDigest;
import java.util.Map;

public class HashOperation implements Operation {
    @Override
    public String execute(String input, Map<String, String> params) throws Exception {
        String algorithm = params.get("algorithm"); // 如SHA-256
        if (algorithm == null) {
            throw new IllegalArgumentException("Hash algorithm not specified.");
        }
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        // 转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

