package com.sparta.ml.controller;

import com.sparta.ml.model.DataCorruptionChecker;
import com.sparta.ml.model.EmployeeDTO;
import com.sparta.ml.controller.util.SQLQueries;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDAO {
    private static final Logger logger = Logger.getLogger("my logger");
    private static final ConsoleHandler consoleHandler = new ConsoleHandler();
    public final Map<String, EmployeeDTO> employeesMap = new HashMap<>();
    public final Map<String, EmployeeDTO> dupeEmployeesMap = new HashMap<>();
    public final Map<String, EmployeeDTO> corruptEmployeesMap = new HashMap<>();
    private final Connection postgresConn;
    private final Statement statement;
    public static double start;

    public EmployeeDAO(Connection postgresConn) {
        this.postgresConn = postgresConn;
        try {
            statement = postgresConn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String, EmployeeDTO> getEmployeesMap() {
        return employeesMap;
    }
    public void populateHashMap(String filename) {
        consoleHandler.setLevel(Level.INFO);
        logger.setUseParentHandlers(true);
        logger.log(Level.FINE,"Method populateHashMap started " + filename+ " is passed to parameter");
        try {
            var fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logger.log(Level.FINE, "In while loop to read csv line. line is: " + line);
                String[] record = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(record);
                if (DataCorruptionChecker.isValid(record)) {
                    logger.log(Level.FINE, "In if statement to check if record is corrupt, isRecordCorrupt is: "
                            + DataCorruptionChecker.isValid(record));
                    if (!employeesMap.containsKey(record[0])) {
                        logger.log(Level.FINE, "In if statement to check if " + record[0] + " is in HashMap" +
                                ", containKey is: " + employeesMap.containsKey(record[0]));
                        employeesMap.put(record[0], employeeDTO);
                    } else {
                        logger.log(Level.FINE, "duplicate found, record is " + Arrays.toString(record));
                        dupeEmployeesMap.put(record[0], employeeDTO);
                    }
                } else {
                    corruptEmployeesMap.put(record[0], employeeDTO);
                }
            }
            logger.log(Level.INFO, "Reading csv while-loop ends");
            logger.log(Level.INFO, dupeEmployeesMap.size() + " Duplicate records found");
            logger.log(Level.INFO, corruptEmployeesMap.size() + " Corrupted records found");
            logger.log(Level.INFO, employeesMap.size() + " Unique and clean records left");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void clearHashMap() {
        employeesMap.clear();
    }

    public void writeToFile() throws IOException {
        FileWriter fileWriter = new FileWriter("src/main/resources/DuplicateEntries.csv", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Map.Entry<String, EmployeeDTO> set: dupeEmployeesMap.entrySet()) {
            bufferedWriter.write(set.toString());
        }
        bufferedWriter.close();

        fileWriter = new FileWriter("src/main/resources/CorruptEntries.csv", true);
        bufferedWriter = new BufferedWriter(fileWriter);
        for (Map.Entry<String, EmployeeDTO> set: corruptEmployeesMap.entrySet()) {
            bufferedWriter.write(set.toString());
        }
        bufferedWriter.close();

        fileWriter = new FileWriter("src/main/resources/CleanEntries.csv",true);
        bufferedWriter = new BufferedWriter(fileWriter);
        for (Map.Entry<String, EmployeeDTO> set: employeesMap.entrySet()) {
            bufferedWriter.write(set.toString());
        }
        bufferedWriter.close();
    }


    public void csvToHashMap(String filename) {
        try {
            var fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            start = System.nanoTime();
            bufferedReader.readLine();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] record = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(record);
                employeesMap.put(record[0], employeeDTO);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertEmployeeRecordDb(int Emp_ID, String Name_Prefix, String First_Name, String Middle_Initial
            , String Last_Name, String Gender, String E_Mail, LocalDate Date_of_Birth, LocalDate Date_of_Joining
            , String Salary) {
        try {
            PreparedStatement preparedStatement = postgresConn.prepareStatement(SQLQueries.INSERT_INTO_DB);
            preparedStatement.setInt(1, Emp_ID);
            preparedStatement.setString(2, Name_Prefix);
            preparedStatement.setString(3, First_Name);
            preparedStatement.setString(4, Middle_Initial);
            preparedStatement.setString(5, Last_Name);
            preparedStatement.setString(6, Gender);
            preparedStatement.setString(7, E_Mail);
            preparedStatement.setDate(8, Date.valueOf(Date_of_Birth));
            preparedStatement.setDate(9, Date.valueOf(Date_of_Joining));
            preparedStatement.setString(10, Salary);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createEmployeeTable() throws SQLException {
        String sqlTable = SQLQueries.DROP_TABLE;
        statement.executeUpdate(sqlTable);
        logger.log(Level.INFO, "Employee table dropped");
        sqlTable = SQLQueries.CREATE_TABLE + " ( "
                + "Emp_ID INT NOT NULL, "
                + "Name_Prefix VARCHAR(255),"
                + "First_Name VARCHAR(255),"
                + "Middle_Initial VARCHAR(255),"
                + "Last_Name VARCHAR(255),"
                + "Gender VARCHAR(255),"
                + "E_Mail VARCHAR(255),"
                + "Date_of_Birth DATE,"
                + "Date_of_Joining DATE,"
                + "Salary VARCHAR(255))";
        statement.executeUpdate(sqlTable);
        logger.log(Level.INFO, "Employee table created");
    }

    public void convertMapToSQL(Map<String, EmployeeDTO> employees) {
        for (Map.Entry<String, EmployeeDTO> set: employees.entrySet()) {
            insertEmployeeRecordDb(Integer.parseInt(
                    set.getKey())
                    , set.getValue().getNamePrefix()
                    , set.getValue().getFirstName()
                    , set.getValue().getMiddleInitial()
                    , set.getValue().getLastName()
                    , set.getValue().getGender()
                    , set.getValue().getEmail()
                    , set.getValue().getDateOfBirth()
                    , set.getValue().getDateOfJoining()
                    , set.getValue().getSalary());
        }
    }

    public EmployeeDTO retrieveRecordsFromSQL(int emp_ID) {
        consoleHandler.setLevel(Level.ALL);
        logger.log(Level.INFO, "Retrieving clean individual records from the database");
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = postgresConn.prepareStatement(SQLQueries.SELECT);
            preparedStatement.setInt(1, emp_ID);
            resultSet = preparedStatement.executeQuery();
            String [] record = new String[10];
            while (resultSet.next()) {
                record[0] = resultSet.getString(1);
                record[1] = resultSet.getString(2);
                record[2] = resultSet.getString(3);
                record[3] = resultSet.getString(4);
                record[4] = resultSet.getString(5);
                record[5] = resultSet.getString(6);
                record[6] = resultSet.getString(7);
                record[7] = resultSet.getString(8);
                logger.log(Level.FINE, "Record[7] is: " + record[7]);
                record[8] = resultSet.getString(9);
                record[9] = resultSet.getString(10);
            }
            EmployeeDTO employeeDTO = new EmployeeDTO(record);
            resultSet.close();
            preparedStatement.close();
            return employeeDTO;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}