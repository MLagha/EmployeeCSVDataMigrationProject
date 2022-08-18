package com.sparta.ml.controller;//package com.sparta.ml;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.model.EmployeeDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ThreadedJDBC implements Runnable {
    private final HashMap<String, EmployeeDTO> employees;
    private final Connection connection;
    public ThreadedJDBC(HashMap<String, EmployeeDTO> employees) {
        this.employees = employees;
        this.connection = ConnectionManager.connectToDB();
    }

    @Override
    public void run() {
//        EmployeeDAO emp = new EmployeeDAO();
//        EmployeeDAO employeeDAO = new EmployeeDAO();
//        for (EmployeeDTO employee : employees) {
//            emp.insertEmployee(employee, connection);
//        }
    }
    static Connection postgresConn = ConnectionManager.connectToDB();
    static EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
    static Map<String,EmployeeDTO> hMap = employeeDAO.getEmployeesMap();
    static HashMap<String,EmployeeDTO> halfHMap1 = new HashMap<>();
    static HashMap<String,EmployeeDTO> halfHMap2 = new HashMap<>();
    public static void splitHashMap() {
        //employeeDAO.populateHashMap("src/main/resources/EmployeeRecordsLarge.csv");
        int count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : hMap.entrySet()) {
            (count < (hMap.size() / 2) ? halfHMap1 : halfHMap2).put(entry.getKey(), entry.getValue());
            count++;
        }
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        splitHashMap();
        System.out.println(halfHMap1.size());
        Thread thread = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap1));
        Thread thread2 = new Thread(() -> employeeDAO.convertMapToSQL(halfHMap2));
        thread.start();
        thread2.start();
        float end = System.nanoTime();
        System.out.println("\nTime taken to persist to SQL table before implementing multiple threads: " + (end - start)/1_000_000_000 + " seconds");
    }

}