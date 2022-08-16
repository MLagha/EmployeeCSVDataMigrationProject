package com.sparta.ml.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//Storing of Employee Data:

//Data Transfer Object: Responsible for pulling the data --> packing it in an object --> providing you getters to get hold of those values
//Read correct Type then convert constructor

//Emp ID, Name Prefix, First Name, Middle Initial, Last Name, Gender, E Mail, Date of Birth, Date of Joining, Salary
//198429, Mrs.,
public class EmployeeDTO {
    private String empID;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String gender; //private char gender
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private Float salary;

    public EmployeeDTO(String [] csvInput) {
        this.empID = csvInput[0];
        this.namePrefix = csvInput[1];
        this.firstName = csvInput[2];
        this.middleInitial = csvInput[3];
        this.lastName = csvInput[4];
        this.gender = csvInput[5];
        this.email = csvInput[6];
        this.dateOfBirth = LocalDate.parse(csvInput[7], DateTimeFormatter.ofPattern("M/d/uuuu"));
        this.dateOfJoining = LocalDate.parse(csvInput[8], DateTimeFormatter.ofPattern("M/d/uuuu"));
        this.salary = Float.valueOf(csvInput[9]);
    }

    /* Constructor doesn't work!!
    //replace with HashMap
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
     */

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
        return lastName;
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

