package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncryptionTest extends DataFilterAdapter {
    private static final long serialVersionUID = -7763445057050257428L;
    private static final Logger log = LogManager.getLogger(EncryptionTest.class);
    private Cipher cipher;

    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {
        return encrypt(todo, this.cipher);
    }

    public void init(String extPars) {
        String passwordEnc = "||||||||||||||||";
        try {
            AESencrp.init();
            passwordEnc = AESencrp.decrypt(extPars);
        }
        catch (Exception e1) {
            log.error("Exception occurred while running main()", e1);
        }
        try {
            Key securekey = getSecretKey(passwordEnc);
            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.cipher.init(1, securekey);
        }
        catch (Exception e) {
            log.error("Exception occurred while running main()", e);
        }
    }

    public Key getSecretKey(String key) {
        if (key == null) {
            key = "||||||||||||||||";
        }
        if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        int size = 16 - key.length();
        StringBuilder keyBuilder = new StringBuilder(key);
        for (int i = 0; i < size; i++) {
            keyBuilder.append("|");
        }
        key = keyBuilder.toString();
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    public String encrypt(String data, Cipher cipher) {
        if (("".equals(data)) || (" ".equals(data)) || ("NULL".equals(data))) {
            return data;
        }
        try {
            byte[] bt = cipher.doFinal(data.getBytes());

            //return byte2HexStr(bt);
            return Base64.getEncoder().encodeToString(bt);
        }
        catch (Exception e) {
            log.error("Exception occurred while running main()", e);
        }
        return "";
    }

    public static String byte2HexStr(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (byte value : b) {
            stmp = Integer.toHexString(value & 0xFF);
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";
        try {
            DataFilterAdapter adapter = (DataFilterAdapter)Class.forName("com.nsn.bighead.glassfish.filterse.adapter.EncryptionTest").newInstance();
            // adapter.init("SXYD_PWD");
            adapter.init("hljydydhbz_sjjm@");
            String re = adapter.handleBuffer(data, "12345678911", 0, "MLTE_S1U_HTTP", "");
            System.out.println("AES???????:" + re);
        }
        catch (Exception e) {
            log.error("Exception occurred while running main()", e);
        }
    }
}
