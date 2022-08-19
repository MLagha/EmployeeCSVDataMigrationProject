package com.sparta.ml;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.model.EmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;

public class EmployeeDAOTest {
    Connection postgresConn = ConnectionManager.connectToDB();
    EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);

    @Test
    @DisplayName("Test size of each hashmap after")
    void testSizeOfEachHashmap() {
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecordsLarge.csv");
        Assertions.assertEquals( 65499, employeeDAO.employeesMap.size());

        employeeDAO.clearHashMap();
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        
        Assertions.assertEquals( 9881, employeeDAO.employeesMap.size());
        Assertions.assertEquals(57, employeeDAO.dupeEmployeesMap.size());
        Assertions.assertEquals(62,employeeDAO.corruptEmployeesMap.size());
    }

    @Test
    @DisplayName("Test that retrieved record method is the same one as the one the db")
    void testThatRetrievedRecordMethodIsTheSameOneAsTheOneTheDb() {
        employeeDAO.clearHashMap();
        employeeDAO.csvToHashMap("src/main/resources/EmployeeRecordsLarge.csv");
        employeeDAO.convertMapToSQL(employeeDAO.employeesMap);
        String result = employeeDAO.retrieveRecordsFromSQL(916980);
        EmployeeDTO employeeDTO = employeeDAO.employeesMap.get("916980");
        System.out.println(result);
        System.out.println(employeeDAO.employeesMap.size());
//        Assertions.assertEquals(expected.toString(), result.toString());
    }
}
