package com.terrakorea.assignment.service;


import com.terrakorea.assignment.vo.*;
import jakarta.validation.Valid;

import java.util.List;

public interface CpuUsageService {

    List<CpuUsageMinuteResponse> findCpuUsageMinute(@Valid CpuUsageRequest cpuUsageRequest);

    CpuUsageHourResponse findCpuUsageHour(@Valid CpuUsageRequest cpuUsageRequest);

    CpuUsageDayResponse findCpuUsageDay(@Valid CpuUsageRangeRequest cpuUsageRangeRequest);
}
