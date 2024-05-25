package com.terrakorea.assignment.exception;

import com.terrakorea.assignment.vo.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(objectError ->
                errors.add(objectError.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .status(status)
                .reason(errors).build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(DataNotFoundException.class)
    public ExceptionResponse handleDataNotFound(DataNotFoundException e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidateCalendarException.class)
    public ExceptionResponse handleInvalidateCalenderAccess(InvalidateCalendarException e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason(errors)
                .build();
    }
}
