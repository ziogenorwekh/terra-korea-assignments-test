package com.terrakorea.assignment.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

    private final String reason;
    private final HttpStatus status;

    @Builder
    public ExceptionResponse(String reason, HttpStatus status) {
        this.reason = reason;
        this.status = status;
    }


}
