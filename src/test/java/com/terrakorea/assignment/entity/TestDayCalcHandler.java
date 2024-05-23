package com.terrakorea.assignment.entity;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TestDayCalcHandler {


    public Double maxCpuUsagePerDay(List<TestEntity> testEntities) {
        List<TestPerDayResultDto> testResultsPerDay = this.getTestResultsPerDay(testEntities);
        return testResultsPerDay.stream().mapToDouble(TestPerDayResultDto::getAvg).max()
                .orElseThrow(NullPointerException::new);
    }

    public Double minCpuUsagePerDay(List<TestEntity> testEntities) {
        List<TestPerDayResultDto> testResultsPerDay = this.getTestResultsPerDay(testEntities);
        return testResultsPerDay.stream()
                .mapToDouble(TestPerDayResultDto::getAvg).min().orElseThrow(NullPointerException::new);
    }

    public List<TestPerDayResultDto> avgCpuUsagePerDay(List<TestEntity> testEntities) {
        return this.getTestResultsPerDay(testEntities);
    }

    private List<TestPerDayResultDto> getTestResultsPerDay(List<TestEntity> testEntities) {
        Map<Integer, List<TestEntity>> mapHours = new HashMap<>();
        List<TestPerDayResultDto> resultDtoList = new ArrayList<>();
        testEntities.forEach(result -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(result.getCreatedDate());
            int day = calendar.get(Calendar.DATE);
            mapHours.computeIfAbsent(day, k -> new ArrayList<>()).add(result);
        });

        mapHours.forEach((integer, testEntities1) -> {
            double avgResult = testEntities1.stream().mapToDouble(TestEntity::getCpuUsage).average()
                    .orElseThrow(NullPointerException::new);
            resultDtoList.add(new TestPerDayResultDto(integer, avgResult));
        });
        return resultDtoList;
    }


}
