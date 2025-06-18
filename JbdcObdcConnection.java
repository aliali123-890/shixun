package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JbdcObdcConnection {
    // 修改为您的数据库配置
    private static final String URL = "jdbc:mysql://localhost:3306/yifuku?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // 数据库用户名
    private static final String PASSWORD = "123456"; // 数据库密码

    public static Connection getConnection() {
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立连接
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("数据库连接成功！");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("找不到MySQL驱动: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("数据库连接错误:");
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet executeQuery(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    public void executeUpdate(String a) {
        // TODO Auto-generated method stub

    }
}