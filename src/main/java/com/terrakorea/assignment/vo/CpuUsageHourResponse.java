package com.terrakorea.assignment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.terrakorea.assignment.monitoring.UsageResultVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@ToString
@Schema(name = "CpuUsageHourResponse -> cpu usage response by hour")
public class CpuUsageHourResponse {

    @Schema(name = "Date", description = "Date inquired")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final Date date;
    //    private final int time;
    @Schema(name = "minCpuUsage", description = "Min cpu usage")
    private final Double minCpuUsage;
    @Schema(name = "maxCpuUsage", description = "Max cpu usage")
    private final Double maxCpuUsage;
    @JsonProperty("List of average values by hour")
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
                        .avgCpuUsage(BigDecimal.valueOf(usageResultVO.getAvg())
                                .setScale(4, RoundingMode.HALF_UP).doubleValue())
                        .time(usageResultVO.getHour())
                        .build());
            });
        }
//        this.avgCpuUsage = avgCpuUsage;
    }
    @Schema(name = "HourResponse -> CpuUsageHourResponse's inner response class")
    static class HourResponse {
        @Schema(name = "hour", description = "Average cpu usage by hour")
        @JsonProperty("hour")
        private final int time;
        @Schema(name = "avgCpuUsage", description = "Average cpu usage")
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
