package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;
import java.sql.*;

//Iteration - Filtering
public class App {

    public static void main( String[] args ) {
        EmployeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EmployeeDAO.employeeMapToSQL();

        EmployeeDAO.retrieveRecordsFromSQL();

        ConnectionManager.closeConnection();
    }
}