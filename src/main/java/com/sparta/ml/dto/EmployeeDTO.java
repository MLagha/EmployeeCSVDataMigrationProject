package com.sparta.ml.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Emp ID, Name Prefix, First Name, Middle Initial, Last Name, Gender, E Mail, Date of Birth, Date of Joining, Salary
//198429, Mrs.,
public class EmployeeDTO {
    private final String empID;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String gender; //private char gender
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private Float salary;

    //09/07/2000
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

    @Override
    public String toString() {
        return "EmployeeID" + empID +
                "namePrefix='" +  + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleInitial='" + middleInitial + '\'' +
                ", LastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfJoining=" + dateOfJoining +
                ", salary=" + salary + "\n";
    }
}