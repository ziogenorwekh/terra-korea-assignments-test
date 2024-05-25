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
public class CpuUsageHourResponse {

    private final Date date;
    //    private final int time;
    private final Double minCpuUsage;
    private final Double maxCpuUsage;
    private List<HourResponse> hourResponses;
//    private final Double avgCpuUsage;

    @Builder
    public CpuUsageHourResponse(Date date, Double minCpuUsage, Double maxCpuUsage, List<UsageResultVO> usageResultVOS) {
        this.date = date;
//        this.time = time;
        this.minCpuUsage = minCpuUsage;
        this.maxCpuUsage = maxCpuUsage;
        hourResponses = new ArrayList<>();
        if (usageResultVOS != null) {
            usageResultVOS.forEach(usageResultVO -> {
                hourResponses.add(HourResponse.builder()
                        .avgCpuUsage(usageResultVO.getAvg())
                        .time(usageResultVO.getHour())
                        .build());
            });
        }
//        this.avgCpuUsage = avgCpuUsage;
    }

    static class HourResponse {
        private final int time;
        private final Double avgCpuUsage;

        @Builder
        HourResponse(int time, Double avgCpuUsage) {
            this.time = time;
            this.avgCpuUsage = avgCpuUsage;
        }

        public int getTime() {
            return time;
        }

        public Double getAvgCpuUsage() {
            return avgCpuUsage;
        }

        @Override
        public String toString() {
            return "HourResponse{" +
                    "time=" + time +
                    ", avgCpuUsage=" + avgCpuUsage +
                    '}' + "\n";
        }
    }
}
