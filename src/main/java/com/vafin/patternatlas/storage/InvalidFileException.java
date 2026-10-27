package com.vafin.patternatlas.storage;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String filename) {
        super("Invalid file: " + filename);
    }
}
