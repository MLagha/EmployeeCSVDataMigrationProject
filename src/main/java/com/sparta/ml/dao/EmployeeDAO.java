package com.sparta.ml.dao;
import com.sparta.ml.DataCorruptionChecker;
import com.sparta.ml.dto.EmployeeDTO;
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
        logger.log(Level.FINE,"Method populateHashMap started " + filename+ " is passed to parameter");
        try {
            var fileReader = new FileReader("src/main/resources/EmployeeRecords.csv");
            bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line;
            int dupeCount = 0;
            int corruptCount = 0;
            int cleanCount = 0;
            while ((line = bufferedReader.readLine()) != null) {
                logger.log(Level.FINE, "In while loop to read csv line. line is: " + line);
                String[] record = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(record);
                if (DataCorruptionChecker.isRecordCorrupt(record)) {
                    logger.log(Level.FINE, "In if statement to check if record is currupt, isRecordCorrupt is: " + DataCorruptionChecker.isRecordCorrupt(record));
                    try {
                        if (!employeesMap.containsKey(record[0])) {
                            logger.log(Level.FINE, "In if statement to check if " + record[0] + " is in HashMap, containKey is: " + employeesMap.containsKey(record[0]));
                                    employeesMap.put(record[0], employeeDTO);
                            writeToFile("src/main/resources/CleanEntries.csv", employeeDTO);
                            cleanCount++;
                        } else {
                            logger.log(Level.FINE, "duplicate found, record is " + Arrays.toString(record));
                            writeToFile("src/main/resources/DuplicateEntries.csv", employeeDTO);
                            dupeCount++;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    writeToFile("src/main/resources/CorruptEntries.csv", employeeDTO);
                    corruptCount++;
                }
            }
            logger.log(Level.INFO, "Reading csv while-loop ends");
            logger.log(Level.INFO, dupeCount + " duplicates found");
            logger.log(Level.INFO, corruptCount + " Corrupted found");
            logger.log(Level.INFO, cleanCount + " clean left");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(String fileName, EmployeeDTO employeeDTO) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
        bufferedWriter.write(employeeDTO.toString());
        bufferedWriter.close();
    }
}