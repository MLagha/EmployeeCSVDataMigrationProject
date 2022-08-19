package com.sparta.ml.start;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.controller.ThreadedJDBC;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        //Runner.start();
        ThreadedJDBC.runThreads();
        double end = System.nanoTime();
        System.out.println("REAL time " + (end- EmployeeDAO.start /1_000_000_000) + " seconds.");
    }
}//6.5647652E13
// 6.5663511E13
// 6.5685577E13