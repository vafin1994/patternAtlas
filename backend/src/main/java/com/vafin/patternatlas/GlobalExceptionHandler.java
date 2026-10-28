package com.vafin.patternatlas;

import com.vafin.patternatlas.pattern.PatternNotFoundException;
import com.vafin.patternatlas.storage.InvalidFileException;
import com.vafin.patternatlas.storage.StorageException;
import com.vafin.patternatlas.storage.StorageFileNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PatternNotFoundException.class)
    public ProblemDetail handleNotFoundException(PatternNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Pattern with this name already exists");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ProblemDetail handleStorageFileNotFound(StorageFileNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(StorageException.class)
    public ProblemDetail handleStorageException(StorageException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file");
    }

    @ExceptionHandler(InvalidFileException.class)
    public ProblemDetail handleInvalidFileException(InvalidFileException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

}
