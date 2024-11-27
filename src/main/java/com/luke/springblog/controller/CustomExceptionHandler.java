package com.luke.springblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException (MethodArgumentNotValidException ex) {

        List<String> errors =
                    ex.getBindingResult().getFieldErrors().stream()
                            .map(e-> e.getField() + ": " +e.getDefaultMessage())
                            .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);








    }




}
