package com.sparta.ml;//package com.sparta.ml;

import com.sparta.ml.dto.EmployeeDTO;

import java.sql.Connection;
import java.util.HashMap;

public class ThreadedJDBC implements Runnable {
    private final HashMap<String, EmployeeDTO> employees;
    private final Connection connection;
    public ThreadedJDBC(HashMap<String, EmployeeDTO> employees) {
        this.employees = employees;
        this.connection = ConnectionManager.connectToDB();
    }
    @Override
    public void run() {
//        EmployeeDAO emp = new EmployeeDAO();
//        for (EmployeeDTO employee : employees) {
//            emp.insertEmployee(employee, connection);
//        }
    }
}