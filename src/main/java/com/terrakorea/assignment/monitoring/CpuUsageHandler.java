package com.terrakorea.assignment.monitoring;

import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.dto.CpuUsageEntityDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CpuUsageHandler {

    public Double maxCpuUsage(List<CpuUsageEntityDto> cpuUsageEntityDtoList, CalendarType calenderType) {
        List<UsageResultVO> testResultsPerDay = this.getTestResults(cpuUsageEntityDtoList, calenderType);
        return testResultsPerDay.stream().mapToDouble(UsageResultVO::getAvg).max()
                .orElseThrow(NullPointerException::new);
    }

    public Double minCpuUsage(List<CpuUsageEntityDto> cpuUsageEntityDtoList, CalendarType calenderType) {
        List<UsageResultVO> testResultsPerDay = this.getTestResults(cpuUsageEntityDtoList, calenderType);
        return testResultsPerDay.stream()
                .mapToDouble(UsageResultVO::getAvg)
                .min().orElseThrow(NullPointerException::new);
    }

    public List<UsageResultVO> avgCpuUsage(List<CpuUsageEntityDto> cpuUsageEntityDtoList, CalendarType calenderType) {
        return this.getTestResults(cpuUsageEntityDtoList, calenderType);
    }

    private List<UsageResultVO> getTestResults(List<CpuUsageEntityDto> cpuUsageEntityDtoList, CalendarType calenderType) {
        Map<Integer, List<CpuUsageEntityDto>> mapHours = new HashMap<>();
        List<UsageResultVO> resultDtoList = new ArrayList<>();
        if (calenderType == CalendarType.HOUR) {
            cpuUsageEntityDtoList.forEach(result -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result.getCreatedTime());
                int day = calendar.get(Calendar.HOUR_OF_DAY);
                mapHours.computeIfAbsent(day, k -> new ArrayList<>()).add(result);
            });
            mapHours.forEach((integer, testEntities1) -> {
                double avgResult = testEntities1.stream().mapToDouble(CpuUsageEntityDto::getCpuUsage).average()
                        .orElseThrow(NullPointerException::new);
                resultDtoList.add(UsageResultVO.builder().avg(avgResult).hour(integer).build());
            });
        } else {
            cpuUsageEntityDtoList.forEach(result -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result.getCreatedDate());
                int day = calendar.get(Calendar.DATE);
                mapHours.computeIfAbsent(day, k -> new ArrayList<>()).add(result);
            });
            mapHours.forEach((integer, testEntities1) -> {
                double avgResult = testEntities1.stream().mapToDouble(CpuUsageEntityDto::getCpuUsage).average()
                        .orElseThrow(NullPointerException::new);
                resultDtoList.add(UsageResultVO.builder().days(integer).avg(avgResult).build());
            });
        }
        return resultDtoList;

    }
}
