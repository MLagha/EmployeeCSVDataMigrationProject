package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;
import com.sparta.ml.dto.EmployeeDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
//        long start = System.nanoTime();
//
//        Connection postgresConn = ConnectionManager.connectToDB();
//        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
//        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
//        try {
//            employeeDAO.createEmployeeTable();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        employeeDAO.employeeMapToSQL();
//        double end = System.nanoTime();
//        System.out.println("\nTime taken to persist to SQL table before implementing multiple threads: " + (end - start)/1_000_000_000 + " seconds");
//
//        //employeeDAO.retrieveRecordsFromSQL();
//
//        ConnectionManager.closeConnection();
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");

        Map<String,EmployeeDTO> hmap = employeeDAO.getEmployeesMap();
        HashMap<String,EmployeeDTO> halfhmap1=new HashMap<>();
        HashMap<String,EmployeeDTO> halfhmap2=new HashMap<>();
        int count=0;

        for(Map.Entry<String, EmployeeDTO> entry : hmap.entrySet()) {
            (count<(hmap.size()/2) ? halfhmap1:halfhmap2).put(entry.getKey(), entry.getValue());
            count++;
        }
        System.out.println(halfhmap1.size());
        System.out.println(halfhmap2.size());

    }
}