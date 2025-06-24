package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IPHexConverter extends DataFilterAdapter {
    private static final long serialVersionUID = -1;
    private static final Logger log = LogManager.getLogger(IPHexConverter.class);

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {

        if (todo == null || todo.isEmpty()) {
            return "";
        }

        if (isValidIPv4Hex(todo)) {
            return hexToIpv4(todo);
        } else if (isValidIPv6Hex(todo)) {
            return hexToIpv6(todo);
        }
        return todo;
    }

    @Override
    public void init(String extPars) {

    }

    /**
     * 将IPv4地址转换为16进制格式（不带 0x）
     * @param ip IPv4地址（如 "100.89.3.226"）
     * @return 16进制格式字符串（如 "645903e2"）
     */
    public static String ipv4ToHex(String ip) {
        if (ip == null || ip.isEmpty()) {
            throw new IllegalArgumentException("Invalid IPv4 address");
        }

        String[] octets = ip.split("\\.");
        if (octets.length != 4) {
            throw new IllegalArgumentException("Invalid IPv4 address format");
        }

        StringBuilder hexString = new StringBuilder();
        for (String octet : octets) {
            int num = Integer.parseInt(octet);
            if (num < 0 || num > 255) {
                throw new IllegalArgumentException("IPv4 address octet out of range: " + octet);
            }
            hexString.append(String.format("%02x", num)); // 2位16进制，不足补0
        }

        return hexString.toString();
    }

    /**
     * 将16进制格式转换回IPv4地址
     * @param hex 16进制字符串（如 "645903e2"）
     * @return IPv4地址（如 "100.89.3.226"）
     */
    public static String hexToIpv4(String hex) {
        if (hex == null || hex.length() != 8) {
            throw new IllegalArgumentException("Invalid hex string length, must be 8 characters.");
        }

        StringBuilder ipString = new StringBuilder();
        for (int i = 0; i < 8; i += 2) {
            int octet = Integer.parseInt(hex.substring(i, i + 2), 16);
            ipString.append(octet);
            if (i < 6) {
                ipString.append(".");
            }
        }

        return ipString.toString();
    }

    /**
     * 将IPv6地址转换为16进制格式（不带 0x）
     */
    public static String ipv6ToHex(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            byte[] bytes = inetAddress.getAddress();
            return new BigInteger(1, bytes).toString(16);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid IPv6 address");
        }
    }

    /**
     * 将16进制格式转换回IPv6地址
     */
    public static String hexToIpv6(String hex) {
        if (hex == null || hex.length() != 32) {
            throw new IllegalArgumentException("Invalid hex string length, must be 32 characters.");
        }

        StringBuilder ipv6 = new StringBuilder();
        for (int i = 0; i < 32; i += 4) {
            ipv6.append(hex, i, i + 4);
            if (i < 28) {
                ipv6.append(":");
            }
        }
        return ipv6.toString();
    }

    private static boolean isValidIPv4Hex(String hex) {
        return hex != null && hex.length() == 8 && hex.matches("^[0-9a-fA-F]{8}$");
    }

    private static boolean isValidIPv6Hex(String hex) {
        return hex != null && hex.length() == 32 && hex.matches("^[0-9a-fA-F]{32}$");
    }


    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";

        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.IPHexConverter").newInstance();
            adapter.init("");
//            String re = adapter.handleBuffer(data, "100.89.3.226", 0, "MLTE_S1U_HTTP", "");
//            System.out.println(re);
            System.out.println(adapter.handleBuffer(data, "645903e2", 0, "MLTE_S1U_HTTP", "")); // 100.89.3.226
            System.out.println(adapter.handleBuffer(data, "20010db8000000000000000000000001", 0, "MLTE_S1U_HTTP", "")); // 2001:db8:0:0:0:0:0:1
            System.out.println(adapter.handleBuffer(data, "invalidhex", 0, "MLTE_S1U_HTTP", "")); // invalidhex
            System.out.println(adapter.handleBuffer(data, " ", 0, "MLTE_S1U_HTTP", "")); // invalidhex
            System.out.println(ipv6ToHex("fe80::dc48:23ff:fe8d:9687"));
        }catch (Exception e){
            log.error("Exception occurred while running main()", e);
        }
    }
}