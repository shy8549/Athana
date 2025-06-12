package com.nsn.bighead.glassfish.filterse.adapter.ipurl;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6AddressRange;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IpUtils {
    public static boolean checkIPv4IsInRangeByConvertingToInt (String inputIP, long startIPAddress, long endIPAddress)
            throws UnknownHostException, UnknownHostException {

        long inputIPAddress = ipToLongInt(InetAddress.getByName(inputIP));

        return (inputIPAddress >= startIPAddress && inputIPAddress <= endIPAddress);
    }

    static long ipToLongInt (InetAddress ipAddress) {
        long resultIP = 0;
        byte[] ipAddressOctets = ipAddress.getAddress();

        for (byte octet : ipAddressOctets) {
            resultIP <<= 8;
            resultIP |= octet & 0xFF;
        }
        return resultIP;
    }

    public static boolean checkIPv6IsInRangeByIPv6library (String inputIP, IPv6AddressRange range) {
        IPv6Address inputIPAddress = IPv6Address.fromString(inputIP);
        return range.contains(inputIPAddress);
    }
    public static String[] cidrToStartEndIp(String cidr) {
        String[] startEndIp = new String[2];
        try {
            String[] cidrParts = cidr.split("/");
            String ip = cidrParts[0];
            int prefixLength = Integer.parseInt(cidrParts[1]);

            byte[] ipBytes = InetAddress.getByName(ip).getAddress();

            int startIp = byteArrayToInt(ipBytes);
            int endIp = startIp | ((1 << (32 - prefixLength)) - 1);

            startEndIp[0] = intToIpAddress(startIp);
            startEndIp[1] = intToIpAddress(endIp);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return startEndIp;
    }

    public static String[] cidrToStartEndIp6(String cidr) {
            String[] parts = cidr.split("/");
            String networkAddress = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);

            try {
                InetAddress inetAddress = Inet6Address.getByName(networkAddress);
                BigInteger start = new BigInteger(inetAddress.getAddress());
                BigInteger end = start.add(BigInteger.valueOf(2).pow(128 - prefixLength).subtract(BigInteger.ONE));

                String startIp = InetAddress.getByAddress(start.toByteArray()).getHostAddress();
                String endIp = InetAddress.getByAddress(end.toByteArray()).getHostAddress();

                return new String[]{startIp, endIp};
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            return null;
        }

    private static int byteArrayToInt(byte[] bytes) {
        return (bytes[0] & 0xFF) << 24 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[2] & 0xFF) << 8 |
                (bytes[3] & 0xFF);
    }

    private static String intToIpAddress(int ip) {
        return ((ip >> 24) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                (ip & 0xFF);
    }

    public static BigInteger countIPAddresses(String cidr) throws UnknownHostException {
        String[] parts = cidr.split("/");
        int prefixLength = Integer.parseInt(parts[1]);
        return BigInteger.valueOf(2).pow(32 - prefixLength);
    }

    public static BigInteger countIPAddresses6(String cidr) throws UnknownHostException {
        String[] parts = cidr.split("/");
        InetAddress inetAddress = InetAddress.getByName(parts[0]);
        int prefixLength = Integer.parseInt(parts[1]);
        return BigInteger.valueOf(2).pow(inetAddress.getAddress().length * 8 - prefixLength);
    }

    public static List<String> generateIPList6(String ipNetwork) {
        List<String> ipList = new ArrayList<>();
        try {
            String[] parts = ipNetwork.split("/");
            String ipAddress = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);

            BigInteger networkAddress = new BigInteger(1, InetAddress.getByName(ipAddress).getAddress());
            BigInteger mask = BigInteger.valueOf(-1).shiftLeft(128 - prefixLength);
            BigInteger startAddress = networkAddress.and(mask);
            BigInteger endAddress = networkAddress.or(mask.not());

            byte[] startBytes = startAddress.toByteArray();
            byte[] endBytes = endAddress.toByteArray();
            for (int i = startBytes.length; i < 16; i++) {
                startBytes = prependZero(startBytes);
                endBytes = prependZero(endBytes);
            }

            InetAddress start = InetAddress.getByAddress(startBytes);
            InetAddress end = InetAddress.getByAddress(endBytes);

            InetAddress current = start;
            while (!current.equals(end)) {
                ipList.add(current.getHostAddress());
                byte[] address = current.getAddress();
                for (int i = address.length - 1; i >= 0; i--) {
                    if ((address[i] & 0xFF) == 0xFF) {
                        address[i] = 0;
                    } else {
                        address[i]++;
                        break;
                    }
                }
                current = InetAddress.getByAddress(address);
            }
            ipList.add(end.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    private static byte[] prependZero(byte[] bytes) {
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, result, 1, bytes.length);
        return result;
    }

    public static String[] generateIPList(String ipNetwork) throws UnknownHostException {
        String[] parts = ipNetwork.split("/");
        int prefixLength = Integer.parseInt(parts[1]);
        InetAddress startAddress = InetAddress.getByName(parts[0]);

        BigInteger numAddresses = BigInteger.valueOf(2).pow(32 - prefixLength);
        String[] ipList = new String[numAddresses.intValue()];
        for (int i = 0; i < ipList.length; i++) {
            ipList[i] = startAddress.getHostAddress();
            startAddress = getNextAddress(startAddress);
        }

        return ipList;
    }

    private static InetAddress getNextAddress(InetAddress address) throws UnknownHostException {
        byte[] bytes = address.getAddress();
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == (byte) 255) {
            bytes[i] = 0;
            i--;
        }
        if (i >= 0) {
            bytes[i]++;
        }
        return InetAddress.getByAddress(bytes);
    }

    static void ipv6() throws UnknownHostException {
        String cidr = "2409:8754:f111:0004::78ed:c6c0/124";
        cidr = "2409:8720:1800:0300::df70:9000/120";
        cidr = "2409:8c54:1020:1:ff::/80";
        cidr = "2409:8c38:c50:302::/64";
        cidr = "2409:8c38:80:158:59::/128";
        cidr = "2409:8754:f111:0004::78ed:c6c0/124";
        BigInteger bigInteger = countIPAddresses6(cidr);
        System.out.println(bigInteger);
        String[] startEnd = cidrToStartEndIp6(cidr);
        assert startEnd != null;
        System.out.println(startEnd[0]+ "," + startEnd[1]);

        BigInteger compareValue = BigInteger.valueOf(1024);
        System.out.println(compareValue.compareTo(bigInteger) >= 0);
        if(compareValue.compareTo(bigInteger) >= 0) {
            List<String> ipArrays = generateIPList6(cidr);
            System.out.println(ipArrays.size());
            for (String ip : ipArrays) {
                System.out.println(ip);
            }
        }
    }

    static void ipv4() throws UnknownHostException {
        String cidr = "183.214.164.252/30";
        String[] startEnd = cidrToStartEndIp(cidr);
        System.out.println(startEnd[0]+ "," + startEnd[1]);
        BigInteger bigInteger = countIPAddresses(cidr);
        System.out.println(bigInteger);
        BigInteger compareValue = BigInteger.valueOf(1024);
        System.out.println(compareValue.compareTo(bigInteger) >= 0);
        String[] ipArrays = generateIPList(cidr);
        System.out.println(ipArrays.length);
        for (String ip : ipArrays) {
            System.out.println(ip);
        }
    }
    public static void main(String[] args) throws UnknownHostException {
//        ipv4();
        ipv6();
    }
}