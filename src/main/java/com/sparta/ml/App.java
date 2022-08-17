
package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;

import java.sql.*;


//Iteration - Filtering
public class App {
    public static void main( String[] args ) {
        EmployeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        System.out.println(EmployeeDAO.getEmployeesMap().size());



        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        employeeDAO.createEmployee(12343,  "Mr", "fvsdca", "fevdsa", "fvsda", "M", "djew@spartaglobal.com", "9/21/1982", "9/21/2022", "102000");
        //employeeDAO.createEmployee(98765,  "Ms", "ijuhg", "nbvc", "lkjg", "F", "djew@spartaglobal.com", "9/21/1994", "8/15/2020", "180570");
        ConnectionManager.closeConnection();



    }
}
