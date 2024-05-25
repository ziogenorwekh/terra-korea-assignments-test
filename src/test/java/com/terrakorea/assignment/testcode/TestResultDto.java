package com.terrakorea.assignment.testcode;


import java.util.Date;

public class TestResultDto {


    private Date days;
    private final Double avg;
    private Integer hour;

    private TestResultDto(Builder builder) {
        this.days = builder.days;
        this.avg = builder.avg;
        this.hour = builder.hour;
    }

    public Double getAvg() {
        return avg;
    }

    public Date getDays() {
        return days;
    }

    public Integer getHour() {
        return hour;
    }

    public static class Builder {
        private Date days;
        private Double avg;
        private Integer hour;

        public Builder() {
        }

        public Builder days(Date days) {
            this.days = days;
            return this;
        }

        public Builder avg(Double avg) {
            this.avg = avg;
            return this;
        }

        public Builder hour(Integer hour) {
            this.hour = hour;
            return this;
        }

        public TestResultDto build() {
            return new TestResultDto(this);
        }
    }
}
