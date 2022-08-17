package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;
import java.sql.*;

public class App {

    public static void main( String[] args ) {
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        employeeDAO.employeeMapToSQL();
        ConnectionManager.closeConnection();
    }
}