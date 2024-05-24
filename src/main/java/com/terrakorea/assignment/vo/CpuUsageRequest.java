package com.terrakorea.assignment.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CpuUsageRequest {

    @Min(value = 1990, message = "Year must be greater than or equal to 1990")
    private final int year;

    @Min(value = 1, message = "Month must be greater than or equal to 1")
    @Max(value = 12, message = "Month must be less than or equal to 12")
    private final int month;

    @Min(value = 1, message = "Day must be greater than or equal to 1")
    @Max(value = 31, message = "Day must be less than or equal to 31")
    private final int day;

    @Min(value = 0, message = "Hour must be greater than or equal to 0")
    @Max(value = 23, message = "Hour must be less than or equal to 23")
    private final int hour;

    @Builder
    public CpuUsageRequest(int year, int month, int day, int hour) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
    }
}
