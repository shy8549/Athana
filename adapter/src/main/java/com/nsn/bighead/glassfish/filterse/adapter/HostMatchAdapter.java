package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description:
 * @className: HostMatchAdapter
 * @date:2023/8/18 10:35
 */
public class HostMatchAdapter extends DataFilterAdapter {

    private static final long serialVersionUID = -1597993181554988888L;
    private static final Logger log = LogManager.getLogger(HostMatchAdapter.class);
    List<String> rowData = new ArrayList<>();
    List<String> rowDataGet = new ArrayList<>();
    String result = "";

    @Override
    public String handleBuffer(String[] data, String todo, int columnX,
                               String service, String extPars) {

        boolean contains = false;

        for (String item: rowDataGet){
            if (todo.contains(item)) {
                contains = true;
                break;
            }
        }
        if (contains) {
            result = "1";
        }else {
            result = "0";
        }

       /* // 输入值完全匹配mysql中的某字段的值
        if (rowData.contains(todo.trim())) {
            result = "1";
        } else {
            result = "0";
        }*/

        return result;
    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX,
                               String service) {
        return handleBuffer(data, todo, columnX, service, ",MS,");
    }

    @Override
    public void init(String extPars) {

        String urli = "jdbc:mysql://192.168.1.65:3306/test";
        String usernamei = "tongdata";
        String passwordi = "cdphadoop@2021";

        if(extPars!=null && extPars.trim().length()>2) {
            String[] pars = extPars.split(",");
            urli = pars[0];
            usernamei = pars[1];
            passwordi= pars[2];
            log.info("update jdbc info");
        }
        log.info("db url:" + urli + ", username: " + usernamei);
        rowDataGet = HostNameFilter(urli, usernamei, passwordi);
        if (rowData.isEmpty()){
            log.info("get mysql data from n3host table is null !!!!");
        } else {
            log.info("get mysql data from n3host table is finished !!!!");
        }
    }

    public List<String> HostNameFilter(String url, String username, String password) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sqlQuery = "SELECT hostname FROM n3host"; // 替换为您要读取的表名
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                String column1Value = resultSet.getObject("hostname").toString();
                rowData.add(column1Value.trim());
            }
            resultSet.close();
            statement.close();
            connection.close();
            return rowData;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";
        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.HostMatchAdapter").newInstance();
            adapter.init("jdbc:mysql://10.10.80.72:3306/test,root,tongtech@2021");
            String re = adapter.handleBuffer(data, "anydesk.com", 0, "MLTE_S1U_HTTP", "1,5");
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
