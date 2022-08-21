package com.sparta.ml.start;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.display.Display;
import com.sparta.ml.exceptions.DatabaseMissingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Runner {
    private static final Logger logger = Logger.getLogger("my logger");
    private static final ConsoleHandler consoleHandler = new ConsoleHandler();
    public static float start;
    public static float end;
    {
        logger.setLevel(Level.FINE);
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        consoleHandler.setLevel(Level.INFO);
    }
    public static void start() {
        start = System.nanoTime();
//        runSmallCSVfilterToSQL();
        runLargeCSVtoSQL();
        end = System.nanoTime();
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
        float LargeCSVEnd = System.nanoTime();
        logger.log(Level.INFO,"Single thread clean large csv to database time: "
                + (LargeCSVEnd - start) / 1_000_000_000 + " seconds");
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
        float smallCSVEnd = System.nanoTime();
        logger.log(Level.INFO,"Single thread filter and clean small csv to database time: "
                + (smallCSVEnd - start) / 1_000_000_000 + " seconds");
    }
}