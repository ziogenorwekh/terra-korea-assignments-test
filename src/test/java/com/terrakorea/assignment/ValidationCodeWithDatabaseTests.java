package com.terrakorea.assignment;

import com.terrakorea.assignment.entity.*;
import com.terrakorea.assignment.monitoring.CustomTimer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@SpringBootTest
@TestPropertySource(value = "/application-test.yml")
public class ValidationCodeWithDatabaseTests {

    private CustomTimer timer = new CustomTimer();

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestHourCalcHandler testHourCalcHandler;

    @Autowired
    private TestDayCalcHandler testDayCalcHandler;

    private final int testSaveMinuteValue = 60 * 24;

    @AfterEach
    public void tearDown() {
        testRepository.deleteAll();
    }

    @Test
    @DisplayName("cpu usage 데이터베이스 저장 및 조회")
    public void saveCpuUsagePerSecond() {

        // given
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();

        for (int i = 0; i < 5; i++) {

            Double cpuUsage = cpu.getSystemCpuLoad(timer.aSecond);
            LocalDateTime now = LocalDateTime.now();
            Date date = Date.from(now.atZone(ZoneId.of(timer.Seoul)).toInstant());
            TestEntity testEntity = new TestEntity(cpuUsage, date, date);
            testRepository.save(testEntity);
        }

        // when
        List<TestEntity> testEntities = testRepository.findAll();

        // then
        Assertions.assertEquals(5, testEntities.size());

        // print
//        testEntities.forEach(result -> System.out.printf(" cpu usage -> %s , createdDate -> %s , " +
//                "createdTime -> %s \n", result.getCpuUsage(), result.getCreatedDate(), result.getCreatedTime()));
    }

    @Test
    @DisplayName("분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.")
    public void retrieveCpuUsagePerMinute() {

        // given
        for (int i = 0; i < 300; i++) { // 300분
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
            calendar.set(2024, Calendar.JUNE, 23, 6, i, 00);
            Date date = calendar.getTime();
            double random = ((int) (Math.random() * 420) + 1) / 100.0; // 사용률 42% 이내 랜덤값
            TestEntity testEntity = new TestEntity(random, date, date);
            testRepository.save(testEntity);
        }

        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal1.set(2024, Calendar.JUNE, 23, 8, 0, 0);
        Date startDateTime = newCal1.getTime();
        Calendar newCal2 = Calendar.getInstance();
        newCal2.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal2.set(2024, Calendar.JUNE, 23, 10, 0, 0);
        Date endDateTime = newCal2.getTime();

        // when
        List<TestEntity> testEntities = testRepository.findByCreatedDateIsBetween(startDateTime, endDateTime);

        // then
        Assertions.assertEquals(120, testEntities.size());

        // print
//        testEntities.forEach(result -> {
//            System.out.printf("cpu -> %s, date -> %s, time -> %s\n", result.getCpuUsage(),
//                    result.getCreatedDate(), result.getCreatedTime());
//        });

    }

    @Test
    @DisplayName("시 단위 조회: 지정한 날짜의 시 단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    public void minMaxAvgCpuUsagePerHourOfDay() {

        // given (3일치 데이터 기록) 20 ~ 22
        for (int i = 0; i < testSaveMinuteValue * 3; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
            calendar.set(2024, Calendar.JUNE, 20, 1, i, 00);
            Date date = calendar.getTime();
            double random = ((int) (Math.random() * 8880) + 1) / 100.0;
            TestEntity testEntity = new TestEntity(random, date, date);
            testRepository.save(testEntity);
        }

        Double max = 0.0;
        Double min = 100.0;

        // 22일 시 단위 데이터 조회
        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal1.set(2024, Calendar.JUNE, 22, 0, 0, 00);
        Date startSearchDate = newCal1.getTime();

        Calendar newCal2 = Calendar.getInstance();
        newCal2.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal2.set(2024, Calendar.JUNE, 23, 0, 0, 00);
        Date endSearchDate = newCal2.getTime();

        // when
        List<TestEntity> testEntities = testRepository.findAll();
        List<TestEntity> byCreatedDate = testRepository.findByCreatedDateIsBetween(startSearchDate, endSearchDate);

        List<TestPerHourResultDto> resultDtoList = testHourCalcHandler.avgCpuUsagePerHour(byCreatedDate);
        Double minValue = testHourCalcHandler.minCpuUsagePerHour(byCreatedDate);
        Double maxValue = testHourCalcHandler.maxCpuUsagePerHour(byCreatedDate);

        // then
        Assertions.assertEquals(testSaveMinuteValue * 3, testEntities.size());
        Assertions.assertEquals(60 * 24, byCreatedDate.size());

        for (int i = 0; i < resultDtoList.size(); i++) {
            Double v = resultDtoList.get(i).getAvg();
            if (v < min) {
                min = v;
            }
            if (v > max) {
                max = v;
            }
        }

        Assertions.assertEquals(min, minValue);
        Assertions.assertEquals(max, maxValue);

//        print
//        resultDtoList.forEach(result -> {
//            System.out.printf("cpu -> %s, hour -> %s\n", result.getAvg(),
//                    result.getHour());
//        });
    }

    @Test
    @DisplayName("일 단위 조회: 지정한 날짜 구간의 일 단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    public void minMaxAvgCpuUsagePerDay() {

        // given (10일부터 24일까지 분 단위 랜덤 값 저장)
        for (int i = 0; i < testSaveMinuteValue * 10; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
            calendar.set(2024, Calendar.JUNE, 15, 0, i, 00);
            Date date = calendar.getTime();
            double random = ((int) (Math.random() * 8880) + 1) / 100.0;
            TestEntity testEntity = new TestEntity(random, date, date);
            testRepository.save(testEntity);
        }

        Double max = 0.0;
        Double min = 100.0;

        // 15 일 ~ 22 사이 데이터 조회
        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal1.set(2024, Calendar.JUNE, 15, 0, 0, 00);
        Date startSearchDate = newCal1.getTime();

        Calendar newCal2 = Calendar.getInstance();
        newCal2.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal2.set(2024, Calendar.JUNE, 22, 0, 0, 00);
        Date endSearchDate = newCal2.getTime();

        // when
        List<TestEntity> testEntities = testRepository.findByCreatedDateIsBetween(startSearchDate, endSearchDate);

        List<TestPerDayResultDto> resultDtos = testDayCalcHandler.avgCpuUsagePerDay(testEntities);
        Double minValue = testDayCalcHandler.minCpuUsagePerDay(testEntities);
        Double maxValue = testDayCalcHandler.maxCpuUsagePerDay(testEntities);

        // then

        for (int i = 0; i < resultDtos.size(); i++) {
            Double v = resultDtos.get(i).getAvg();
            if (v < min) {
                min = v;
            }
            if (v > max) {
                max = v;
            }
        }

        Assertions.assertEquals(min, minValue);
        Assertions.assertEquals(max, maxValue);

        // print
//        resultDtos.forEach(result -> {
//            System.out.printf("min -> %s, max -> %s, cpu Avg -> %s, days -> %s\n", minValue, maxValue, result.getAvg(),
//                    result.getDays());
//        });
    }


}
