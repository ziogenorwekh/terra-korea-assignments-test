package com.terrakorea.assignment.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class CpuUsageDayResponse {


    private final Date day;
    private final Double minCpuUsage;
    private final Double maxCpuUsage;
    private final Double avgCpuUsage;

    @Builder
    public CpuUsageDayResponse(Date day, Double minCpuUsage, Double maxCpuUsage, Double avgCpuUsage) {
        this.day = day;
        this.minCpuUsage = minCpuUsage;
        this.maxCpuUsage = maxCpuUsage;
        this.avgCpuUsage = avgCpuUsage;
    }
}
