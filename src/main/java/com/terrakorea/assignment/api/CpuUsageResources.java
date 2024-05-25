package com.terrakorea.assignment.api;

import com.terrakorea.assignment.monitoring.CustomTimer;
import com.terrakorea.assignment.service.CpuUsageService;
import com.terrakorea.assignment.vo.CpuUsageHourResponse;
import com.terrakorea.assignment.vo.CpuUsageMinuteResponse;
import com.terrakorea.assignment.vo.CpuUsageRangeRequest;
import com.terrakorea.assignment.vo.CpuUsageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
public class CpuUsageResources {

    private final CpuUsageService cpuUsageService;

    @Autowired
    public CpuUsageResources(CpuUsageService cpuUsageService) {
        this.cpuUsageService = cpuUsageService;
    }

    @RequestMapping(value = "/usage/minute/{year}/{month}/{day}/{hour}", method = RequestMethod.GET)
    public ResponseEntity<List<CpuUsageMinuteResponse>> retrieveAMinutesCpuUsage(@PathVariable int year,
                                                                                 @PathVariable int month,
                                                                                 @PathVariable int day,
                                                                                 @PathVariable int hour) {

        List<CpuUsageMinuteResponse> minuteResponses = cpuUsageService.findCpuUsageMinute(CpuUsageRequest.builder()
                .year(year)
                .month(month)
                .day(day)
                .hour(hour)
                .build());

        return ResponseEntity.ok(minuteResponses);
    }

    @RequestMapping(value = "/usage/hour/{year}/{month}/{day}", method = RequestMethod.GET)
    public ResponseEntity<List<CpuUsageHourResponse>> retrieveAHoursCpuUsage(@PathVariable int day,
                                                                             @PathVariable int month,
                                                                             @PathVariable int year) {
        List<CpuUsageHourResponse> cpuUsageHourResponses = cpuUsageService.findCpuUsageHour(CpuUsageRequest.builder()
                .year(year)
                .month(month)
                .day(day)
                .build());

        return ResponseEntity.ok().body(cpuUsageHourResponses);
    }

    @RequestMapping(value = "/usage/day/{year}/from/{fromMonth}/{fromDay}/to/{toMonth}/{toDay}", method = RequestMethod.GET)
    public ResponseEntity<Void> retrieveADaysCpuUsage(@PathVariable int fromDay,
                                                      @PathVariable int fromMonth,
                                                      @PathVariable int toDay,
                                                      @PathVariable int toMonth,
                                                      @PathVariable int year) {

        cpuUsageService.findCpuUsageDay(CpuUsageRangeRequest.builder()
                .year(year)
                .fromMonth(fromMonth)
                .fromDay(fromDay)
                .toMonth(toMonth)
                .toDay(toDay)
                .build());


        return ResponseEntity.noContent().build();
    }

}
