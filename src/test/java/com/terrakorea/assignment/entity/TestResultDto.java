package com.terrakorea.assignment.entity;


public class TestResultDto {

    public TestResultDto(Integer hour, Double avg) {
        this.hour = hour;
        this.avg = avg;
    }

    private Integer hour;
    private Double avg;


    public Double getAvg() {
        return avg;
    }

    public Integer getHour() {
        return hour;
    }
}
