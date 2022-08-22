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
    public static final Map<String, EmployeeDTO> employeesMap = new HashMap<>();
    public static final Map<String, EmployeeDTO> dupeEmployeesMap = new HashMap<>();
    public static final Map<String, EmployeeDTO> corruptEmployeesMap = new HashMap<>();
    private final Connection postgresConn;
    private final Statement statement;
    public static double start;
    public static int NoOfDuplicateRecords;
    public static int NoOfCorruptedRecords;
    public static int NoOfUniqueCleanRecords;

    {
        logger.setLevel(Level.FINE);
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
        consoleHandler.setLevel(Level.INFO);
    }

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

    public void filterCSVtoHashMap(String filename) {
        consoleHandler.setLevel(Level.INFO);
        logger.setUseParentHandlers(true);
        logger.log(Level.FINE,"Method populateHashMap started " + filename+ " is passed to parameter");
        try {
            var fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logger.log(Level.FINER, "In while loop to read csv line. line is: " + line);
                String[] record = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(record);
                if (DataCorruptionChecker.isValid(record)) {
                    logger.log(Level.FINER, "In if statement to check if record is corrupt, isRecordCorrupt is: "
                            + DataCorruptionChecker.isValid(record));
                    if (!employeesMap.containsKey(record[0])) {
                        logger.log(Level.FINER, "In if statement to check if " + record[0] + " is in HashMap" +
                                ", containKey is: " + employeesMap.containsKey(record[0]));
                        employeesMap.put(record[0], employeeDTO);
                    } else {
                        logger.log(Level.FINER, "duplicate found, record is " + Arrays.toString(record));
                        dupeEmployeesMap.put(record[0], employeeDTO);
                    }
                } else {
                    corruptEmployeesMap.put(record[0], employeeDTO);
                }
            }
            logger.log(Level.FINE, "Reading csv while-loop ends");
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

    public static void writeToFile() throws IOException {
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

    public void createEmployeeTable() throws SQLException {
        String sqlTable = SQLQueries.DROP_TABLE;
        logger.log(Level.INFO, "Employee table dropped");
        statement.executeUpdate(sqlTable);
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

    public String retrieveRecordsFromSQL(int emp_ID) {
        logger.log(Level.FINE, "Retrieving employee id: " + emp_ID + " records from the database");
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String employee = "";
        try {
            preparedStatement = postgresConn.prepareStatement(SQLQueries.SELECT);
            preparedStatement.setInt(1, emp_ID);
            resultSet = preparedStatement.executeQuery();
            
            System.out.println("\nEmp ID, " + "Name Prefix, " + "First Name, " + "Middle Initial, " + "Last Name,  " + "Gender, " + "E Mail, " + "Date of Birth, " + "Date of Joining, " + "Salary");
            while (resultSet.next()) {
                employee = resultSet.getString(1) + " "
                + resultSet.getString(2) + " "
                + resultSet.getString(3) + " "
                + resultSet.getString(4) + " "
                + resultSet.getString(5) + " "
                + resultSet.getString(6) + " "
                + resultSet.getString(7) + " "
                + resultSet.getString(8) + " "
                + resultSet.getString(9) + " "
                + resultSet.getString(10);
            }
            resultSet.close();
            preparedStatement.close();
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }
}