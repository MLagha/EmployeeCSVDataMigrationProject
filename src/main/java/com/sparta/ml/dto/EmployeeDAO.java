package com.sparta.ml.dto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//Data Access Object
//CRUD
public class EmployeeDAO {
    private static ArrayList<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();
    private static BufferedReader bufferedReader;

    public static ArrayList<EmployeeDTO> getEmployees() {
        return employees;
    }
    public static void PopulatedArray(String filename) {
            try {
            var fileReader = new FileReader("src/main/resources/EmployeeRecords.csv");
            var bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line = bufferedReader.readLine();

            while(line != null) {
                String[] records = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(records);
                employees.add(employeeDTO);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}