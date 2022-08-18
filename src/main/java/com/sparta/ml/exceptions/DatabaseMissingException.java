package com.sparta.ml.exceptions;

public class DatabaseMissingException extends RuntimeException{
    public DatabaseMissingException(String s) {
        super(s);
    }
}
