package com.terrakorea.assignment.service;


import com.terrakorea.assignment.vo.*;

import java.util.List;

public interface CpuUsageService {

    List<CpuUsageMinuteResponse> findCpuUsageMinute(CpuUsageRequest cpuUsageRequest);

    List<CpuUsageHourResponse> findCpuUsageHour(CpuUsageRequest cpuUsageRequest);

    CpuUsageDayResponse findCpuUsageDay(CpuUsageRangeRequest cpuUsageRangeRequest);
}
