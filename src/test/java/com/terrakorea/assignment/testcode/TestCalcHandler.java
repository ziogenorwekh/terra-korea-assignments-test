package com.terrakorea.assignment.testcode;

import com.terrakorea.assignment.monitoring.CalendarType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TestCalcHandler {


    public Double maxCpuUsage(List<TestEntity> testEntities, CalendarType calendarType) {
        List<TestResultDto> testResultsPerDay = this.getTestResults(testEntities, calendarType);
        return testResultsPerDay.stream().mapToDouble(TestResultDto::getAvg).max()
                .orElseThrow(NullPointerException::new);
    }

    public Double minCpuUsage(List<TestEntity> testEntities, CalendarType calendarType) {
        List<TestResultDto> testResultsPerDay = this.getTestResults(testEntities, calendarType);
        return testResultsPerDay.stream()
                .mapToDouble(TestResultDto::getAvg)
                .min().orElseThrow(NullPointerException::new);
    }

    public List<TestResultDto> avgCpuUsage(List<TestEntity> testEntities, CalendarType calendarType) {
        return this.getTestResults(testEntities, calendarType);
    }

    private List<TestResultDto> getTestResults(List<TestEntity> testEntities, CalendarType calendarType) {
        Map<Integer, List<TestEntity>> mapHours = new HashMap<>();
        List<TestResultDto> resultDtoList = new ArrayList<>();
        if (calendarType == CalendarType.HOUR) {
            testEntities.forEach(result -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result.getCreatedTime());
                int day = calendar.get(Calendar.HOUR_OF_DAY);
                mapHours.computeIfAbsent(day, k -> new ArrayList<>()).add(result);
            });
            mapHours.forEach((integer, testEntities1) -> {
                double avgResult = testEntities1.stream().mapToDouble(TestEntity::getCpuUsage).average()
                        .orElseThrow(NullPointerException::new);
                resultDtoList.add(new TestResultDto.Builder().avg(avgResult).hour(integer).build());
            });
            return resultDtoList;
        } else {
            testEntities.forEach(result -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result.getCreatedDate());
                int day = calendar.get(Calendar.DATE);
                mapHours.computeIfAbsent(day, k -> new ArrayList<>()).add(result);
            });
            mapHours.forEach((integer, testEntities1) -> {
                double avgResult = testEntities1.stream().mapToDouble(TestEntity::getCpuUsage).average()
                        .orElseThrow(NullPointerException::new);
                resultDtoList.add(new TestResultDto.Builder().days(integer).avg(avgResult).build());
            });
        return resultDtoList;
        }

    }


}
