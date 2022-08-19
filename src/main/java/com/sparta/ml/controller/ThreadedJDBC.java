package com.sparta.ml.controller;//package com.sparta.ml;

import com.sparta.ml.display.Display;
import com.sparta.ml.model.EmployeeDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ThreadedJDBC {
    public static float start;
    public static float end;
    private final HashMap<String, EmployeeDTO> employees;
    private final Connection connection;
    public ThreadedJDBC(HashMap<String, EmployeeDTO> employees) {
        this.employees = employees;
        this.connection = ConnectionManager.connectToDB();
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
    static HashMap<String,EmployeeDTO> halfHMap7 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap8 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap9 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap10 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap11 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap12 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap13 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap14 = new HashMap<>();


    public static void splitHashMap() throws InterruptedException {

        int count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : hMap.entrySet()) {
            (count < (hMap.size() / 2) ? halfHMap1 : halfHMap2).put(entry.getKey(), entry.getValue());
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
        count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : halfHMap3.entrySet()) {
            (count < (halfHMap3.size() / 2) ? halfHMap7 : halfHMap8).put(entry.getKey(), entry.getValue());
            count++;
        }

    }
        public static void runThreads () throws InterruptedException {
            start = System.nanoTime();

            try {
                employeeDAO.createEmployeeTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //employeeDAO.populateHashMap("src/main/resources/EmployeeRecordsLarge.csv");     //For corrupted files
            employeeDAO.csvToHashMap("src/main/resources/EmployeeRecordsLarge.csv");        //For clean files
            splitHashMap();

            //System.out.println(halfHMap3.size());
            System.out.println(halfHMap4.size());
            System.out.println(halfHMap5.size());
            System.out.println(halfHMap6.size());
            System.out.println(halfHMap7.size());
            System.out.println(halfHMap8.size());


            //Thread thread1 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap3));
            Thread thread2 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap4));
            Thread thread3 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap5));
            Thread thread4 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap6));
            Thread thread5 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap7));
            Thread thread6 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap8));

            //thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
            thread5.start();
            thread6.start();

            //thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
            thread6.join();

            end = System.nanoTime();
            Display.enterSQLRecords();

            //System.out.println("\nTime taken to persist to SQL table AFTER implementing multiple threads: " + (end - start)/1_000_000_000 + " seconds");
        }
    }

