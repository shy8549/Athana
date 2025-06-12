package com.nsn.bighead.glassfish.filterse.adapter.ipurl;

import com.googlecode.ipv6.IPv6AddressRange;

import java.net.UnknownHostException;
import java.util.Map;

public class IpUrlFilter {
    private final IpUrlModel dataFromDatabase;

    public IpUrlFilter(String url, String userName, String password) {
        DimIpUrl dimIpUrl = new DimIpUrl();
        dataFromDatabase = dimIpUrl.getDataFromDatabase(url, userName, password);
    }
    public static void main(String[] args) {
        String url = "jdbc:mysql://10.10.80.71:3306/test";
        String username = "root";
        String password = "Tongtech@2021";

//        DimIpUrl dimIpUrl = new DimIpUrl();
//        long st = System.currentTimeMillis();
//        DimStaticData dataFromDatabase = dimIpUrl.getDataFromDatabase(url, username, password);
//        System.out.println(dataFromDatabase + ", cost = " + (System.currentTimeMillis() - st));
        IpUrlFilter ipUrlFilter = new IpUrlFilter(url, username, password);

        long st = System.currentTimeMillis();
        boolean result = false;
        String ipv6 = "2409:8028:3810:002a:0000:0000:0000:27ff"; // 输入要判断的IPv6地址
        String ipv4 = "255.184.243.130";
        String uri = "mmsc.montern11et.com";
        for(int i=0; i<10000; i++) {
            result = ipUrlFilter.filter(ipv4, ipv6, uri);
        }
        System.out.println("cost = " + (System.currentTimeMillis()-st) + ", result = " + result);
        st = System.currentTimeMillis();
        for(int i=0; i<10000; i++) {
            result = ipUrlFilter.filter(ipv4, ipv6, uri);
        }
        System.out.println("cost = " + (System.currentTimeMillis()-st) + ", result = " + result);
    }

    public boolean filter(String ipv4, String ipv6, String uri) {
        boolean result = false;

        if(ipv4 != null && ipv4.trim().length() > 0) {
            result = filterByIpv4(ipv4, dataFromDatabase);
        }
        if(!result && ipv6 != null && ipv6.trim().length() > 0) {
            result = filterByIpv6(ipv6, dataFromDatabase);
        }
        if(!result && uri != null && uri.trim().length() > 0) {
            result = filterByUrl(uri, dataFromDatabase);
        }
        return result;
    }

    private static boolean filterByUrl(String uri, IpUrlModel dataFromDatabase) {
        for(String url : dataFromDatabase.getUrlList()) {
            if(uri.contains(url)) {
                return true;
            }
        }
        return false;
    }

    private static boolean filterByIpv6(String ip, IpUrlModel dataFromDatabase) {
        boolean result = dataFromDatabase.getIpv6Set().contains(ip);
        if(result) {
            return true;
        }
        for(IPv6AddressRange range : dataFromDatabase.getRangeIpv6Set()) {
            result = IpUtils.checkIPv6IsInRangeByIPv6library(ip, range);
            if(result) {
                return true;
            }
        }
        return result;
    }

    private static boolean filterByIpv4(String ip, IpUrlModel dataFromDatabase) {
        boolean result = dataFromDatabase.getIpv4Set().contains(ip);
        if(result) {
            return true;
        }
        for(Map.Entry<Long,Long> startEnd : dataFromDatabase.getStartEndIpv4Map().entrySet()) {
            try {
                result = IpUtils.checkIPv4IsInRangeByConvertingToInt(ip, startEnd.getKey().longValue(), startEnd.getValue().longValue());
                if(result) {
                    return true;
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return false;
            }
        }
        return result;
    }
}
