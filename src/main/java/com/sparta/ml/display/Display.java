package com.sparta.ml.display;

import com.sparta.ml.controller.ThreadedJDBC;
import com.sparta.ml.start.Runner;

public class
Display {
    public static void enterSQLRecords() {
        System.out.println("\nTime taken to persist to SQL table before implementing multiple threads: " + (Runner.end - Runner.start)/1_000_000_000 + " seconds");
        System.out.println("\nTime taken to persist to SQL table after implementing multiple threads: " + (ThreadedJDBC.end - ThreadedJDBC.start)/1_000_000_000 + " seconds");
    }
}
