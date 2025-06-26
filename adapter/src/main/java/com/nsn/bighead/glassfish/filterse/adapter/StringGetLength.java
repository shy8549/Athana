package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringGetLength extends DataFilterAdapter {

    private static final Logger log = LogManager.getLogger(StringGetLength.class);

    public static int getStringLength(String str) {
        if (str == null || str.equals("null") || str.isEmpty()) {
            return 0;
        }
        return str.length();
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {
        return String.valueOf(getStringLength(todo));
    }

    public static void main(String[] args) {
        String xdrLine = "2024-11-05 15:57:48.734|2024-11-05 15:57:49.621|111|3|14|4659|4799|17759|45571||||460078457850390||8662182001269478||2569807482|2|58|0|1|12345678911|175368270|326008244498190332|326008244498190332|326008244498190332";
        String[] data = xdrLine.split("\\|", -1);

        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.StringGetLength").newInstance();
//            String re = adapter.handleBuffer(data, "+86100860", 0, "MLTE_S1U_HTTP", "");
            String re = adapter.handleBuffer(data, "null", 0, "MLTE_S1U_HTTP", "");
            System.out.println(re);
        }catch (Exception e){
            log.error("Exception occurred while running main()", e);
        }
    }
}
