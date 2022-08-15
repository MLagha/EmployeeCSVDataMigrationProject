package com.sparta.ml.dao;

import com.sparta.ml.dto.EmployeeDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//Data Access Object - CRUD

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
            bufferedReader.readLine();                                  //Avoiding the heading line

            // Consider replacing the condition with a condition of the total number of lines. Faster?
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()){
                String[] csvInput = line.split(",");              //splitting the line and putting it into a primitive array
                EmployeeDTO employeeDTO = new EmployeeDTO(csvInput);    //Creating an employee DTO and fill it up with the content of the array
                employees.add(employeeDTO);                             //Adding the records (csvInputs) into the array
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}