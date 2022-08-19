package com.sparta.ml.start;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.controller.ThreadedJDBC;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        Runner.start();
        ThreadedJDBC.runThreads();
        double end = System.nanoTime();
        System.out.println("REAL time " + (end- EmployeeDAO.start /1_000_000_000) + " seconds.");
    }
}