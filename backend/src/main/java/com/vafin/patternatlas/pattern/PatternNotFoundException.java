package com.vafin.patternatlas.pattern;

public class PatternNotFoundException extends RuntimeException {
    public PatternNotFoundException(Integer id) {
        super("Could not find pattern with id " + id);
    }
}
