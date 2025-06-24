package com.nsn.bighead.glassfish.filterse.adapter;

import com.nsn.bighead.glassfish.filterse.handler.DataFilterAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: create by suhy
 * @version: v1.0
 * @description: MySqlConnectTool udf中获取mysql的信息
 * @className: MySqlConnectTool
 * @date:2023/7/17 17:26
 */
public class MySqlConnectTool extends DataFilterAdapter {

    private static final Logger log = LogManager.getLogger(MySqlConnectTool.class);

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service, String extPars) {
        String result = "0";
        MySqlConnectTool mySqlConnectTool = new MySqlConnectTool();
        List<String> Mydata = mySqlConnectTool.MysqlGet(extPars);
//        for (List<Object> rowData : Mydata) {
//            log.info("读取的mysql数据为"+rowData);
//
//        }
//        log.info("data的数据为："+data);
//        for (String element : data){
//            log.info(element);
//        }
//        log.info("todo的数据为："+todo);
//        log.info("String service 的结果"+ service);
//        log.info("String extPars 的结果"+ extPars);

        if (todo != null && !todo.isEmpty()) {
//            for (List<Object> rowData : Mydata) {
////                log.info("读取的mysql数据为"+rowData);
//                if (todo.contains(rowData.toString())) {
////                    System.out.println(rowData.toString());
//                    System.out.println(todo);
//                    result = "1";
////                    return result;
//                } else {
////                    System.out.println("not match");
//                    System.out.println(rowData.toString());
//                    System.out.println("==");
//                    System.out.println(todo);
//                    result = "0";
////                    return result;
//                }
//                return result;
//            }
            if (Mydata.contains(todo)) {
                System.out.println(todo);
                result = "1";
            } else {
                result = "0";
            }
            return result;

        } else {
//            log.info("serviceName : " + service + " substr column "+columnX+" is 'null' ,please check data!!! ");
//            log.info("serviceName : " + service + " substr column is 'null'  : " + Arrays.toString(data));
            result = "0";
            return result;

        }
//        return result;

    }

    public List<String> MysqlGet(String userpasswd) {
        String jdbcUrl = "jdbc:mysql://10.10.80.71:3306/test";
        String username = "root";
        String password = "Tongtech@2021";
//        String table = "n3host";
        String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(userpasswd, ",");

        if(pars!=null && pars.length==3){
            jdbcUrl = pars[0];
            username = pars[1];
            password = pars[2];
//            table = pars[3];
            log.info("update jdbc info") ;
        } else {
            log.error("Wrong InPut parameter");
        }

        try {
            // 1. 注册 JDBC 驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立数据库连接
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // 3. 创建 SQL 语句并执行查询
            String sqlQuery = "SELECT hostname FROM n3host"; // 替换为您要读取的表名
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // 4. 使用 ArrayList 存储读取的数据
            List<String> rowData = new ArrayList<>();

            while (resultSet.next()) {
                // 获取当前行的所有列数据
//                List<String> rowData = new ArrayList<>();

                // 假设表中有两列，列名为 "column1" 和 "column2"，您可以根据实际表结构修改列名
                String column1Value = resultSet.getObject("hostname").toString();
//                Object column2Value = resultSet.getObject("url");

                // 将列数据添加到当前行的列表中
                rowData.add(column1Value);
//                rowData.add(column2Value);

                // 将当前行的列表添加到表数据列表中
//                tableData.add(rowData);
            }

            // 5. 关闭连接
            resultSet.close();
            statement.close();
            connection.close();



            // 现在 tableData 中存储了读取的数据，您可以根据需要进行进一步处理
            //                System.out.println(rowData);
            return rowData;
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Exception occurred while running main()", e);
            return null;
        }


    }

    @Override
    public String handleBuffer(String[] data, String todo, int columnX, String service) {
        return this.handleBuffer(data, todo, columnX, service, ",");
    }

    @Override
    public void init(String extPars) {
        String[] pars = StringUtils.splitByWholeSeparatorPreserveAllTokens(extPars, ",");
        String username = "root";
        String password = "Tongtech@2021";
        if(pars!=null && pars.length==2){
            username = pars[0];
            password = pars[1];
            log.info("update jdbc info") ;
        } else {
            log.error("Wrong InPut parameter");
        }
//        MySqlConnectTool mySqlConnectTool = new MySqlConnectTool();


    }

    public static void main(String[] args) {

//        MySqlConnectTool xx = new MySqlConnectTool();
//        xx.MysqlGet();

        String[] data = new String[2];
        data[0] = "0";
        data[1] = "1";
//        System.out.println("start test");
        try {
            DataFilterAdapter adapter = (DataFilterAdapter) Class.forName("com.nsn.bighead.glassfish.filterse.adapter.MySqlConnectTool").newInstance();
            adapter.init("jdbc:mysql://10.10.80.71:3306/test,root,Tongtech@2021");
            String re = adapter.handleBuffer(data, "[anydesk.com]", 0, "MLTE_S1U_HTTP", "1,5");

            System.out.println(re);
        } catch (Exception e) {
            log.error("Exception occurred while running main()", e);
        }

    }

}
