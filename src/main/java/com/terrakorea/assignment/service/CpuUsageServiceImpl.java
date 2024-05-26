package com.terrakorea.assignment.service;

import com.terrakorea.assignment.dto.CpuUsageEntityDto;
import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.exception.DataNotFoundException;
import com.terrakorea.assignment.exception.InvalidateCalendarException;
import com.terrakorea.assignment.mapper.CpuUsageDataMapper;
import com.terrakorea.assignment.monitoring.CalendarType;
import com.terrakorea.assignment.monitoring.CpuUsageHandler;
import com.terrakorea.assignment.monitoring.CustomTimer;
import com.terrakorea.assignment.monitoring.UsageResultVO;
import com.terrakorea.assignment.repository.CpuUsageRepository;
import com.terrakorea.assignment.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
public class CpuUsageServiceImpl implements CpuUsageService {

    private final CpuUsageRepository cpuUsageRepository;
    private final CpuUsageDataMapper cpuUsageDataMapper;
    private final CpuUsageHandler cpuUsageHandler;

    @Autowired
    public CpuUsageServiceImpl(CpuUsageRepository cpuUsageRepository,
                               CpuUsageDataMapper cpuUsageDataMapper, CpuUsageHandler cpuUsageHandler) {
        this.cpuUsageRepository = cpuUsageRepository;
        this.cpuUsageDataMapper = cpuUsageDataMapper;
        this.cpuUsageHandler = cpuUsageHandler;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CpuUsageMinuteResponse> findCpuUsageMinute(CpuUsageRequest cpuUsageRequest) {

        Calendar findCalendar = Calendar.getInstance();
        findCalendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
        findCalendar.set(cpuUsageRequest.getYear(), cpuUsageRequest.getMonth() - 1, cpuUsageRequest.getDay(),
                cpuUsageRequest.getHour(), 0, 0);

        validCalendarRecentDataWithinWeeks(findCalendar);

        Calendar searchCalendar = Calendar.getInstance();
        searchCalendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
        searchCalendar.set(cpuUsageRequest.getYear(), cpuUsageRequest.getMonth() - 1, cpuUsageRequest.getDay());

        Calendar endCalendar = (Calendar) findCalendar.clone();
        endCalendar.add(Calendar.HOUR_OF_DAY, 1);

        Date searchDate = searchCalendar.getTime();
        Date startDate = findCalendar.getTime();
        Date endDate = endCalendar.getTime();

        List<CpuUsageEntity> cpuUsageEntities = cpuUsageRepository.
                findByCreatedDateAndCreatedTimeIsBetween(searchDate, startDate, endDate);

        List<CpuUsageEntityDto> mappingDto = cpuUsageEntities.stream()
                .map(cpuUsageDataMapper::entityToDto).toList();

        isNotFoundData(mappingDto);

        return mappingDto.stream().map(cpuUsageDataMapper::dtoToCpuUsageMinuteResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CpuUsageHourResponse findCpuUsageHour(CpuUsageRequest cpuUsageRequest) {

        // 현재 시간 변환
        Calendar findCalendar = Calendar.getInstance();
        findCalendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
        findCalendar.set(cpuUsageRequest.getYear(), cpuUsageRequest.getMonth() - 1, cpuUsageRequest.getDay(),
                0, 0, 0);

        validCalendarRecentDataWithinThreeMonths(findCalendar);

        List<CpuUsageEntityDto> cpuUsageEntities = cpuUsageRepository.findByCreatedDate(findCalendar.getTime())
                .stream().map(cpuUsageDataMapper::entityToDto).toList();

        isNotFoundData(cpuUsageEntities);

        List<UsageResultVO> resultVOList = cpuUsageHandler.avgCpuUsage(cpuUsageEntities, CalendarType.HOUR);
        Double maxValue = cpuUsageHandler.maxCpuUsage(resultVOList);
        Double minValue = cpuUsageHandler.minCpuUsage(resultVOList);

        CpuUsageHourResponse cpuUsageHourResponse = cpuUsageDataMapper
                .dtoToCpuUsageHourResponse(findCalendar.getTime(), resultVOList, maxValue, minValue);


        return cpuUsageHourResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public CpuUsageDayResponse findCpuUsageDay(CpuUsageRangeRequest cpuUsageRangeRequest) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
        startCalendar.set(cpuUsageRangeRequest.getFromYear(), cpuUsageRangeRequest.getFromMonth() - 1,
                cpuUsageRangeRequest.getFromDay(), 0, 0, 0);
        Date startDate = startCalendar.getTime();

        validCalendarRecentDataWithinAYears(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));

        endCalendar.set(cpuUsageRangeRequest.getToYear(), cpuUsageRangeRequest.getToMonth() - 1,
                cpuUsageRangeRequest.getToDay(), 23, 59, 59);
        Date endDate = endCalendar.getTime();

        List<CpuUsageEntityDto> cpuUsageEntities = cpuUsageRepository.findByCreatedDateIsBetween(startDate, endDate)
                .stream().map(cpuUsageDataMapper::entityToDto).toList();

        isNotFoundData(cpuUsageEntities);

        List<UsageResultVO> usageResultVOS = cpuUsageHandler.avgCpuUsage(cpuUsageEntities, CalendarType.DAY);
        Double maxValue = cpuUsageHandler.maxCpuUsage(usageResultVOS);
        Double minValue = cpuUsageHandler.minCpuUsage(usageResultVOS);

        usageResultVOS.sort(Comparator.comparing(UsageResultVO::getDate));

        CpuUsageDayResponse cpuUsageDayResponse = cpuUsageDataMapper.dtoToCpuUsageDayResponse(usageResultVOS, maxValue, minValue);

        return cpuUsageDayResponse;
    }


    // 최근 일주일 이내 데이터만 검색 허용
    private void validCalendarRecentDataWithinWeeks(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));

        Calendar sevenDaysAgo = (Calendar) now.clone();
        sevenDaysAgo.add(Calendar.DAY_OF_MONTH, -7);

        if (calendar.before(sevenDaysAgo)) {
            throw new InvalidateCalendarException("You can only access data from last 7 days.");
        } else if (calendar.after(now)) {
            throw new InvalidateCalendarException("Unable to query data beyond today.");
        }
    }

    // 최근 3개월 이내 데이터만 검색 허용
    private void validCalendarRecentDataWithinThreeMonths(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));

        Calendar threeMonthsAgo = (Calendar) now.clone();
        threeMonthsAgo.add(Calendar.MONTH, -3);

        if (calendar.before(threeMonthsAgo)) {
            throw new InvalidateCalendarException("You can only access data from last 3 months.");
        } else if (calendar.after(now)) {
            throw new InvalidateCalendarException("Unable to query data beyond today.");
        }
    }

    // 최근 1년 이내 데이터만 검색 허용
    private void validCalendarRecentDataWithinAYears(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));

        Calendar aYearsAgo = (Calendar) now.clone();
        aYearsAgo.add(Calendar.YEAR, -1);

        if (calendar.before(aYearsAgo)) {
            throw new InvalidateCalendarException("You can only access data from last 1 years.");
        } else if (calendar.after(now)) {
            throw new InvalidateCalendarException("Unable to query data beyond today.");
        }
    }

    // 조회 데이터가 없을 경우
    private void isNotFoundData(List<CpuUsageEntityDto> cpuUsageEntities) {
        if (cpuUsageEntities.isEmpty()) {
            throw new DataNotFoundException("No CPU usage data found.");
        }
    }
}
