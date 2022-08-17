package com.sparta.ml;
//Execute a statement (for DROP, CREATE, INSERT, UPDATE, or DELETE statements)

public interface SQLQueries {
    public static final String SELECT_ALL = "SELECT * from public.employee_db";         //no need for this line!!

    String DROP_TABLE = "DROP TABLE IF EXISTS public.employee_db";
    String CREATE_TABLE = "CREATE TABLE public.employee_db";
    String INSERT_INTO_DB =  "INSERT INTO public.employee_db (empID, namePrefix, firstName, middleInitial, lastName, gender, email, dateOfBirth, dateOfJoining, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    //String INSERT_INTO_DB =  "INSERT INTO public.employee_db (Emp ID, Name Prefix, First Name, Middle Initial, Last Name, Gender, E Mail, Date of Birth, Date of Joining, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";



}
