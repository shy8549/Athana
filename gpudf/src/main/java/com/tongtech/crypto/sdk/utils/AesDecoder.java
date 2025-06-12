package com.tongtech.crypto.sdk.utils;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: liangpeng
 * @Date: 2024/7/18 14:08:03
 **/
public class AesDecoder {

//    private static final Logger log = LoggerFactory.getLogger(AesDecoder.class);

    private static final String HEX_STR = "0123456789ABCDEF";

    private static final Map<String, Cipher> map = new HashMap<>();

    private static final Lock LOCK = new ReentrantLock();

    private Cipher getInstance(String key) {
        Cipher cipher = map.get(key);
        if (cipher == null)
            try {
                LOCK.lock();
                cipher = map.get(key);
                if (cipher == null) {
                    if (key == null)
                        throw new Exception("AES key is null;");
                    Key securekey = new SecretKeySpec(key.getBytes(), "AES");
                    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                    cipher.init(2, securekey);
                    map.put(key, cipher);
                }
            } catch (Exception e) {
//                log.error("init AES error; ", e);
                System.out.println("init AES error; " + e);
            } finally {
                LOCK.unlock();
            }
        return cipher;
    }

    public String eval(Object in, String key) {
        if (in == null)
            return null;
        String inStr = in.toString();
        if (inStr.isEmpty() || inStr.equalsIgnoreCase("NULL") || inStr.equals(" "))
            return inStr;
        try {
            byte[] bytesValue = hexStr2Bytes(inStr);
            Cipher cipher = getInstance(key);
            byte[] bytes = cipher.doFinal(bytesValue);
            return new String(bytes);
        } catch (Exception e) {
//            log.error("AesDecoder eval error; ", e);
            System.out.println("AesDecoder eval error; " + e);
            return inStr;
        }
    }

    private byte[] hexStr2Bytes(String hexStr) {
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int n = "0123456789ABCDEF".indexOf(hexs[2 * i]) * 16;
            n += "0123456789ABCDEF".indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte)(n & 0xFF);
        }
        return bytes;
    }
}
