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
@Schema(name = "CpuUsageDayResponse -> cpu usage response by date")
public class CpuUsageDayResponse {


    @Schema(name = "minCpuUsage", description = "Min cpu usage")
    private final Double minCpuUsage;
    @Schema(name = "maxCpuUsage", description = "Max cpu usage")
    private final Double maxCpuUsage;
    @Schema(name = "dayResponses", description = "List of average values by date")
    private List<DayResponse> dayResponses;

    @Builder
    public CpuUsageDayResponse(Double minCpuUsage, Double maxCpuUsage, List<UsageResultVO> usageResultVOS) {
        this.minCpuUsage = minCpuUsage;
        this.maxCpuUsage = maxCpuUsage;
        this.dayResponses = new ArrayList<>();
        if (usageResultVOS != null) {
            usageResultVOS.forEach(usageResultVO -> {
                dayResponses.add(DayResponse.builder()
                        .avgCpuUsage(BigDecimal.valueOf(usageResultVO.getAvg())
                                .setScale(4, RoundingMode.HALF_UP).doubleValue())
                        .date(usageResultVO.getDate())
                        .build());
            });
        }
    }
    @Schema(name = "DayResponse -> CpuUsageDayResponse's inner response class")
    public static class DayResponse {

        @Schema(name = "avgCpuUsage", description = "Average cpu usage")
        private final Double avgCpuUsage;

        @Schema(name = "Date", description = "Average cpu usage by date")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
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
