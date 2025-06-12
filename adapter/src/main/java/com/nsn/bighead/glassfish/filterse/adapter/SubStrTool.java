package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.commons.lang3.StringUtils;


/**
 * @author: create by suhy
 * @version: v1.0
 * @description: com.nsn.bighead.glassfish.filterse.adapter
 * @date:2021/8/17
 */
public class SubStrTool extends DataFilterAdapter {
    private static final long serialVersionUID = -4038199637215448599L;
    private int sub_start = -1;
    private int sub_end = -1;

    /**
     *
     * @param data  原始数据
     * @param todo  要操作的字段
     * @param columnX  要操作的字段所属的列号
     * @param service  业务名称
     * @param extPars   扩展参数
     * @return      返回操作后的值
     */
    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {
        String result ;
        if (todo != null && !todo.isEmpty()) {
            // 长度不够时返回原值
            if(todo.length()<sub_end){
                result = todo;
            }else {
                result = todo.substring(sub_start, sub_end);
            }

            return result;
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
        String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
        if (pars.length == 2) {
            sub_start = Integer.parseInt(pars[0]);
            sub_end = Integer.parseInt(pars[1]);
        }
    }

    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";
        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.SubStrTool").newInstance();
            adapter.init("2,19");
            String re = adapter.handleBuffer(data, "1234567890", 0, "MLTE_S1U_HTTP", "1,5");

            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
