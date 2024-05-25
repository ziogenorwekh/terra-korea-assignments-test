package com.terrakorea.assignment.monitoring;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class UsageResultVO {
    private Date days;
    private Double avg;
    private Integer hour;

    @Builder
    public UsageResultVO(Date days, Double avg, Integer hour) {
        this.days = days;
        this.avg = avg;
        this.hour = hour;
    }
}
