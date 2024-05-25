package com.terrakorea.assignment.mapper;

import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.dto.CpuUsageEntityDto;
import com.terrakorea.assignment.monitoring.UsageResultVO;
import com.terrakorea.assignment.vo.CpuUsageDayResponse;
import com.terrakorea.assignment.vo.CpuUsageHourResponse;
import com.terrakorea.assignment.vo.CpuUsageMinuteResponse;
import org.springframework.stereotype.Component;

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
                .cpuUsage(cpuUsageEntityDto.getCpuUsage())
                .date(cpuUsageEntityDto.getCreatedDate())
                .time(cpuUsageEntityDto.getCreatedTime())
                .build();
    }

    public CpuUsageHourResponse dtoToCpuUsageHourResponse(Date searchDate, List<UsageResultVO> usageResultVO,
                                                          Double maxValue, Double minValue) {
        return new CpuUsageHourResponse(searchDate, minValue, maxValue, usageResultVO);
    }

    public CpuUsageDayResponse dtoToCpuUsageDayResponse(List<UsageResultVO> usageResultVO, Double maxValue, Double minValue) {
        return new CpuUsageDayResponse(minValue, maxValue, usageResultVO);
    }
}
