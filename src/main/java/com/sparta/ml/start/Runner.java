package com.sparta.ml.start;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.display.Display;
import com.sparta.ml.exceptions.DatabaseMissingException;

import java.sql.Connection;
import java.sql.SQLException;

public class Runner {
    public static float start;
    public static float end;
    public static void start() {
        start = System.nanoTime();
        runSmallCSVfilterToSQL();
//        runLargeCSVtoSQL();
        end = System.nanoTime();
        System.out.println("Time " + (end - start) / 1_000_000_000);
    }
    private static void runLargeCSVtoSQL() {
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        employeeDAO.csvToHashMap("src/main/resources/EmployeeRecordsLarge.csv");
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employeeDAO.convertMapToSQL(employeeDAO.getEmployeesMap());
        ConnectionManager.closeConnection();
    }

    private static void runSmallCSVfilterToSQL() {
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        employeeDAO.filterCSVtoHashMap("src/main/resources/EmployeeRecords.csv");
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employeeDAO.convertMapToSQL(employeeDAO.getEmployeesMap());
        ConnectionManager.closeConnection();
    }
}