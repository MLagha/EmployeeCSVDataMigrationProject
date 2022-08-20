package com.sparta.ml;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.EmployeeDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.Connection;

//setup //test //teardown
public class IntegrationTest {

    File cleanEntries = new File("src/main/resources/CleanEntries.csv");
    File duplicateEntries = new File ("src/main/resources/DuplicateEntries.csv");
    File corruptEntries = new File ("src/main/resources/CorruptEntries.csv");



    private void test(){
        Connection postgresConn = ConnectionManager.connectToDB();
        EmployeeDAO employeeDAO  = new EmployeeDAO(postgresConn);
        employeeDAO.filterCSVtoHashMap("src/main/resources/EmployeeRecords.csv");
        FileReader fileClean = null;
        FileReader fileDup = null;
        FileReader fileCorr = null;
        try {
            fileClean = new FileReader("src/main/resources/cleanEntries.csv");
            fileDup = new FileReader("src/main/resources/DuplicateEntries.csv");
            fileCorr = new FileReader("src/main/resources/CorruptEntries.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader cleanReader = new BufferedReader(fileClean);
        BufferedReader dupReader = new BufferedReader(fileDup);
        BufferedReader corrReader = new BufferedReader(fileCorr);

        if(cleanEntries.exists() && duplicateEntries.exists() && corruptEntries.exists()){
            System.out.println(cleanEntries.getName() + " " + duplicateEntries.getName() + " " + corruptEntries.getName() + " Exist");
        }
        try {
            System.out.println("The first line of " + cleanEntries.getName() + " " + duplicateEntries.getName() + corruptEntries.getName());
            System.out.println(cleanReader.readLine());
            System.out.println(dupReader.readLine());
            System.out.println(corrReader.readLine());
            cleanReader.close();
            dupReader.close();
            corrReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //check if files exist
        //check if records are correct
        //check if output seems right?
    }

    private void tearDown() {

        if(cleanEntries.delete() && duplicateEntries.delete() && corruptEntries.delete()){
            System.out.println("Deleted the files: " + cleanEntries.getName() + " " + duplicateEntries.getName() + " " + corruptEntries);
        }
    }

    @Test
    @DisplayName("Given a csv, separate corruptions,duplicates and clean data")
    void givenACsvSeparateCorruptionsDuplicatesAndCleanData() {
        test();
        tearDown();
    }


}
