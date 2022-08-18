package com.sparta.ml.controller;

import com.sparta.ml.model.EmployeeDTO;

import java.sql.Connection;
import java.util.Map;

public class ThreadedJDBC implements Runnable {

    private final Map<String, EmployeeDAO> employees;
    private final Connection connection;

    public ThreadedJDBC(Map<String, EmployeeDTO> employees) {
        this.employees = employees;
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public void run() {
        EmployeeDAO emp = new EmployeeDAO();
        for (EmployeeDTO employee : employees) {
            emp.insertEmployee(employee, connection);
        }
    }
}
