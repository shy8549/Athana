package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description:
 * @className: SubFromBackUtil
 * @date:2024/1/25 10:47
 */
public class SubFromBackUtil extends DataFilterAdapter {

    private static final long serialVersionUID = -2684438370023427754L;
    private static int sub_start;
    private static int sub_end;
    private static final Logger log = Logger.getLogger(SubFromBackUtil.class);

    public static String processInput(String input) {
        if(input.length() >= sub_end){
            if (input.length() < 11) {
                return input;
            } else {
                int actualStart = input.length() - sub_start;
                int actualEnd = input.length() - sub_end;
                return input.substring(actualEnd, actualStart);
            }
        } else {
            return  input;
        }
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {
        if (todo != null && !todo.isEmpty()) {
//            log.error("service name : "+service+" extPars : "+extPars+" data is : "+todo);
            return processInput(todo);
        } else {
//            log.error("service name : "+service+" extPars : "+extPars+" data is : "+todo);
            return todo;
        }
    }

    @Override
    public void init(String extPars) {
        String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
        if (pars.length == 2) {
            sub_start = Integer.parseInt(pars[0]);
            sub_end = Integer.parseInt(pars[1]);
        } else {
            log.error("Wrong InPut parameter");
        }
    }

    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";
        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.SubFromBackUtil").newInstance();
            adapter.init("4,11");
//            String re = adapter.handleBuffer(data, "13689098765", 0, "MLTE_S1U_HTTP", "1,5");
            String re = adapter.handleBuffer(data, "111", 0, "MLTE_S1U_HTTP", "1,5");
            System.out.println(re);
        } catch (Exception e) {
            log.error("Exception occurred while running main()", e);
        }
    }
}
