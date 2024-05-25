package com.terrakorea.assignment.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ExceptionResponse {

    private final List<String> reason;
    private final HttpStatusCode status;

    @Builder
    public ExceptionResponse(List<String> reason, HttpStatusCode status) {
        this.reason = reason;
        this.status = status;
    }

}
