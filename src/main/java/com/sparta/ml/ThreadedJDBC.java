package com.sparta.ml;//package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDAO;
import com.sparta.ml.dto.EmployeeDTO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
//        EmployeeDAO employeeDAO = new EmployeeDAO();
//        for (EmployeeDTO employee : employees) {
//            emp.insertEmployee(employee, connection);
//        }
    }
    Connection postgresConn = ConnectionManager.connectToDB();
    EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
    Map<String,EmployeeDTO> hmap = employeeDAO.getEmployeesMap();
    HashMap<String,EmployeeDTO> halfhmap1=new HashMap<>();
    HashMap<String,EmployeeDTO> halfhmap2=new HashMap<>();
    void splitHashMap() {
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        int count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : hmap.entrySet()) {
            (count < (hmap.size() / 2) ? halfhmap1 : halfhmap2).put(entry.getKey(), entry.getValue());
            count++;
        }
        employeeDAO.employeeMapToSQL(halfhmap1);
        Thread thread = new Thread(() -> employeeDAO.employeeMapToSQL(halfhmap1));
        thread.start();
    }

    public static void main(String[] args) {
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO = new EmployeeDAO(postgresConn);
        Map<String,EmployeeDTO> hmap = employeeDAO.getEmployeesMap();
        employeeDAO.populateHashMap("src/main/resources/EmployeeRecords.csv");
        HashMap<String,EmployeeDTO> halfhmap1=new HashMap<>();
        HashMap<String,EmployeeDTO> halfhmap2=new HashMap<>();
        int count = 0;
        for (Map.Entry<String, EmployeeDTO> entry : hmap.entrySet()) {
            (count < (hmap.size() / 2) ? halfhmap1 : halfhmap2).put(entry.getKey(), entry.getValue());
            count++;
        }
        employeeDAO.employeeMapToSQL(halfhmap1);
        Thread thread = new Thread(() -> employeeDAO.employeeMapToSQL(halfhmap1));
        thread.start();
    }

}