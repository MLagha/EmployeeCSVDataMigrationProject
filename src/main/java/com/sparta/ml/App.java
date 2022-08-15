package com.sparta.ml;

import com.sparta.ml.dto.EmployeeDAO;
import com.sparta.ml.dto.EmployeeDTO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class App {
    public static void main( String[] args ) {
        EmployeeDAO.PopulatedArray("src/main/resources/EmployeeRecords.csv");
        System.out.println(EmployeeDAO.getEmployees().size());
    }
}

























