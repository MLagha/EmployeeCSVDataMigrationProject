package com.sparta.ml.dao;

import com.sparta.ml.ConnectionManager;
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
public class EmployeeDAO implements Runnable{
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
/*
                            Connection postgresConn = ConnectionManager.connectToDB();
                            EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
                            employeeDAO.createEmployee(Integer.parseInt(record[0]),  record[1], record[2], record[3], record[4], record[5], record[6], record[7], record[8], record[9]);
                            ConnectionManager.closeConnection();
 */
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


    //////////////////


    private static Connection postgresConn;        //final?

    private Statement statement;
    public EmployeeDAO(Connection postgresConn) {
        this.postgresConn = postgresConn;
        try {
            statement = postgresConn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public static void createEmployeeRecordDb(int empID, String namePrefix, String firstName, String middleInitial, String lastName, String gender, String email, LocalDate dateOfBirth, LocalDate dateOfJoining, String salary) {
        try {
                PreparedStatement preparedStatement = postgresConn.prepareStatement(SQLQueries.INSERT_INTO_DB);
                preparedStatement.setInt(1, empID);
                preparedStatement.setString(2, namePrefix);
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, middleInitial);
                preparedStatement.setString(5, lastName);
                preparedStatement.setString(6, gender);
                preparedStatement.setString(7, email);
                preparedStatement.setDate(8, Date.valueOf(dateOfBirth));
                preparedStatement.setDate(9, Date.valueOf(dateOfJoining));
                preparedStatement.setString(10, salary);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createEmployeeTable() throws SQLException {
        String sqlTable = null;

        //Drop employee table if exists then create a new one
        sqlTable = SQLQueries.DROP_TABLE;
        statement.executeUpdate(sqlTable);
        System.out.println("Employee table dropped");

        sqlTable = SQLQueries.CREATE_TABLE + " ( "
                + "empID INT NOT NULL, "
                + "namePrefix VARCHAR(255),"
                + "firstName VARCHAR(255),"
                + "middleInitial VARCHAR(255),"
                + "lastName VARCHAR(255),"
                + "gender VARCHAR(255),"
                + "email VARCHAR(255),"
                + "dateOfBirth VARCHAR(255),"
                + "dateOfJoining VARCHAR(255),"
                + "salary VARCHAR(255))";
        statement.executeUpdate(sqlTable);
        System.out.println("Employee table created");
    }

    public static void employeeMapToSQL() {
        for (Map.Entry<String, EmployeeDTO> set: employeesMap.entrySet()) {
            createEmployeeRecordDb(Integer.parseInt(set.getKey()),  set.getValue().getNamePrefix(), set.getValue().getFirstName(), set.getValue().getMiddleInitial(), set.getValue().getLastName(), set.getValue().getGender(), set.getValue().getEmail(), set.getValue().getDateOfBirth(), set.getValue().getDateOfJoining(), set.getValue().getSalary());

        }

    }

    @Override
    public void run() {

    }

//    public void ThreadedJDBC(Map<String, EmployeeDTO> employeesMap) {
//
//        postgresConn = ConnectionManager.connectToDB();
//    }
//    @Override
//    public void run() {
//        EmployeeDAO emp = new EmployeeDAO();
//        for (EmployeeDTO employee : employees) {
//            emp.insertEmployee(employee, postgresConn);
//        }
//    }

}