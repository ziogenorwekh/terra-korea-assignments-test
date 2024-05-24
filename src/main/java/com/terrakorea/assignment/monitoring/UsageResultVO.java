package com.terrakorea.assignment.monitoring;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UsageResultVO {
    private Integer days;
    private Double avg;
    private Integer hour;

    @Builder
    public UsageResultVO(Integer days, Double avg, Integer hour) {
        this.days = days;
        this.avg = avg;
        this.hour = hour;
    }
}
