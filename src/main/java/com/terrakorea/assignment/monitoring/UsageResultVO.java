package com.terrakorea.assignment.monitoring;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class UsageResultVO {
    private Date date;
    private Double avg;
    private Integer hour;

    @Builder
    public UsageResultVO(Date date, Double avg, Integer hour) {
        this.date = date;
        this.avg = avg;
        this.hour = hour;
    }
}
