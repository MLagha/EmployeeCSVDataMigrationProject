package com.sparta.ml.controller.util;

public interface SQLQueries {
    String SELECT = "SELECT * from public.employee_db WHERE emp_id = ?";
    String DROP_TABLE = "DROP TABLE IF EXISTS public.employee_db";
    String CREATE_TABLE = "CREATE TABLE public.employee_db";
    String INSERT_INTO_DB =  "INSERT INTO public.employee_db (Emp_ID, Name_Prefix, First_Name, Middle_Initial" +
            ", Last_Name, Gender, E_Mail, Date_of_Birth, Date_of_Joining, Salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
}
