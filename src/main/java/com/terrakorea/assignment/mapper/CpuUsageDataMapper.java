package com.terrakorea.assignment.mapper;

import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.dto.CpuUsageEntityDto;
import com.terrakorea.assignment.monitoring.UsageResultVO;
import com.terrakorea.assignment.vo.CpuUsageDayResponse;
import com.terrakorea.assignment.vo.CpuUsageHourResponse;
import com.terrakorea.assignment.vo.CpuUsageMinuteResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Component
public class CpuUsageDataMapper {


    public CpuUsageEntityDto entityToDto(CpuUsageEntity cpuUsageEntity) {
        return CpuUsageEntityDto.builder()
                .id(cpuUsageEntity.getId())
                .cpuUsage(cpuUsageEntity.getCpuUsage())
                .createdTime(cpuUsageEntity.getCreatedTime())
                .createdDate(cpuUsageEntity.getCreatedDate())
                .build();
    }


    public CpuUsageMinuteResponse dtoToCpuUsageMinuteResponse(CpuUsageEntityDto cpuUsageEntityDto) {
        return CpuUsageMinuteResponse.builder()
                .cpuUsage(roundValue(cpuUsageEntityDto.getCpuUsage()))
                .date(cpuUsageEntityDto.getCreatedDate())
                .time(cpuUsageEntityDto.getCreatedTime())
                .build();
    }

    public CpuUsageHourResponse dtoToCpuUsageHourResponse(Date searchDate, List<UsageResultVO> usageResultVO,
                                                          Double maxValue, Double minValue) {
        return new CpuUsageHourResponse(searchDate, roundValue(minValue), roundValue(maxValue), usageResultVO);
    }

    public CpuUsageDayResponse dtoToCpuUsageDayResponse(List<UsageResultVO> usageResultVO, Double maxValue, Double minValue) {
        return new CpuUsageDayResponse(roundValue(minValue), roundValue(maxValue), usageResultVO);
    }

    private Double roundValue(Double value) {
        return BigDecimal.valueOf(value)
                .setScale(4, RoundingMode.HALF_UP).doubleValue();
    }
}
