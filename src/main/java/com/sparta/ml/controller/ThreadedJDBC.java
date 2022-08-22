package com.sparta.ml.controller;

import com.sparta.ml.display.Display;
import com.sparta.ml.model.EmployeeDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class ThreadedJDBC {
    private static final Logger logger = Logger.getLogger("my logger");
    private static final ConsoleHandler consoleHandler = new ConsoleHandler();
    private static float start;
    private static float end;
    private final HashMap<String, EmployeeDTO> employees;
    private final Connection connection;
    static Connection postgresConn = ConnectionManager.connectToDB();
    static EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
    public static Map<String,EmployeeDTO> mainMap = employeeDAO.getEmployeesMap();
    static HashMap<String,EmployeeDTO> subMap = new HashMap<>();
    /*
    {
        logger.setLevel(Level.FINE);
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        consoleHandler.setLevel(Level.INFO);
    }

     */

    public ThreadedJDBC(HashMap<String, EmployeeDTO> employees) {
        this.employees = employees;
        this.connection = ConnectionManager.connectToDB();
    }

    public static void runThreads() throws InterruptedException {
        start = System.nanoTime();
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //employeeDAO.filterCSVtoHashMap("src/main/resources/EmployeeRecords.csv");               //Multithreading corrupted data
        employeeDAO.csvToHashMap("src/main/resources/EmployeeRecordsLarge.csv");            //Multithreading clean data

        int NoOfThreads = 4;
        int subMapSize = mainMap.size()/NoOfThreads;
        int remainderRecords = subMapSize % NoOfThreads;
        if (remainderRecords != 0){
            subMapSize += 1;
        }

        int subMapCounter = 0;
        int subsTotalSizeCounter = 0;
        for (Map.Entry<String, EmployeeDTO> entry : mainMap.entrySet()) {
            if (subMapCounter < subMapSize) {
                subMap.put(entry.getKey(), entry.getValue());
                subMapCounter++;
                subsTotalSizeCounter++;

                if (subMapCounter == subMapSize || subsTotalSizeCounter == mainMap.size()) {
                    subMapCounter = 0;
                    //System.out.println(subMap.size());

                    Thread thread = new Thread(() -> employeeDAO.convertMapToSQL(subMap));
                    thread.start();
                    thread.join();

                    subMap = new HashMap<>();
                }
            }
        }

        employeeDAO.retrieveRecordsFromSQL(3640);

        ConnectionManager.closeConnection();
        end = System.nanoTime();
        Display.enterSQLRecords();

        System.out.println("\nTime spent in sending unique and clean records to database in multiple threads is: " + (ThreadedJDBC.end - ThreadedJDBC.start)/1_000_000_000 + " seconds");
    }
}