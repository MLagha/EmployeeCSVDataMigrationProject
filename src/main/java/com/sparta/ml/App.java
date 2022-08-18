package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;
import java.sql.*;

//Iteration - Filtering
public class App {

    public static void main( String[] args ) {
        long start = System.nanoTime();

        EmployeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EmployeeDAO.employeeMapToSQL();
        double end = System.nanoTime();
        System.out.println("\nTime taken to persist to SQL table before implementing multiple threads: " + (end - start)/1_000_000_000 + " seconds");

        //EmployeeDAO.retrieveRecordsFromSQL();

        ConnectionManager.closeConnection();
    }
}