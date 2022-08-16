package com.sparta.ml.dao;

import com.sparta.ml.DataCorruptionChecker;
import com.sparta.ml.dto.EmployeeDTO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

//Data Access Object
//CRUD
public class EmployeeDAO {
    private static final Logger logger = Logger.getLogger("my logger");
    private static ConsoleHandler consoleHandler = new ConsoleHandler();
    private static Map<String, EmployeeDTO> employeesMap = new HashMap<>();
    private static BufferedReader bufferedReader;

    public static Map<String, EmployeeDTO> getEmployeesMap() {
        return employeesMap;
    }

    public static void populateHashMap(String filename) {
        consoleHandler.setLevel(Level.INFO);
        try {
            var fileReader = new FileReader("src/main/resources/EmployeeRecords.csv");
            bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] record = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(record);
                //if no data corruption
                if (DataCorruptionChecker.isRecordCorrupt(record)) {
                    try {
                        if (!employeesMap.containsKey(record[0])) {
                            employeesMap.put(record[0], employeeDTO);
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/CleanEntries.csv", true));
                            bufferedWriter.write(employeeDTO.toString());
                            bufferedWriter.close();
                        } else {
                            logger.log(Level.FINE, "employeesMap.containsKey(record[0]) duplicate found, record is " + Arrays.toString(record));
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/DuplicateEntries.csv", true));
                            bufferedWriter.write(employeeDTO.toString());
                            bufferedWriter.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/CorruptEntries.csv", true));
                    bufferedWriter.write(employeeDTO.toString());
                    bufferedWriter.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}