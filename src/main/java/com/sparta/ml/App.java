package com.sparta.ml;

import com.sparta.ml.dto.EmployeeDAO;

public class App {
    public static void main( String[] args ) {
        EmployeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        System.out.println(EmployeeDAO.getEmployeesMap().size());
    }
}

























