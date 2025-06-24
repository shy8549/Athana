package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StringConcat extends DataFilterAdapter {

    private static final long serialVersionUID = -1;
    private static final Logger log = LogManager.getLogger(StringConcat.class);


    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {

        if (todo != null && !todo.isEmpty()) {
            String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
            String appendStr = null;
            boolean appendAtFront = false;
            int lengthCondition = -1;
            if (pars.length == 3) {
                appendStr = pars[0];
                appendAtFront = Boolean.parseBoolean(pars[1]);
                lengthCondition = Integer.parseInt(pars[2]);
            }
            return processString(todo, appendStr, appendAtFront, lengthCondition);
        } else {
            return todo;
        }
    }

    /**
     * 根据条件对目标字符串进行拼接操作
     *
     * @param todo            目标字符串
     * @param appendStr       要拼接的字符串（如 "+86"）
     * @param appendAtFront   是否拼接到前面（true=前面，false=后面）
     * @param lengthCondition 长度判断条件（-1 表示不判断，其它正整数表示匹配该长度才拼接）
     * @return 处理后的字符串
     */
    public static String processString(String todo, String appendStr, boolean appendAtFront, int lengthCondition) {
        if (todo == null) {
            return null;  // 可选处理：或返回 ""
        }

        if (lengthCondition == -1 || todo.length() == lengthCondition) {
            return appendAtFront ? appendStr + todo : todo + appendStr;
        }

        return todo;
    }

    @Override
    public void init(String extPars) {
/*        String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
        if (pars.length == 3) {
            String appendStr = pars[0];
            boolean appendAtFront = Boolean.parseBoolean(pars[1]);
            Integer lengthCondition = Integer.parseInt(pars[2]);
        } else {
            log.error("Wrong InPut parameter");
        }*/
    }

    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";

        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.StringConcat").newInstance();
            adapter.init("+86,true,11");
            String re = adapter.handleBuffer(data, "10086", 0, "MLTE_S1U_HTTP", "+86,false,-1");
            System.out.println(re);
        }catch (Exception e){
            log.error("Exception occurred while running main()", e);
        }
    }

}
