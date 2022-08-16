package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;
import com.sparta.ml.dto.EmployeeDTO;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;

public class App {
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();


    public static void main( String[] args ) {
        EmployeeDAO.PopulatedArray("src/main/resources/EmployeeRecords.csv");
        System.out.println(EmployeeDAO.getEmployees().size());

        HashMap<String, EmployeeDTO> employeeRecords = new HashMap<>();
        String line;
        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/EmployeeRecords.csv"));
            bufferedReader.readLine();
            while((line =  bufferedReader.readLine()) != null) {
                populate(line);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void populate(String line) throws ParseException {
        String[] record = line.trim().split(",");
        EmployeeDTO employeeDTO = new EmployeeDTO(record);
        if(!DataCorruptionChecker.isRecordCorrupt(record)) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/CorruptEntries.csv", true));
                bufferedWriter.write(employeeDTO.toString());
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}