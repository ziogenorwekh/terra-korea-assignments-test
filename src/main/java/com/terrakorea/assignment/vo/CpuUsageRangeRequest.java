package com.terrakorea.assignment.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CpuUsageRangeRequest {


    private final int year;
    private final int fromMonth;
    private final int fromDay;
    private final int toMonth;
    private final int toDay;

    @Builder
    public CpuUsageRangeRequest(int year, int fromMonth, int fromDay, int toMonth, int toDay) {
        this.year = year;
        this.fromMonth = fromMonth;
        this.fromDay = fromDay;
        this.toMonth = toMonth;
        this.toDay = toDay;
    }
}
