package com.terrakorea.assignment.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CpuUsageRangeRequest {


    @Min(value = 1990, message = "Year must be greater than or equal to 1990")
    private final int fromYear;


    @Min(value = 1, message = "Month must be greater than or equal to 1")
    @Max(value = 12, message = "Month must be less than or equal to 12")
    private final int fromMonth;

    @Min(value = 1, message = "Day must be greater than or equal to 1")
    @Max(value = 31, message = "Day must be less than or equal to 31")
    private final int fromDay;

    @Min(value = 1990, message = "Year must be greater than or equal to 1990")
    private final int toYear;

    @Min(value = 1, message = "Month must be greater than or equal to 1")
    @Max(value = 12, message = "Month must be less than or equal to 12")
    private final int toMonth;

    @Min(value = 1, message = "Day must be greater than or equal to 1")
    @Max(value = 31, message = "Day must be less than or equal to 31")
    private final int toDay;

    @Builder
    public CpuUsageRangeRequest(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {
        this.fromYear = fromYear;
        this.toYear = toYear;
        this.fromMonth = fromMonth;
        this.fromDay = fromDay;
        this.toMonth = toMonth;
        this.toDay = toDay;
    }
}
