package com.app.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String MYSQL_Connector = "com.mysql.cj.jdbc.Driver";
    private static final String SSL = "useSSL=false";
    private static final String TIMEZONE = "serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://localhost:3306/kampus";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
             Class.forName(MYSQL_Connector);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Add connector jar to classpath.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL+ "?" + SSL + "&" + TIMEZONE, USER, PASS);
    }
}
