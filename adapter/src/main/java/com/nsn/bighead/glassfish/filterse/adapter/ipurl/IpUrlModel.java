package com.nsn.bighead.glassfish.filterse.adapter.ipurl;

import com.googlecode.ipv6.IPv6AddressRange;

import java.util.*;

public class IpUrlModel {
    private Set<String> ipv4Set = new HashSet<>();
    private Set<String> ipv6Set = new HashSet<>();

    private Map<Long,Long> startEndIpv4Map = new HashMap<>();
    private Set<IPv6AddressRange> rangeIpv6Set = new HashSet<>();

    private List<String> urlList = new ArrayList<>();

    public Set<String> getIpv4Set() {
        return ipv4Set;
    }

    public void setIpv4Set(Set<String> ipv4Set) {
        this.ipv4Set = ipv4Set;
    }

    public Set<String> getIpv6Set() {
        return ipv6Set;
    }

    public void setIpv6Set(Set<String> ipv6Set) {
        this.ipv6Set = ipv6Set;
    }

    public Map<Long, Long> getStartEndIpv4Map() {
        return startEndIpv4Map;
    }

    public void setStartEndIpv4Map(Map<Long, Long> startEndIpv4Map) {
        this.startEndIpv4Map = startEndIpv4Map;
    }

    public Set<IPv6AddressRange> getRangeIpv6Set() {
        return rangeIpv6Set;
    }

    public void setRangeIpv6Set(Set<IPv6AddressRange> rangeIpv6Set) {
        this.rangeIpv6Set = rangeIpv6Set;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public String toString() {
        return "IpUrlModel{" +
                "ipv4Set=" + ipv4Set.size() +
                ", ipv6Set=" + ipv6Set.size() +
                ", startEndIpv4Map=" + startEndIpv4Map.size() +
                ", rangeIpv6Set=" + rangeIpv6Set.size() +
                ", urlList=" + urlList.size() +
                '}';
    }
}
