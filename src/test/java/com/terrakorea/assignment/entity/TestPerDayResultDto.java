package com.terrakorea.assignment.entity;

public class TestPerDayResultDto {


    private final Integer days;
    private final Double avg;

    public TestPerDayResultDto(Integer days, Double avg) {
        this.days = days;
        this.avg = avg;
    }

    public Double getAvg() {
        return avg;
    }

    public Integer getDays() {
        return days;
    }
}
