package com.sparta.ml.dao;

import com.sparta.ml.DataCorruptionChecker;
import com.sparta.ml.dao.EmployeeDTO;

import java.io.*;
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
        logger.log(Level.INFO, "populateHashMap method started, filename is " + filename);
        try {
            var fileReader = new FileReader("src/main/resources/EmployeeRecords.csv");
            bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while (line != null) {
                logger.log(Level.FINE, "While loop for reading csv line started");
                String[] record = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(record);
                //if no data corruption
                try {
                    if (!DataCorruptionChecker.isRecordCorrupt(record)) {
                        if (!employeesMap.containsKey(record[0])) {
                            employeesMap.put(record[0], employeeDTO);
                            logger.log(Level.FINE, "!employeesMap.containsKey(record[0]), " + Arrays.toString(record) + " added");
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/CleanEntries.csv", true));
                            bufferedWriter.write(employeeDTO.toString());
                            bufferedWriter.close();
                        } else {
                            logger.log(Level.FINE, "employeesMap.containsKey(record[0]) duplicate found, record is " + Arrays.toString(record));
                            //put record in duplicate list
                        }
                        line = bufferedReader.readLine();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}