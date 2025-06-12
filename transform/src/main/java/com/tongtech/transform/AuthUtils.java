package com.tongtech.transform;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthUtils {
    // 简单Token校验
    public static boolean isValidToken(String token, String expectedToken) {
        return expectedToken != null && expectedToken.equals(token);
    }
    // HMAC-SHA256签名（可用于未来增强防重放）
    public static String hmacSha256(String message, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] bytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
