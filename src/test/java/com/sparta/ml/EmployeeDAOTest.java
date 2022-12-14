package com.sparta.ml;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.model.EmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeDAOTest {
    Connection postgresConn = ConnectionManager.connectToDB();
    EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
    private void largeCSVtoDB() {
        employeeDAO.clearHashMap();
        employeeDAO.csvToHashMap("src/main/resources/EmployeeRecordsLarge.csv");
        try {
            employeeDAO.createEmployeeTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        employeeDAO.convertMapToSQL(employeeDAO.employeesMap);
    }
    @Test
    @DisplayName("Test size of each hashmap after filtering")
    void testSizeOfEachHashmapAfterFiltering() {
        employeeDAO.clearHashMap();
        employeeDAO.filterCSVtoHashMap("src/main/resources/EmployeeRecordsLarge.csv");
        Assertions.assertEquals( 65499, employeeDAO.employeesMap.size());

        employeeDAO.clearHashMap();
        employeeDAO.filterCSVtoHashMap("src/main/resources/EmployeeRecords.csv");
        
        Assertions.assertEquals( 9881, employeeDAO.employeesMap.size());
        Assertions.assertEquals(57, employeeDAO.dupeEmployeesMap.size());
        Assertions.assertEquals(62,employeeDAO.corruptEmployeesMap.size());
    }

    @Test
    @DisplayName("Test that retrieved record is the same one as the on the db")
    void testThatRetrievedRecordIsTheSameOneAsTheOnTheDb() {
        largeCSVtoDB();
        String result = employeeDAO.retrieveRecordsFromSQL(831);
        String expected = "831 Ms. Jeannetta F Barbosa F jeannetta.barbosa@earthlink.net 1981-03-05 2004-02-26 139279";
        Assertions.assertEquals(expected, result);
    }



    @Test
    @DisplayName("Return string value of emp_id 1 record from database")
    void returnStringValueOfEmpId1RecordFromDatabase() {
        largeCSVtoDB();
        String result = employeeDAO.retrieveRecordsFromSQL(1);
        String expected = "1 Mrs. Lavon A Shufelt F lavon.shufelt@aol.com 1977-12-19 2000-07-23 184597";
        Assertions.assertEquals(expected, result);
    }
    @Test
    @DisplayName("Return string value of emp_id 65499 record from db")
    void returnStringValueOfEmpId65499RecordFromDb() {
        largeCSVtoDB();
        String result = employeeDAO.retrieveRecordsFromSQL(65499);
        String expected = "65499 Mrs. Tamekia Y Mccutcheon F tamekia.mccutcheon@hotmail.com 1982-12-22 2011-01-13 190296";
        Assertions.assertEquals(expected, result);
    }

}
