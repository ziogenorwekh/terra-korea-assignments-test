package com.terrakorea.assignment.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class CpuUsageMinuteResponse {

    private final Double cpuUsage;
    private final Date date;
    private final Date time;

    @Builder
    public CpuUsageMinuteResponse(Double cpuUsage, Date date, Date time) {
        this.cpuUsage = cpuUsage;
        this.date = date;
        this.time = time;
    }
}
