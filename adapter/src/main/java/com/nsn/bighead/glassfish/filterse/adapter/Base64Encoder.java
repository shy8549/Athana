package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description:
 * @className: Base64Encoder
 * @date:2024/12/4 10:36
 */
public class Base64Encoder extends DataFilterAdapter {

    private static final Logger log = LogManager.getLogger(Base64Encoder.class);

    public String encodeToBase64(String data) {
        if (data == null) {
            return null;
        }
        if (data.trim().isEmpty() || data.equalsIgnoreCase("NULL")) {
            return data;
        }
        try {
            // 将字符串直接使用 Base64 编码
            return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Base64 encoding error: " + e.getMessage(), e);
            throw new RuntimeException("Error during Base64 encoding", e);
        }
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {
        String result = null;
        try {
            if (todo != null && !todo.isEmpty()) {
                result = encodeToBase64(todo);
            } else {
                log.error("Input String is error : " + todo);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public void init(String extPars) {

    }

    public static void main(String[] args) {
        String[] data = new String[2];
        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.Base64Encoder").newInstance();
            adapter.init("");
            String re = adapter.handleBuffer(data, "375CF1702A3B52178108328E13518C25", 0, "MLTE_S1U_HTTP", "");
            System.out.println(re);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
