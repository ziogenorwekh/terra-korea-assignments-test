package com.terrakorea.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "데이터 조회 일자 한도 초과")
public class InvalidateCalendarException extends RuntimeException {

    public InvalidateCalendarException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidateCalendarException(String message) {
        super(message);
    }
}
