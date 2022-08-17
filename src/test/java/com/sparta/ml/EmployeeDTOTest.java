package com.sparta.ml;

import com.sparta.ml.dto.EmployeeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

public class EmployeeDTOTest {

    @Test
    @DisplayName("Test to see if program will get right properties")
    void TestEmployeeRecord() {

        String[] testString = {"487417", "Mrs.", "Dotty", "H", "Leyba", "F" , "dotty.leyba@bp.com", "6/16/1996", "6/27/2017", "62561"};
        EmployeeDTO employee = new EmployeeDTO(testString);
        Assertions.assertEquals("487417", employee.getEmpID());
        Assertions.assertEquals("Mrs.", employee.getNamePrefix());
        Assertions.assertEquals("Dotty", employee.getFirstName());
        Assertions.assertEquals("H", employee.getMiddleInitial());
        Assertions.assertEquals("Leyba", employee.getLastName());
        Assertions.assertEquals("F", employee.getGender());
        Assertions.assertEquals("dotty.leyba@bp.com", employee.getEmail());
        Assertions.assertEquals("6/16/1996",  DateTimeFormatter.ofPattern("M/d/yyyy").format(employee.getDateOfBirth()));
        Assertions.assertEquals("6/27/2017",  DateTimeFormatter.ofPattern("M/d/yyyy").format(employee.getDateOfJoining()));
        Assertions.assertEquals("62561", employee.getSalary());
    }
}