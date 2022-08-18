package com.sparta.ml.start;

import com.sparta.ml.controller.ConnectionManager;
import com.sparta.ml.controller.ThreadedJDBC;

public class App {
    public static void main(String[] args) {
        Runner.start();
        ThreadedJDBC.runThreads();
    }
}