package com.vafin.patternatlas.storage;

public class StorageFileNotFoundException extends RuntimeException {
    public StorageFileNotFoundException(String filename) {
        super("Could not find file: " + filename);
    }
}
