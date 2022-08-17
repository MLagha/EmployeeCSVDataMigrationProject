package com.sparta.ml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataCorruptionCheckerTest {

    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Test
    @DisplayName("Test to asses whether an ID input is valid")
    void TestValidID() {
        Assertions.assertTrue(isStringInt("13124"));
        Assertions.assertFalse(isStringInt("453y34"));
    }

    @Test
    void TestValidNamePrefix() {
        String[] titles = new String[] {"Prof.", "Mrs.", "Mr.", "Ms.", "Dr.", "Drs.", "Hon."};
        boolean validTest1 = false;
        boolean validTest2 = false;
        for (String honoraryTitle : titles) {
            if(honoraryTitle.equals("Mr.")) validTest1 = true;
            if(honoraryTitle.equals("Jr.")) validTest2 = true;
        }
        Assertions.assertTrue(validTest1);
        Assertions.assertFalse(validTest2);
    }

    @Test
    void TestValidName() {
        Assertions.assertTrue(("Samuel").matches("^[a-zA-Z]*$"));
        Assertions.assertFalse(("S4mmy".matches("^[a-zA-Z]*$")));
    }

    @Test
    void TestValidInitial() {
        Assertions.assertTrue(("L".matches("^[A-Z]") || "L".length() != 1));
        Assertions.assertFalse(("BR".matches("^[A-Z]") && "BR".length() != 1));
    }

    @Test
    void TestValidGender() {
        Assertions.assertTrue("M".equalsIgnoreCase("F") || "M".equalsIgnoreCase("M"));
        Assertions.assertTrue("F".equalsIgnoreCase("F") || "F".equalsIgnoreCase("M"));
        Assertions.assertFalse("A".equalsIgnoreCase("F") || "A".equalsIgnoreCase("M"));
    }

    @Test
    void TestValidEmail() {
        Assertions.assertTrue("amy.bob@gmail.com".matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$"));
        Assertions.assertFalse("ilyas@gmail@hotmail.com".matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$"));
    }

    @Test
    void TestValidDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        Date date = new Date();
        try {
            Assertions.assertTrue(!dateFormat.parse("06/12/2004").after(date));
            Assertions.assertTrue(!dateFormat.parse("6/2/1995").after(date));
            Assertions.assertFalse(!dateFormat.parse("27/1/2028").after(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void TestSalary() {
        Assertions.assertTrue(Integer.parseInt("56654") >= 0);
        Assertions.assertFalse(Integer.parseInt("-3459078") >= 0);
    }
}
