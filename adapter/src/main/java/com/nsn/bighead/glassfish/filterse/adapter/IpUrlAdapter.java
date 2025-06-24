package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.adapter.ipurl.IpUrlFilter;
import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IpUrlAdapter extends DataFilterAdapter {

    private static final Logger log = LogManager.getLogger(IpUrlAdapter.class);

    private IpUrlFilter ipUrlFilter;



    @Override
    public String handleBuffer(String[] data, String todo, int columnX,
                               String service, String extPars) {
        String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
        String ipv4 = data[Integer.parseInt(pars[3])];
        String ipv6 = data[Integer.parseInt(pars[4])];
        String url = data[Integer.parseInt(pars[5])];
        boolean result = ipUrlFilter.filter(ipv4, ipv6, url);
		return String.valueOf(result);
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX,
                               String service) {
        return handleBuffer(data, todo, columnX, service, ",MS,");
    }

    @Override
    public void init(String extPars) {
        String url = "jdbc:mysql://10.10.80.71:3306/test";
        String username = "root";
        String password = "Tongtech@2021";
        if(extPars!=null && extPars.trim().length()>2) {
            String[] pars = extPars.split(",");
                url = pars[0];
                username = pars[1];
                password = pars[2];
                log.info("update jdbc info");
        }
        log.info("db url:" + url + ", username: " + username);
        ipUrlFilter = new IpUrlFilter(url, username, password);
    }

    public static void main(String[] args) {
        String[] data = new String[3];
        data[0] = "255.184.243.130";
        data[1] = "2409:8028:3810:002a:0000:0000:0000:27ff";
        data[2] = "mmsc.montern11et.com";

        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.IpUrlAdapter").newInstance();
            String url = "jdbc:mysql://10.10.80.71:3306/test";
            String username = "root";
            String password = "Tongtech@2021";
            String jdbcInfo = String.format("%s,%s,%s", url, username, password);
            adapter.init(jdbcInfo);

            try {
                String re = adapter.handleBuffer(data, "1683682920000", 0, "MLTE_S1U_HTTP", "NOW,MS");
                System.out.println("result = " + re);
            } catch (Exception ex43) {
                System.out.println("ssss");
                log.error("Exception occurred while running main()", ex43);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Exception occurred while running main()", e);
        }


    }

}
