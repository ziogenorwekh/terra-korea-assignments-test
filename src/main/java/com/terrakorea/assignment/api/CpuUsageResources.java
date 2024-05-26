package com.terrakorea.assignment.api;

import com.terrakorea.assignment.monitoring.CustomTimer;
import com.terrakorea.assignment.service.CpuUsageService;
import com.terrakorea.assignment.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "cpu usage", description = "the cpu usage retrieve API")
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
public class CpuUsageResources {

    private final CpuUsageService cpuUsageService;

    @Autowired
    public CpuUsageResources(CpuUsageService cpuUsageService) {
        this.cpuUsageService = cpuUsageService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CpuUsageMinuteResponse.class)),
                    }),
            @ApiResponse(description = "successful operation")
    })
    @Operation(summary = "분 단위 API", description = "지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.")
    @RequestMapping(value = "/usage/minute/{year}/{month}/{day}/{hour}", method = RequestMethod.GET)
    public ResponseEntity<List<CpuUsageMinuteResponse>> retrieveAMinutesCpuUsage(
            @Parameter(description = "year") @PathVariable int year,
            @Parameter(description = "month") @PathVariable int month,
            @Parameter(description = "day") @PathVariable int day,
            @Parameter(description = "hour") @PathVariable int hour) {

        List<CpuUsageMinuteResponse> minuteResponses = cpuUsageService.findCpuUsageMinute(CpuUsageRequest.builder()
                .year(year)
                .month(month)
                .day(day)
                .hour(hour)
                .build());

        return ResponseEntity.ok(minuteResponses);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CpuUsageHourResponse.class)),
                    }),
            @ApiResponse(description = "successful operation")
    })
    @Operation(summary = "시 단위 API", description = "지정한 날짜의 시  단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    @RequestMapping(value = "/usage/hour/{year}/{month}/{day}", method = RequestMethod.GET)
    public ResponseEntity<CpuUsageHourResponse> retrieveAHoursCpuUsage(
            @Parameter(description = "year") @PathVariable int year,
            @Parameter(description = "month") @PathVariable int month,
            @Parameter(description = "day") @PathVariable int day
    ) {
        CpuUsageHourResponse cpuUsageHourResponse = cpuUsageService.findCpuUsageHour(CpuUsageRequest.builder()
                .year(year)
                .month(month)
                .day(day)
                .build());

        return ResponseEntity.ok().body(cpuUsageHourResponse);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CpuUsageDayResponse.class)),
                    }),
            @ApiResponse(description = "successful operation")
    })
    @Operation(summary = "일 단위 API", description = "지정한 날짜 구간의 일  단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    @RequestMapping(value = "/usage/day/from/{fromYear}/{fromMonth}/{fromDay}/to/{toYear}/{toMonth}/{toDay}",
            method = RequestMethod.GET)
    public ResponseEntity<CpuUsageDayResponse> retrieveADaysCpuUsage(
            @Parameter(description = "from year") @PathVariable int fromYear,
            @Parameter(description = "from day") @PathVariable int fromDay,
            @Parameter(description = "from month") @PathVariable int fromMonth,
            @Parameter(description = "to year") @PathVariable int toYear,
            @Parameter(description = "to day") @PathVariable int toDay,
            @Parameter(description = "to month") @PathVariable int toMonth
    ) {

        CpuUsageDayResponse cpuUsageDayResponses = cpuUsageService.findCpuUsageDay(CpuUsageRangeRequest.builder()
                .fromYear(fromYear)
                .fromMonth(fromMonth)
                .fromDay(fromDay)
                .toYear(toYear)
                .toMonth(toMonth)
                .toDay(toDay)
                .build());

        return ResponseEntity.ok().body(cpuUsageDayResponses);
    }

}
