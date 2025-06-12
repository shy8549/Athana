package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: Jstorm 加密列函数（先使用 AES-128-ECB 模式加密，再使用 Base64 转码）
 * @Author: liangpeng
 * @Date: 2024/11/28 17:28:20
 **/
public class AesEncodeAdapter extends DataFilterAdapter {

    private static final Logger log = LogManager.getLogger(AesEncodeAdapter.class);

    private static final Map<String, Cipher> map = new HashMap<>();

    private static final Lock LOCK = new ReentrantLock();

    private String secKey;

    /**
     * 处理密钥，使其长度为16个字符。
     * 如果密钥为空，则使用字符 '|' 补全至16位。
     * 如果密钥长度大于16位，则截取前16位。
     * 如果密钥长度小于16位，则使用字符 '|' 补全至16位。
     *
     * @param key 原始密钥
     * @return 处理后的16位密钥
     */
    private String processKey(String key) {
        if (key == null) {
            key = "";
        }
        if (key.length() > 16) {
            return key.substring(0, 16);
        } else {
            StringBuilder sb = new StringBuilder(key);
            while (sb.length() < 16) {
                sb.append('|');
            }
            return sb.toString();
        }
    }

    /**
     * 获取 Cipher 实例，使用处理后的密钥
     *
     * @param key 处理后的密钥
     * @return Cipher 实例
     */
    private Cipher getInstance(String key) {
        Cipher cipher = map.get(key);
        if (cipher == null)
            try {
                LOCK.lock();
                cipher = map.get(key);
                if (cipher == null) {
                    if (key == null)
                        throw new Exception("AES 密钥为 null;");
                    byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                    if (keyBytes.length != 16) { // 现在密钥长度已经被处理为16
                        throw new Exception("无效的 AES 密钥长度: " + keyBytes.length + " 字节。要求的长度：16 字节。");
                    }
                    Key securekey = new SecretKeySpec(keyBytes, "AES");
                    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, securekey);
                    map.put(key, cipher);
                }
            } catch (Exception e) {
                log.error("初始化 AES 错误; ", e);
            } finally {
                LOCK.unlock();
            }
        return cipher;
    }

    /**
     * 加密方法
     *
     * @param data 原始数据
     * @param key  密钥
     * @return Base64 编码的加密字符串
     * @throws Exception 加密过程中可能抛出的异常
     */
    public String encrypt(String data, String key) throws Exception {
        if (data == null)
            return null;
        if (data.trim().isEmpty() || data.equalsIgnoreCase("NULL")
                || key == null || key.trim().isEmpty() || key.equalsIgnoreCase("NULL"))
            return data;
        // 处理密钥
        String processedKey = processKey(key);
        try {
            Cipher cipher = getInstance(processedKey);
            if (cipher == null) {
                throw new Exception("Cipher 初始化失败。");
            }
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("AesEncodeAdapter 加密错误: " + e.getMessage());
            throw new Exception(e);
        }
    }

    @Override
    public void init(String extPars) {
        secKey = processKey(extPars);
    }

    /**
     * 重写抽象父类的方法
     *
     * @param datas    原始数据列表（由所有列构成的数组）
     * @param todo     要操作的字段（目标数据）
     * @param columnX  要操作的字段所属的列号（从0开始算）
     * @param service  业务名称（jstorm 拓扑名称，例如：MLTE_S1U_HTTP）
     * @return 返回操作后的值
     */
    @Override
    public String handleBuffer(String[] datas, String todo, int columnX, String service, String extPars) {
        String result = null;
        try {
            if (todo != null && !todo.isEmpty()) {
                result = encrypt(todo, secKey);
            } else {
                result = todo;
                // log.error("输入字符串为空: " + todo);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {
        // System.out.println(System.getProperty("java.class.path"));
        String xdrLing = "2024-11-05 15:57:48.734|2024-11-05 15:57:49.621|111|3|14|4659|4799|17759|45571||||460078457850390||8662182001269478||2569807482|2|58|0|1|12345678911|175368270|326008244498190332|326008244498190332|326008244498190332";
        String[] data = xdrLing.split("\\|", -1);
        try {
            AesEncodeAdapter adapter = new AesEncodeAdapter(); // 直接实例化
            // 使用不同长度的密钥进行测试
            adapter.init("SXYD_PWD"); // 原始8位密钥，处理后为 "SXYD_PWD||||||||"
//            adapter.init("hljydydhbz_sjjm@"); // 16位密钥，不需要处理
            // adapter.init("thisisaverylongkeythatexceedssixteencharacters"); // 超过16位，处理后截取前16位
//            String re = adapter.handleBuffer(data, "12345678911", 21, "MLTE_S1U_HTTP", null);
            String re = adapter.handleBuffer(data, "13796617016", 21, "MLTE_S1U_HTTP", null);

            System.out.println("程序运行结果: " + re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
