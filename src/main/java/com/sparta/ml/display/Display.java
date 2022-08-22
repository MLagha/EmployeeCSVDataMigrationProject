package com.sparta.ml.display;

import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.controller.ThreadedJDBC;

public class Display {
    public static void enterSQLRecords() {

        int NoOfUniqueCleanRecords = EmployeeDAO.NoOfUniqueCleanRecords;
        if (EmployeeDAO.NoOfUniqueCleanRecords == 0){
            NoOfUniqueCleanRecords = ThreadedJDBC.mainMap.size();
        }

        System.out.println("\nYou have " + EmployeeDAO.NoOfDuplicateRecords + " duplicate records.");
        System.out.println("You have " + EmployeeDAO.NoOfCorruptedRecords + " corrupted records.");
        System.out.println("You have " + NoOfUniqueCleanRecords + " unique and clean records.");
    }
}
