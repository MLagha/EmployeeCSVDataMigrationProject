package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;

//Iteration - Filtering
public class App {
    public static void main( String[] args ) {
        EmployeeDAO.PopulatedArray("src/main/resources/EmployeeRecords.csv");
        System.out.println(EmployeeDAO.getEmployees().size());
        System.out.println(EmployeeDAO.getEmployees().get(0).getLastName());
    }
}

