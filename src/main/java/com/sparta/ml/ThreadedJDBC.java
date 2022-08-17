package com.sparta.ml;//package com.sparta.ml;
//
//import com.sparta.ml.dao.EmployeeDAO;
//
//import java.sql.Connection;
//import java.util.HashMap;
//
//public class ThreadedJDBC implements Runnable {
//    private final HashMap<EmployeesDTO> employees;
//    private final Connection connection;
//    public ThreadedJDBC(HashMap<EmployeeDAO> employees) {
//        this.employees = employees;
//        this.connection = ConnectionManager.getConnection();
//    }
//    @Override
//    public void run() {
//        EmployeeDAO emp = new EmployeeDAO();
//        for (EmployeeDTO employee : employees) {
//            emp.insertEmployee(employee, connection);
//        }
//    }
//}