package com.terrakorea.assignment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Getter
//@Schema(name = "cpu usage range request")
public class CpuUsageRangeRequest {


    @Min(value = 1990, message = "Year must be greater than or equal to 1990")
//    @Schema(name = "start year", example = "2024", required = true)
    private final int fromYear;


    @Min(value = 1, message = "Month must be greater than or equal to 1")
    @Max(value = 12, message = "Month must be less than or equal to 12")
//    @Schema(name = "start moth", example = "3", required = true)
    private final int fromMonth;

    @Min(value = 1, message = "Day must be greater than or equal to 1")
    @Max(value = 31, message = "Day must be less than or equal to 31")
//    @Schema(name = "start day", example = "10", required = true)
    private final int fromDay;

    @Min(value = 1990, message = "Year must be greater than or equal to 1990")
//    @Schema(name = "end year", example = "2024", required = true)
    private final int toYear;

    @Min(value = 1, message = "Month must be greater than or equal to 1")
    @Max(value = 12, message = "Month must be less than or equal to 12")
//    @Schema(name = "end moth", example = "5", required = true)
    private final int toMonth;

    @Min(value = 1, message = "Day must be greater than or equal to 1")
    @Max(value = 31, message = "Day must be less than or equal to 31")
//    @Schema(name = "end day", example = "3", required = true)
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
