package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StringUtils extends DataFilterAdapter {

    private static final Logger log = LogManager.getLogger(StringUtils.class);

    public static String extractAfter86Prefix(String input, int length, boolean fromStart) {
        if (input == null || length < 0) {
            return input;
        }

        String remaining = null;

        // 匹配 +86 或 86 开头
        if (input.startsWith("+86")) {
            remaining = input.substring(3);
        } else if (input.startsWith("86")) {
            remaining = input.substring(2);
        } else if (input.length() >= 11) {
            // input 本身大于等于11位，作为 remaining
            remaining = input;
        } else {
            return input; // 不满足以上条件，原样返回
        }

        if (remaining.length() < length) {
            return input; // 剩余长度不足，原样返回
        }

        return fromStart
                ? remaining.substring(0, length)
                : remaining.substring(remaining.length() - length);
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {

        if (todo != null && !todo.isEmpty()) {
            String[] pars = org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
            boolean fromStart = false;
            int length = -1;
            if (pars.length == 2) {

                fromStart = Boolean.parseBoolean(pars[1]);
                length = Integer.parseInt(pars[0]);
            }
            return extractAfter86Prefix(todo,  length, fromStart);
        } else {
            return todo;
        }
    }

    public static void main(String[] args) {

        String xdrLine = "2024-11-05 15:57:48.734|2024-11-05 15:57:49.621|111|3|14|4659|4799|17759|45571||||460078457850390||8662182001269478||2569807482|2|58|0|1|12345678911|175368270|326008244498190332|326008244498190332|326008244498190332";
        String[] data = xdrLine.split("\\|", -1);

        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.StringUtils").newInstance();
            String re = adapter.handleBuffer(data, "abc00013333333335", 0, "MLTE_S1U_HTTP", "5,true");
            System.out.println(re);
        }catch (Exception e){
            log.error("Exception occurred while running main()", e);
        }
    }
}
