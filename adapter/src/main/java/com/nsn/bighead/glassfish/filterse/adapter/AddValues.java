package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author: create by suhy
 * @version: v1.0
 * @description:
 * @className: AddValues
 * @date:2023/10/27 15:18
 */
public class AddValues extends DataFilterAdapter {
    private static final Logger log = LogManager.getLogger(AddValues.class);

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars){

//        return  insertValue(data,int_arr,newValue);
        return null;
    }

    @Override
    public void init(String extPars){
        String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
        if (pars.length == 2) {
            int int_arr = Integer.parseInt(pars[0]);
            String newValue = pars[1];
        } else {
            log.error("Wrong InPut parameter");
        }
    }

    public static String[] insertValue(String[] data, int i, String newValue) {
        if (i < 0 || i >= data.length - 1) {
            // 检查索引 i 的有效性
            throw new IllegalArgumentException("Invalid index i");
        }

        // 创建一个新的字符串数组，长度比原数组大 1
        String[] newData = new String[data.length + 1];

        // 将原数组中索引 i 之前的元素复制到新数组
        System.arraycopy(data, 0, newData, 0, i + 1);

        // 将 newValue 插入到新数组中
        newData[i + 1] = newValue;

        // 将原数组中索引 i 之后的元素复制到新数组
        if (data.length - (i + 1) >= 0) System.arraycopy(data, i + 1, newData, i + 1 + 1, data.length - (i + 1));

        return newData;
    }

    public static void main(String[] args) {

    }

}
