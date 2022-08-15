package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class duplicateDataFilter implements Filters{
    private static final Logger logger = Logger.getLogger("my logger");
    private static ConsoleHandler consoleHandler = new ConsoleHandler();

    @Override
    public List<EmployeeDTO> filter(List<EmployeeDTO> employees) {
        consoleHandler.setLevel(Level.INFO);
        logger.log(Level.INFO,"filter method started");

        HashMap employeeHashMap = new HashMap(employees.size());
        return null;    }
//    public String[]
}
