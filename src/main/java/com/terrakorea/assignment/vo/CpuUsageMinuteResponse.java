package com.terrakorea.assignment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@Schema(name = "CpuUsageMinuteResponse -> cpu usage response by minute")
public class CpuUsageMinuteResponse {

    @Schema(name = "hour", description = "Average cpu usage by minute")
    private final Double cpuUsage;

    @Schema(name = "Date", description = "Date inquired")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final Date date;

    @Schema(name = "minute", description = "minute inquired")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private final Date time;

    @Builder
    public CpuUsageMinuteResponse(Double cpuUsage, Date date, Date time) {
        this.cpuUsage = cpuUsage;
        this.date = date;
        this.time = time;
    }
}
