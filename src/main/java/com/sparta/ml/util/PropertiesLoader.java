package com.sparta.ml.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    public static String getProperty(String key) {
        Properties properties = new Properties();
        try {
            properties.load (new FileReader("src/main/resources/database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }
}
