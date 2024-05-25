package com.terrakorea.assignment.vo;

import com.terrakorea.assignment.monitoring.UsageResultVO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@ToString
public class CpuUsageDayResponse {

    private final Double minCpuUsage;
    private final Double maxCpuUsage;
    private List<DayResponse> dayResponses;

    @Builder
    public CpuUsageDayResponse(Double minCpuUsage, Double maxCpuUsage, List<UsageResultVO> usageResultVOS) {
        this.minCpuUsage = minCpuUsage;
        this.maxCpuUsage = maxCpuUsage;
        this.dayResponses = new ArrayList<>();
        if (usageResultVOS != null) {
            usageResultVOS.forEach(usageResultVO -> {
                dayResponses.add(DayResponse.builder()
                        .avgCpuUsage(usageResultVO.getAvg())
                        .date(usageResultVO.getDate())
                        .build());
            });
        }
    }

    static class DayResponse {
        private final Double avgCpuUsage;
        private final Date date;

        @Builder
        DayResponse(Double avgCpuUsage, Date date) {
            this.avgCpuUsage = avgCpuUsage;
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public Double getAvgCpuUsage() {
            return avgCpuUsage;
        }

        @Override
        public String toString() {
            return "DayResponse{" +
                    "avgCpuUsage=" + avgCpuUsage +
                    ", date=" + date +
                    '}' +"\n";
        }
    }
}
