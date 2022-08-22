package com.sparta.ml.controller;

import com.sparta.ml.controller.util.PropertiesLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection postgresConn;
    public static Connection connectToDB() {
        String url = PropertiesLoader.getProperty("url");
        String userName = PropertiesLoader.getProperty("userName");
        String password = PropertiesLoader.getProperty("password");
        try {
            postgresConn = DriverManager.getConnection(url, userName, password);
            postgresConn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postgresConn;
    }

    public static void closeConnection() {
        try {
            postgresConn.commit();
            postgresConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
