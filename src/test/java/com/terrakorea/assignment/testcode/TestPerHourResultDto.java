package com.terrakorea.assignment.testcode;


public class TestPerHourResultDto {

    public TestPerHourResultDto(Integer hour, Double avg) {
        this.hour = hour;
        this.avg = avg;
    }

    private final Integer hour;
    private final Double avg;


    public Double getAvg() {
        return avg;
    }

    public Integer getHour() {
        return hour;
    }
}
