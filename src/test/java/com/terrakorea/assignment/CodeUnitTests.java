package com.terrakorea.assignment;

import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.exception.InvalidateCalendarException;
import com.terrakorea.assignment.monitoring.CustomTimer;
import com.terrakorea.assignment.repository.CpuUsageRepository;
import com.terrakorea.assignment.service.CpuUsageService;
import com.terrakorea.assignment.vo.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SpringBootTest
@TestPropertySource(value = "/application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// 유닛 테스트: 서비스 계층과 데이터베이스 계층의 유닛 테스트를 작성하세요.
public class CodeUnitTests {

    @Autowired
    private CpuUsageRepository cpuUsageRepository;

    @Autowired
    private CpuUsageService cpuUsageService;

    private static CpuUsageRequest minuteCpuUsageRequest;

    private static CpuUsageRequest hourCpuUsageRequest;

    private static CpuUsageRangeRequest dayCpuUsageRangeRequest;

    private final int aDay = 60 * 24;

    @BeforeAll
    public void setUp() {
        // given(common) 60일의 분 단위 데이터 저장 2월부터 5월 말까지
        for (int i = 0; i < aDay * 120; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
            calendar.set(2024, Calendar.FEBRUARY, 1, 0, i, 00);
            Date date = calendar.getTime();
            double random;
            if (i % 7 == 0) {
                random = ((int) (Math.random() * 5880) + 3000) / 100.0; // 48.8% 이내의 사용량 저장
            } else {
                random = ((int) (Math.random() * 1880) + 1) / 100.0; // 48.8% 이내의 사용량 저장
            }
            CpuUsageEntity cpuUsageEntity = new CpuUsageEntity(random, date, date);
            cpuUsageRepository.save(cpuUsageEntity);
        }
        minuteCpuUsageRequest = new CpuUsageRequest(2024, 5, 20, 1);
        hourCpuUsageRequest = new CpuUsageRequest(2024, 5, 20, 0);
        dayCpuUsageRangeRequest = new CpuUsageRangeRequest(2024, 3, 10,
                2024, 4, 24);
    }

    @AfterAll
    public void tearDown() {
        cpuUsageRepository.deleteAll();
    }

    @Test
    @DisplayName("분 단위 데이터 검색 및 최근 데이터(일주일 이내)만 조회 가능한지")
    public void searchMinuteData() {

        // when
        List<CpuUsageMinuteResponse> minuteResponses =
                cpuUsageService.findCpuUsageMinute(minuteCpuUsageRequest);

        // then 해당 시간의 데이터는 0~59분의 60개가 있어야 한다.
        Assertions.assertEquals(60, minuteResponses.size());

        // print 정상적으로 조회되었는지 확인
//        minuteResponses.forEach(System.out::println);

        // given 일주일을 넘긴 데이터 조회 할 경우
        minuteCpuUsageRequest = new CpuUsageRequest(2024, 5, 10, 1);

        // when, then
        InvalidateCalendarException invalidateCalendarException1 = Assertions.assertThrows(InvalidateCalendarException.class, () -> {
            cpuUsageService.findCpuUsageMinute(minuteCpuUsageRequest);
        });
        Assertions.assertEquals("You can only access data from last 7 days.", invalidateCalendarException1.getMessage());

        // given 오늘 날짜를 초과하여 조회
        minuteCpuUsageRequest = new CpuUsageRequest(2024, 6, 10, 1);

        // when, then
        InvalidateCalendarException invalidateCalendarException = Assertions.assertThrows(InvalidateCalendarException.class, () -> {
            cpuUsageService.findCpuUsageMinute(minuteCpuUsageRequest);
        });
        Assertions.assertEquals("Unable to query data beyond today.", invalidateCalendarException.getMessage());
    }

    @Test
    @DisplayName("시 단위 데이터 검색 및 최근 데이터(한 달이내)만 조회 가능한지")
    public void searchHourData() {

        // when
        CpuUsageHourResponse cpuUsageHour = cpuUsageService.findCpuUsageHour(hourCpuUsageRequest);

        // then
        Assertions.assertEquals(24, cpuUsageHour.getHourResponses().size());

        // print
        System.out.println(cpuUsageHour);

        // given
        hourCpuUsageRequest = new CpuUsageRequest(2024, 2, 10, 1);

        // when
        InvalidateCalendarException invalidateCalendarException =
                Assertions.assertThrows(InvalidateCalendarException.class, () -> {
                    cpuUsageService.findCpuUsageHour(hourCpuUsageRequest);
                });

        // then
        Assertions.assertEquals("You can only access data from last 3 months.",
                invalidateCalendarException.getMessage());

        // given
        hourCpuUsageRequest = new CpuUsageRequest(2024, 6, 10, 0);

        // when
        InvalidateCalendarException invalidateCalendarException2 =
                Assertions.assertThrows(InvalidateCalendarException.class, () -> {
                    cpuUsageService.findCpuUsageHour(hourCpuUsageRequest);
                });

        // then
        Assertions.assertEquals("Unable to query data beyond today.",
                invalidateCalendarException2.getMessage());
    }

    @Test
    @DisplayName("일 단위 데이터 검색 및 최근 데이터(일 년이내)만 조회 가능한지")
    public void searchDayData() {

        // when
        CpuUsageDayResponse cpuUsageDay = cpuUsageService.findCpuUsageDay(dayCpuUsageRangeRequest);

        // then
        Assertions.assertEquals(45, cpuUsageDay.getDayResponses().size());

        // print
        System.out.println(cpuUsageDay);

        // given
        dayCpuUsageRangeRequest = new CpuUsageRangeRequest(2023, 3, 10,
                2024, 4, 24);

        // when
        InvalidateCalendarException invalidateCalendarException = Assertions.assertThrows(InvalidateCalendarException.class,
                () -> {
                    cpuUsageService.findCpuUsageDay(dayCpuUsageRangeRequest);
                });

        // then
        Assertions.assertEquals("You can only access data from last 1 years.",
                invalidateCalendarException.getMessage());

    }
}
