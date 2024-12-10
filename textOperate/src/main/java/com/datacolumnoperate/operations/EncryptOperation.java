package com.datacolumnoperate.operations;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

/**
 * EncryptOperation 负责执行加密操作。
 */
public class EncryptOperation implements Operation {

    @Override
    public String execute(String input, Map<String, String> params) {
        String method = params.get("method");
        String key = params.get("key");

        if (method == null) {
            throw new IllegalArgumentException("Encryption method not specified.");
        }

        try {
            switch (method) {
                case "AES":
                    if (key == null || key.length() != 16) {
                        throw new IllegalArgumentException("AES encryption requires a 16-byte key.");
                    }
                    Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), "AES");
                    IvParameterSpec aesIV = new IvParameterSpec(new byte[16]); // 16字节IV
                    aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, aesIV);
                    byte[] aesEncrypted = aesCipher.doFinal(input.getBytes());
                    return Base64.getEncoder().encodeToString(aesEncrypted);

                case "DES":
                    if (key == null || key.length() != 8) {
                        throw new IllegalArgumentException("DES encryption requires an 8-byte key.");
                    }
                    Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                    SecretKeySpec desKey = new SecretKeySpec(key.getBytes(), "DES");
                    IvParameterSpec desIV = new IvParameterSpec(new byte[8]); // 8字节IV
                    desCipher.init(Cipher.ENCRYPT_MODE, desKey, desIV);
                    byte[] desEncrypted = desCipher.doFinal(input.getBytes());
                    return Base64.getEncoder().encodeToString(desEncrypted);

                case "MD5":
                    MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                    byte[] md5Hash = md5Digest.digest(input.getBytes());
                    StringBuilder md5Hex = new StringBuilder();
                    for (byte b : md5Hash) {
                        md5Hex.append(String.format("%02x", b));
                    }
                    return md5Hex.toString();

                default:
                    throw new UnsupportedOperationException("Unsupported encryption method: " + method);
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            throw e; // 直接抛出，不包装
        } catch (Exception e) {
            throw new RuntimeException("Encryption operation failed.", e);
        }
    }
}
