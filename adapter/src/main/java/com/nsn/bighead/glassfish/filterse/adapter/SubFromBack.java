package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @author: create by suhy
 * @version: v1.0
 * @description:
 * @className: SubFromBack
 * @date:2024/1/4 9:00
 */
public class SubFromBack extends DataFilterAdapter {

    private static final long serialVersionUID = -1;
    private static final Logger log = LogManager.getLogger(SubFromBack.class);

    public static String getLast11Digits(String input) {
        // 检查输入是否为 null 或空
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 正则表达式，匹配符合条件的字符串
        String pattern = "^(\\+?86)?\\d{11}$";

        // 判断输入是否符合预定义的模式
        if (input.matches(pattern)) {
            // 移除所有非数字字符
            String numericInput = input.replaceAll("[^0-9]", "");

            // 如果长度大于11，则截取最后11位
            if (numericInput.length() > 11) {
                return numericInput.substring(numericInput.length() - 11);
            }
            // 如果长度等于11，直接返回处理后的字符串
            return numericInput;
        }

        // 如果不匹配，返回原始输入
        return input;
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {
        return getLast11Digits(todo);
    }

    @Override
    public void init(String extPars) {

    }

    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";

        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.SubFromBack").newInstance();
            adapter.init("");
//            String re = adapter.handleBuffer(data, "+8615265478901", 0, "MLTE_S1U_HTTP", "");
            String re = adapter.handleBuffer(data, "+8610086", 0, "MLTE_S1U_HTTP", "");
            System.out.println(re);
        }catch (Exception e){
            log.error("Exception occurred while running main()", e);
        }
    }
}
