package com.terrakorea.assignment.service;

import com.terrakorea.assignment.dto.CpuUsageEntityDto;
import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.exception.InvalidateCalendarException;
import com.terrakorea.assignment.mapper.CpuUsageDataMapper;
import com.terrakorea.assignment.monitoring.CalendarType;
import com.terrakorea.assignment.monitoring.CpuUsageHandler;
import com.terrakorea.assignment.monitoring.CustomTimer;
import com.terrakorea.assignment.monitoring.UsageResultVO;
import com.terrakorea.assignment.repository.CpuUsageRepository;
import com.terrakorea.assignment.vo.CpuUsageDayResponse;
import com.terrakorea.assignment.vo.CpuUsageHourResponse;
import com.terrakorea.assignment.vo.CpuUsageMinuteResponse;
import com.terrakorea.assignment.vo.CpuUsageRequest;
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

        Date findDate = findCalendar.getTime();


        Calendar endCalendar = (Calendar) findCalendar.clone();
        endCalendar.add(Calendar.HOUR_OF_DAY, 1);

        Date startDate = findCalendar.getTime();
        Date endDate = endCalendar.getTime();

        List<CpuUsageEntity> cpuUsageEntities = cpuUsageRepository.
                findByCreatedDateAndCreatedTimeIsBetween(findDate, startDate, endDate);

        List<CpuUsageEntityDto> mappingDto = cpuUsageEntities.stream()
                .map(cpuUsageDataMapper::entityToDto).toList();

        return mappingDto.stream().map(cpuUsageDataMapper::dtoToMinuteResponse).toList();
    }

    @Override
    public List<CpuUsageHourResponse> findCpuUsageHour(CpuUsageRequest cpuUsageRequest) {

        // 현재 시간 변환
        Calendar findCalendar = Calendar.getInstance();
        findCalendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
        findCalendar.set(cpuUsageRequest.getYear(), cpuUsageRequest.getMonth() - 1, cpuUsageRequest.getDay(),
                0, 0, 0);
        validCalendarRecentDataWithinThreeMonths(findCalendar);

        List<CpuUsageEntityDto> cpuUsageEntities = cpuUsageRepository.findByCreatedDate(findCalendar.getTime())
                .stream().map(cpuUsageDataMapper::entityToDto).toList();

        List<UsageResultVO> resultVOList = cpuUsageHandler.avgCpuUsage(cpuUsageEntities, CalendarType.HOUR);
        Double maxValue = cpuUsageHandler.maxCpuUsage(cpuUsageEntities, CalendarType.HOUR);
        Double minValue = cpuUsageHandler.minCpuUsage(cpuUsageEntities, CalendarType.HOUR);

        List<CpuUsageHourResponse> cpuUsageHourResponses = new ArrayList<>();
        // 여기 부터 코드 짜기

        return null;
    }

    @Override
    public CpuUsageDayResponse findCpuUsageDay() {
        return null;
    }


    // 최근 일주일 이내 데이터만 검색 허용
    private void validCalendarRecentDataWithinWeeks(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));

        Calendar sevenDaysAgo = (Calendar) now.clone();
        sevenDaysAgo.add(Calendar.DAY_OF_MONTH, -7);

        if (calendar.before(sevenDaysAgo) || calendar.after(now)) {
            throw new InvalidateCalendarException("You can only access data from last 7 days.");
        }
    }

    // 최근 3개월 이내 데이터만 검색 허용
    private void validCalendarRecentDataWithinThreeMonths(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));

        Calendar threeMonthsAgo = (Calendar) now.clone();
        threeMonthsAgo.add(Calendar.MONTH, -3);

        if (calendar.before(threeMonthsAgo) || calendar.after(now)) {
            throw new InvalidateCalendarException("You can only access data from last 3 months.");
        }
    }
}
