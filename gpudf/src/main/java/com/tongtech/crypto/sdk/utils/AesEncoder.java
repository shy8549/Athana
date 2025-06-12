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
 * @Date: 2024/7/18 13:49:13
 **/
public class AesEncoder {

//    private static final Logger log = LoggerFactory.getLogger(AesEncoder.class);

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
                    cipher.init(1, securekey);
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

    private String byte2HexStr(byte[] b) {
        StringBuilder sb = StringBuilderUtil.getLocalStringBuilder();
        for (byte value : b) {
            String hexValue = Integer.toHexString(value & 0xFF);
            if (hexValue.length() == 1) {
                sb.append("0").append(hexValue);
            } else {
                sb.append(hexValue);
            }
        }
        return sb.toString().toUpperCase();
    }

    public String eval(Object in, String key) {
        if (in == null)
            return null;
        String inStr = in.toString();
        if (inStr.isEmpty() || inStr.equalsIgnoreCase("NULL") || inStr.equals(" "))
            return inStr;
        try {
            Cipher cipher = getInstance(key);
            byte[] bytes = cipher.doFinal(inStr.getBytes());
            return byte2HexStr(bytes);
        } catch (Exception e) {
//            log.error("AesEncoder eval error; ", e);
            System.out.println("AesEncoder eval error; " + e);
            return inStr;
        }
    }
}
