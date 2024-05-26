package com.terrakorea.assignment.monitoring;

import com.terrakorea.assignment.dto.CpuUsageEntityDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class CpuUsageHandler {

    public Double maxCpuUsage(List<UsageResultVO> usageResultVOS) {
        return usageResultVOS.stream().mapToDouble(UsageResultVO::getAvg).max()
                .orElseThrow(NullPointerException::new);
    }

    public Double minCpuUsage(List<UsageResultVO> usageResultVOS) {
        return usageResultVOS.stream()
                .mapToDouble(UsageResultVO::getAvg)
                .min().orElseThrow(NullPointerException::new);
    }

    public List<UsageResultVO> avgCpuUsage(List<CpuUsageEntityDto> cpuUsageEntityDtoList, CalendarType calenderType) {
        return this.getTestResults(cpuUsageEntityDtoList, calenderType);
    }

    private List<UsageResultVO> getTestResults(List<CpuUsageEntityDto> cpuUsageEntityDtoList, CalendarType calenderType) {
        Map<Object, List<CpuUsageEntityDto>> mapHours = new HashMap<>();
        List<UsageResultVO> resultDtoList = new ArrayList<>();
        if (calenderType == CalendarType.HOUR) {
            cpuUsageEntityDtoList.forEach(result -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(result.getCreatedTime());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                mapHours.computeIfAbsent(hour, k -> new ArrayList<>()).add(result);
            });
            mapHours.forEach((integer, testEntities1) -> {
                double avgResult = testEntities1.stream().mapToDouble(CpuUsageEntityDto::getCpuUsage).average()
                        .orElseThrow(NullPointerException::new);
                resultDtoList.add(UsageResultVO.builder().avg(avgResult).hour((Integer) integer).build());
            });
        } else {
            cpuUsageEntityDtoList.forEach(result -> {
                mapHours.computeIfAbsent(result.getCreatedDate(), k -> new ArrayList<>()).add(result);
            });
            mapHours.forEach((integer, dtoList) -> {
                if (!dtoList.isEmpty()) {
                    double avgResult = dtoList.stream().mapToDouble(CpuUsageEntityDto::getCpuUsage).average()
                            .orElseThrow(NullPointerException::new);
                    resultDtoList.add(UsageResultVO.builder().date(dtoList.get(0).getCreatedDate())
                            .avg(avgResult).build());
                }
            });
        }
        return resultDtoList;
    }


//    private Double roundValue(Double value) {
//        return BigDecimal.valueOf(value)
//                .setScale(4, RoundingMode.HALF_UP).doubleValue();
//    }
}
