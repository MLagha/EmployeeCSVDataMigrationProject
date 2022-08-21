package com.sparta.ml.start;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.display.Display;
import com.sparta.ml.exceptions.DatabaseMissingException;

import java.sql.Connection;
import java.sql.SQLException;

public class Runner {
    private static float start;
    private static float end;
    static Connection postgresConn = ConnectionManager.connectToDB();
    static EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);

    public static void start() {
        start = System.nanoTime();
        try{
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        employeeDAO.convertMapToSQL(employeeDAO.getEmployeesMap());

        //employeeDAO.retrieveRecordsFromSQL(3640);

        ConnectionManager.closeConnection();
        end = System.nanoTime();
        Display.enterSQLRecords();
        System.out.println("\nTime spent in sending unique and clean records to database in a single thread is: " + (Runner.end - Runner.start)/1_000_000_000 + " seconds");
    }
}