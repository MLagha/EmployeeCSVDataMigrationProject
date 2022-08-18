package com.sparta.ml.dao;

import com.sparta.ml.DataCorruptionChecker;
import com.sparta.ml.SQLQueries;
import com.sparta.ml.dto.EmployeeDTO;
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

//Data Access Object
//CRUD
public class EmployeeDAO {
    private static final Logger logger = Logger.getLogger("my logger");
    private static final ConsoleHandler consoleHandler = new ConsoleHandler();
    private final Map<String, EmployeeDTO> employeesMap = new HashMap<>();
    private final Connection postgresConn;
    private final Statement statement;
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
        logger.log(Level.FINE,"Method populateHashMap started " + filename+ " is passed to parameter");
        try {
            var fileReader = new FileReader("src/main/resources/EmployeeRecords.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line;
            int dupeCount = 0;
            int corruptCount = 0;
            int cleanCount = 0;
            while ((line = bufferedReader.readLine()) != null) {
                logger.log(Level.FINE, "In while loop to read csv line. line is: " + line);
                String[] record = line.split(",");
                EmployeeDTO employeeDTO = new EmployeeDTO(record);
                if (DataCorruptionChecker.isValid(record)) {
                    logger.log(Level.FINE, "In if statement to check if record is currupt, isRecordCorrupt is: "
                            + DataCorruptionChecker.isValid(record));
                    try {
                        if (!employeesMap.containsKey(record[0])) {
                            logger.log(Level.FINE, "In if statement to check if " + record[0] + " is in HashMap" +
                                    ", containKey is: " + employeesMap.containsKey(record[0]));
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
            logger.log(Level.INFO, dupeCount + " Duplicate records found");
            logger.log(Level.INFO, corruptCount + " Corrupted records found");
            logger.log(Level.INFO, cleanCount + " Unique and clean records left");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String fileName, EmployeeDTO employeeDTO) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
        bufferedWriter.write(employeeDTO.toString());
        bufferedWriter.close();
    }


    public void createEmployeeRecordDb(int Emp_ID, String Name_Prefix, String First_Name, String Middle_Initial
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

    public void employeeMapToSQL(Map<String, EmployeeDTO> employees) {
        for (Map.Entry<String, EmployeeDTO> set: employees.entrySet()) {
            createEmployeeRecordDb(Integer.parseInt(
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

    public void retrieveRecordsFromSQL() {
        logger.log(Level.INFO, "Retrieving clean individual records from the database");    //Prints table heading before logger!!!!
        try {
            ResultSet resultSet = statement.executeQuery(SQLQueries.SELECT_ALL);
            System.out.println("Emp ID, " + "Name Prefix, " + "First Name, " + "Middle Initial, " + "Last Name,  " + "Gender, " + "E Mail, " + "Date of Birth, " + "Date of Joining, " + "Salary");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1)
                        + " " + resultSet.getString(2)
                        + " " + resultSet.getString(3)
                        + " " + resultSet.getString(4)
                        + " " + resultSet.getString(5)
                        + " " + resultSet.getString(6)
                        + " " + resultSet.getString(7)
                        + " " + resultSet.getString(8)
                        + " " + resultSet.getString(9)
                        + " " + resultSet.getString(10));
            }
            logger.log(Level.INFO, "Finished retrieving clean individual records from the database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}