package com.sparta.ml.display;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import com.sparta.ml.start.Runner;

import java.sql.Connection;
import java.sql.SQLException;

public class Display {

    public static void enterSQLRecords() {
        System.out.println("\nTime taken to persist to SQL table before implementing multiple threads: " + (Runner.end - Runner.start)/1_000_000_000 + " seconds");
    }
}
