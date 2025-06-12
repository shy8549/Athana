package com.tongtech.transform;

import javax.crypto.*;
import javax.crypto.spec.*;

public class CryptoUtils {
    public static byte[] encryptAES(byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] decryptAES(byte[] data, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec k = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

