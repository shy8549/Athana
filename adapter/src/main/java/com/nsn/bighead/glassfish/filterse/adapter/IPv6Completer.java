package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description:
 * @className: IPv6Completer
 * @date:2024/1/19 14:27
 */
public class IPv6Completer extends DataFilterAdapter {

    private static final long serialVersionUID = 1059886914848214965L;
    private static final Logger log = LogManager.getLogger(IPv6Completer.class);

    private static boolean isValidIPv6(String ip) {
        // Remove all whitespace and invisible characters
        String cleanedIp = ip.replaceAll("\\s+", "");

        String regex = "^(" +
                "(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|" +
                "(?:[0-9a-fA-F]{1,4}:){1,7}:|" +
                "(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
                "(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}|" +
                "(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}|" +
                "(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}|" +
                "(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}|" +
                "[0-9a-fA-F]{1,4}::(?:[0-9a-fA-F]{1,4}:){1,5}|" +
                "::(?:[0-9a-fA-F]{1,4}:){1,6}[0-9a-fA-F]{1,4}|" +
                "::(?:[0-9a-fA-F]{1,4}:){1,7}|"+
                "::(?:[0-9a-fA-F]{1,4}:){0,5}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])(?:\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])){3}"+
                ")%?.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanedIp);
        return matcher.matches();
    }

    private static String[] expandPart(String[] part) {
        String[] expanded = new String[part.length];
        for (int i = 0; i < part.length; i++) {
            expanded[i] = String.format("%1$4s", part[i]).replace(' ', '0');
        }
        return expanded;
    }

    public static String expandIPv6Address(String shortenedIPv6) {
        // Remove all whitespace and invisible characters
        String cleanedIPv6 = shortenedIPv6.replaceAll("\\s+", "");

        String[] arr = cleanedIPv6.split("::", -1);
        String[] firstPart = arr[0].split(":", -1);
        String[] secondPart = arr.length > 1 ? arr[1].split(":", -1) : new String[0];

        String[] expandedFirstPart = expandPart(firstPart);
        String[] expandedSecondPart = expandPart(secondPart);

        int missingBlocks = 8 - expandedFirstPart.length - expandedSecondPart.length;
        String[] missingPart = new String[missingBlocks];
        Arrays.fill(missingPart, "0000");

        String[] fullAddress = new String[8];
        System.arraycopy(expandedFirstPart, 0, fullAddress, 0, expandedFirstPart.length);
        System.arraycopy(missingPart, 0, fullAddress, expandedFirstPart.length, missingBlocks);
        System.arraycopy(expandedSecondPart, 0, fullAddress, expandedFirstPart.length + missingBlocks, expandedSecondPart.length);

        return String.join(":", fullAddress).toUpperCase();
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {

        if (todo != null && !todo.isEmpty()) {
            if (isValidIPv6(todo)) {
                return expandIPv6Address(todo).toUpperCase();
            } else {
                return todo;
            }
        } else {
            return "";
        }
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service) {
        return this.handleBuffer(data, todo, columnX, service, ",");
    }

    @Override
    public void init(String extPars) {
    }

    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";
//        System.out.println(isValidIPv6("240:fF:F1t0:1009::10"));
        System.out.println(isValidIPv6("2001:db8:85a3::8a2e:370:7334"));
        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.IPv6Completer").newInstance();
            adapter.init("");
            String re = adapter.handleBuffer(data, "2001:db8:85a3::8a2e:370:7334", 0, "MLTE_S1U_HTTP", "");
            System.out.println(re);
        }catch (Exception e){
            log.error("Exception occurred while running main()", e);
        }
    }

}
