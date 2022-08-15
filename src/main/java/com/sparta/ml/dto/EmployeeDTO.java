package com.sparta.ml.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Emp ID, Name Prefix, First Name, Middle Initial, Last Name, Gender, E Mail, Date of Birth, Date of Joining, Salary
//198429, Mrs.,
public class EmployeeDTO {
    private String empID;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String LastName;
    private String gender; //private char gender
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private Float salary;

    //09/07/2000
    public EmployeeDTO(String empID, String namePrefix, String firstName, String middleInitial, String lastName, String gender, String email, String dateOfBirth, String dateOfJoining, String salary) {
        this.empID = empID;
        this.namePrefix = namePrefix;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.LastName = lastName;
        this.gender = gender;
        this.email = email;
        this.dateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("M/d/uuuu"));
        this.dateOfJoining = LocalDate.parse(dateOfJoining, DateTimeFormatter.ofPattern("M/d/uuuu"));
        this.salary = Float.valueOf(salary);
    }

    public EmployeeDTO(String[] csvInput) {
        new EmployeeDTO (
                csvInput[0],
                csvInput[1],
                csvInput[2],
                csvInput[3],
                csvInput[4],
                csvInput[5],
                csvInput[6],
                csvInput[7],
                csvInput[8],
                csvInput[9]
        );
    }

    public String getEmpID() {
        return empID;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getLastName() {
        return LastName;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public Float getSalary() {
        return salary;
    }
}















