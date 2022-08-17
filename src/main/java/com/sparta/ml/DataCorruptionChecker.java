package com.sparta.ml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataCorruptionChecker {
    private static final Logger logger = Logger.getLogger("my logger");
    private static ConsoleHandler consoleHandler = new ConsoleHandler();
    private static final int numberOfColumns = 10;

    public static boolean isRecordCorrupt(String[] employeeRecord) throws ParseException {

        return (employeeRecord.length == numberOfColumns &&
                isEmployeeIdCorrupt(employeeRecord[0]) &&
                isTitleCorrupt(employeeRecord[1]) &&
                isNameCorrupt(employeeRecord[2]) &&
                isMiddleInitialCorrupt(employeeRecord[3]) &&
                isNameCorrupt(employeeRecord[4]) &&
                isGenderCorrupt(employeeRecord[5]) &&
                isEmailCorrupt(employeeRecord[6]) &&
                isDateCorrupt(employeeRecord[7]) &&
                isDateCorrupt(employeeRecord[8]) &&
                isSalaryCorrupt(employeeRecord[9]));
    }

    private static boolean isEmployeeIdCorrupt(String employeeId){
        consoleHandler.setLevel(Level.INFO);
        try {
            Integer.parseInt(employeeId);
        } catch (NumberFormatException e) {
            logger.log(Level.FINE, employeeId + " isEmployeeIdCorrupt is false");
            return false;
        }
        logger.log(Level.FINE, employeeId + " isEmployeeIdCorrupt is true");
        return true;
    }

    private static boolean isTitleCorrupt(String title){
        String[] titles = new String[] {"Prof.", "Mrs.", "Mr.", "Ms.", "Dr.", "Drs.", "Hon."};
        for (String honoraryTitle : titles) {
            if(honoraryTitle.equals(title)) return true; //checked on Excel to see all possibilities
        }
        return false;
    }

    private static boolean isNameCorrupt(String name){
        return (name.matches("^[a-zA-Z]*$")); //regex for string char, capital included
    }

    private static boolean isMiddleInitialCorrupt(String middleInitial){
        return (middleInitial.matches("^[A-Z]") || middleInitial.length() != 1); //if not a letter or not 1 character, return false
    }

    private static boolean isGenderCorrupt(String gender){
        return gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("M"); //will be false if anything else
    }

    private static boolean isEmailCorrupt(String email){
        return email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$"); // looked up the regex for string, came up with that
    }

    private static boolean isDateCorrupt(String dates) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        Date date = new Date();
        if (dateFormat.parse(dates).after(date)) { // compares date to see if it's in the future
            return false;
        }
        return true;
    }

    private static boolean isSalaryCorrupt(String salary){
        if (Integer.parseInt(salary) < 0) { //cant have salary under 0
            return false;
        }
        return true;
    }
}