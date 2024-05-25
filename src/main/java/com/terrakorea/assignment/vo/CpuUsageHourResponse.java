package com.terrakorea.assignment.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class CpuUsageHourResponse {

    private final Date date;
    private final int time;
    private final Double minCpuUsage;
    private final Double maxCpuUsage;
    private final Double avgCpuUsage;

    @Builder
    public CpuUsageHourResponse(Date date, int time, Double minCpuUsage, Double maxCpuUsage, Double avgCpuUsage) {
        this.date = date;
        this.time = time;
        this.minCpuUsage = minCpuUsage;
        this.maxCpuUsage = maxCpuUsage;
        this.avgCpuUsage = avgCpuUsage;
    }
}
