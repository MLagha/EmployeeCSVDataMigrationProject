package com.sparta.ml.start;

import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.controller.ThreadedJDBC;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        Runner.start();
        Connection postgresConn;
        ThreadedJDBC threadedJDBC = new ThreadedJDBC(,postgresConn);
    }
}