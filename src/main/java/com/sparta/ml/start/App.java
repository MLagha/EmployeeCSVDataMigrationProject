package com.sparta.ml.start;

import com.sparta.ml.controller.ThreadedJDBC;

public class App {
    public static void main(String[] args) throws InterruptedException {

        Runner.start();                   //For Single thread
//        ThreadedJDBC.runThreads();          //For Multithreading
    }
}