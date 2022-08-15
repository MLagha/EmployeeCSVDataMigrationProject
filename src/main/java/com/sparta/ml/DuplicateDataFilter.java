//package com.sparta.ml;
//
//import com.sparta.ml.dao.EmployeeDTO;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.ConsoleHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class DuplicateDataFilter extends EmployeeDTO{
//    private static final Logger logger = Logger.getLogger("my logger");
//    private static ConsoleHandler consoleHandler = new ConsoleHandler();
//
//    public DuplicateDataFilter (EmployeeDTO employeeDTO) {
//        super(employeeDTO);
//    }
//    public Map<String, EmployeeDTO> filter(List<EmployeeDTO> employees) {
//        consoleHandler.setLevel(Level.INFO);
//        logger.log(Level.INFO,"filter method started");
//
//        Map employeeHashMap = new HashMap(employees.size());
//        return null;
//    }
//
//
//
//}
