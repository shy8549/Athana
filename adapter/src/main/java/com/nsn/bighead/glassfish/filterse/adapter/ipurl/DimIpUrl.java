package com.nsn.bighead.glassfish.filterse.adapter.ipurl;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6AddressRange;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

public class DimIpUrl {

    public static void main(String[] args) {
        String url = "jdbc:mysql://10.10.80.71:3306/test";
        String username = "root";
        String password = "Tongtech@2021";
        DimIpUrl dimIpUrl = new DimIpUrl();
        IpUrlModel dataFromDatabase = dimIpUrl.getDataFromDatabase(url, username, password);
        System.out.println("dataFromDatabase: " + dataFromDatabase);
    }

    public IpUrlModel getDataFromDatabase(String url, String username, String password) {
        IpUrlModel dimStaticData = new IpUrlModel();
        Set<String> ipv4Set = new HashSet<>();
        Set<String> ipv6Set = new HashSet<>();
        Map<Long,Long> startEndIpv4Map = new HashMap<>();
        Set<IPv6AddressRange> rangeIpv6Set = new HashSet<>();
        List<String> urlList = new ArrayList<>();
        dimStaticData.setIpv4Set(ipv4Set);
        dimStaticData.setIpv6Set(ipv6Set);
        dimStaticData.setStartEndIpv4Map(startEndIpv4Map);
        dimStaticData.setRangeIpv6Set(rangeIpv6Set);
        dimStaticData.setUrlList(urlList);
        List<String> appServerIpList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            String query = "SELECT app_server_ip,url FROM ipurl";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String appServerIp = resultSet.getString("app_server_ip");

                if (appServerIp != null && appServerIp.trim().length() > 0) {
                    appServerIpList.add(appServerIp);
                } else {
                    String urlStr = resultSet.getString("url");
                    if(urlStr != null && urlStr.trim().length() > 0) {
                        urlList.add(urlStr);
                    }
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("appServerIpList = " + appServerIpList.size() + ", urlSize = " + urlList.size());
        for(String appServerIp : appServerIpList) {
            if(appServerIp.contains(".")) {
                if(appServerIp.contains("/")) {
                    BigInteger bigInteger = null;
                    try {
                        bigInteger = IpUtils.countIPAddresses(appServerIp);
                        BigInteger compareValue = BigInteger.valueOf(512);
                        if(compareValue.compareTo(bigInteger) >= 0) {
                            String[] ipList = IpUtils.generateIPList(appServerIp);
                            for(String ip : ipList) {
                                ipv4Set.add(ip);
                            }
                        } else {
                            String[] startEndIp = IpUtils.cidrToStartEndIp(appServerIp);
                            try {
                                startEndIpv4Map.put(IpUtils.ipToLongInt(InetAddress.getByName(startEndIp[0])), IpUtils.ipToLongInt(InetAddress.getByName(startEndIp[1])));
                            } catch (UnknownHostException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    ipv4Set.add(appServerIp);
                }
            } else {
                if(appServerIp.contains("/")) {
                    BigInteger bigInteger = null;
                    try {
                        bigInteger = IpUtils.countIPAddresses6(appServerIp);
                        BigInteger compareValue = BigInteger.valueOf(1024);
                        if(compareValue.compareTo(bigInteger) >= 0) {
                            List<String> ipList = IpUtils.generateIPList6(appServerIp);
                            for(String ip : ipList) {
                                ipv6Set.add(ip);
                            }
                        } else {
                            String[] startEndIp = IpUtils.cidrToStartEndIp6(appServerIp);
                            IPv6Address startIPAddress1 = IPv6Address.fromString(startEndIp[0]);
                            IPv6Address endIPAddress1 = IPv6Address.fromString(startEndIp[1]);
                            IPv6AddressRange ipRange = IPv6AddressRange.fromFirstAndLast(startIPAddress1, endIPAddress1);
                            rangeIpv6Set.add(ipRange);
                        }
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    ipv6Set.add(appServerIp);
                }
            }
        }
        return  dimStaticData;
    }

}
