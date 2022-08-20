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

        try{
            start = System.nanoTime();
            Connection postgresConn = ConnectionManager.connectToDB();
            EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
            employeeDAO.filterCSVtoHashMap("src/main/resources/EmployeeRecordsLarge.csv");
            try {
                employeeDAO.createEmployeeTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            employeeDAO.convertMapToSQL(employeeDAO.getEmployeesMap());
            end = System.nanoTime();
            System.out.println(employeeDAO.retrieveRecordsFromSQL(831));
            Display.enterSQLRecords();
            ConnectionManager.closeConnection();
        } catch (DatabaseMissingException e) {
            throw new RuntimeException(e);
        }

    }
}