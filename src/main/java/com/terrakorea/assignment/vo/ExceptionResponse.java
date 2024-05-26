package com.terrakorea.assignment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Schema(name = "exception response")
public class ExceptionResponse {

    @Schema(name = "error message")
    private final List<String> reason;
    @Schema(name = "error status code")
    private final HttpStatusCode status;

    @Builder
    public ExceptionResponse(List<String> reason, HttpStatusCode status) {
        this.reason = reason;
        this.status = status;
    }

}
