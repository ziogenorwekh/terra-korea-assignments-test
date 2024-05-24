package com.terrakorea.assignment.service;


import com.terrakorea.assignment.vo.CpuUsageDayResponse;
import com.terrakorea.assignment.vo.CpuUsageHourResponse;
import com.terrakorea.assignment.vo.CpuUsageMinuteResponse;
import com.terrakorea.assignment.vo.CpuUsageRequest;

import java.util.List;

public interface CpuUsageService {

    List<CpuUsageMinuteResponse> findCpuUsageMinute(CpuUsageRequest cpuUsageRequest);

    List<CpuUsageHourResponse> findCpuUsageHour(CpuUsageRequest cpuUsageRequest);

    CpuUsageDayResponse findCpuUsageDay();
}
