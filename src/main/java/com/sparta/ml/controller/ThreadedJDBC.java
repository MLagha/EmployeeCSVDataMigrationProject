package com.sparta.ml.controller;//package com.sparta.ml;

import com.sparta.ml.display.Display;
import com.sparta.ml.model.EmployeeDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ThreadedJDBC implements Runnable {
    public static float start;
    public static float end;
    private final HashMap<String, EmployeeDTO> employees;
    private final Connection connection;
    public ThreadedJDBC(HashMap<String, EmployeeDTO> employees) {
        this.employees = employees;
        this.connection = ConnectionManager.connectToDB();
    }

    @Override
    public void run() {


    }

    static Connection postgresConn = ConnectionManager.connectToDB();
    static EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
    static Map<String,EmployeeDTO> hMap = employeeDAO.getEmployeesMap();
    static HashMap<String,EmployeeDTO> halfHMap1 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap2 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap3 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap4 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap5 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap6 = new HashMap<>();

    public static void splitHashMap() {
        Map<String, EmployeeDTO> newMap = new HashMap<>();




        //employeeDAO.populateHashMap("src/main/resources/EmployeeRecordsLarge.csv");
        int count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : hMap.entrySet()) {
            (count < (hMap.size() / 2) ? halfHMap1 : halfHMap2 ).put(entry.getKey(), entry.getValue());
            count++;
        }
        count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : halfHMap1.entrySet()) {
            (count < (halfHMap1.size() / 2) ? halfHMap3 : halfHMap4).put(entry.getKey(), entry.getValue());
            count++;
        }
        count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : halfHMap2.entrySet()) {
            (count < (halfHMap2.size() / 2) ? halfHMap5 : halfHMap6).put(entry.getKey(), entry.getValue());
            count++;
        }

    }

    public static void runThreads() throws InterruptedException {
        start = System.nanoTime();

        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //employeeDAO.populateHashMap("src/main/resources/EmployeeRecordsLarge.csv");     //For corrupted files
        employeeDAO.printLargeFileToDB("src/main/resources/EmployeeRecordsLarge.csv");        //For clean files
        splitHashMap();


        System.out.println(halfHMap3.size());
        System.out.println(halfHMap4.size());
        System.out.println(halfHMap5.size());
        System.out.println(halfHMap6.size());


        Thread thread1 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap3));
        Thread thread2 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap4));
        Thread thread3 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap5));
        Thread thread4 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap6));
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();


        end = System.nanoTime();
        Display.enterSQLRecords();

        //System.out.println("\nTime taken to persist to SQL table AFTER implementing multiple threads: " + (end - start)/1_000_000_000 + " seconds");
    }

}