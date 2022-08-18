package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;
import java.sql.*;

public class App {

    public static void main( String[] args ) {
        long start = System.nanoTime();

        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employeeDAO.employeeMapToSQL();
        double end = System.nanoTime();
        System.out.println("\nTime taken to persist to SQL table before implementing multiple threads: " + (end - start)/1_000_000_000 + " seconds");

        //employeeDAO.retrieveRecordsFromSQL();

        ConnectionManager.closeConnection();
    }
}