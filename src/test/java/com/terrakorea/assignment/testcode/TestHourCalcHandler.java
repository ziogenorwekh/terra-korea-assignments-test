package com.terrakorea.assignment.testcode;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TestHourCalcHandler {


    // 지정한 날짜의 시 단위 최소/최대/평균 사용률
    public List<TestPerHourResultDto> avgCpuUsagePerHour(List<TestEntity> testEntities) {
        return this.getTestResultsPerHour(testEntities);
    }

    public Double minCpuUsagePerHour(List<TestEntity> testEntities) {
        List<TestPerHourResultDto> testResults = this.getTestResultsPerHour(testEntities);
        return testResults.stream()
                .mapToDouble(TestPerHourResultDto::getAvg)
                .min().orElseThrow(NullPointerException::new);
    }

    public Double maxCpuUsagePerHour(List<TestEntity> testEntities) {
        List<TestPerHourResultDto> testResults = this.getTestResultsPerHour(testEntities);
        return testResults.stream().mapToDouble(TestPerHourResultDto::getAvg)
                .max().orElseThrow(NullPointerException::new);
    }

    private List<TestPerHourResultDto> getTestResultsPerHour(List<TestEntity> testEntities) {
        Map<Integer, List<TestEntity>> mapHours = new HashMap<>();
        List<TestPerHourResultDto> resultDtoList = new ArrayList<>();
        // 0시 0분부터 23시 59분까지 있음
        testEntities.forEach(result -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(result.getCreatedTime());

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            mapHours.computeIfAbsent(hour, k -> new ArrayList<>()).add(result);
            // 처음 설정된 값이 0시 0분이니까 0시 59분까지의 값을 모두 더해서 평균 산출
//            System.out.printf("cpu usage -> %s, date -> %s, minute -> %s\n", result.getCpuUsage(),
//                    result.getCreatedDate(), calendar.get(Calendar.MINUTE));
        });

        mapHours.forEach((integer, testEntities1) -> {
            double avgResult = testEntities1.stream().mapToDouble(TestEntity::getCpuUsage).average()
                    .orElseThrow(NullPointerException::new);
            resultDtoList.add(new TestPerHourResultDto(integer, avgResult));
        });
        return resultDtoList;
    }

}
