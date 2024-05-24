package com.terrakorea.assignment;

import com.terrakorea.assignment.monitoring.CalendarType;
import com.terrakorea.assignment.testcode.*;
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
    private TestCalcHandler testCalcHandler;

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
//    @DisplayName("분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.")
    @Deprecated
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
        Assertions.assertEquals(300, testEntities.size());

        // print
//        testEntities.forEach(result -> {
//            System.out.printf("cpu -> %s, date -> %s, time -> %s\n", result.getCpuUsage(),
//                    result.getCreatedDate(), result.getCreatedTime());
//        });

    }

    @Test
//    @DisplayName("시 단위 조회: 지정한 날짜의 시 단위 CPU 최소/최대/평균 사용률을 조회합니다.(X)")
    @Deprecated
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
//        newCal2.set(2024, Calendar.JUNE, 23, 0, 0, 00);
        newCal2.set(2024, Calendar.JUNE, 22, 23, 59, 59);
        Date endSearchDate = newCal2.getTime();

        // when
        List<TestEntity> testEntities = testRepository.findAll();
        List<TestEntity> byCreatedDate = testRepository.findByCreatedDateIsBetween(startSearchDate, endSearchDate);


        List<TestResultDto> resultDtoList = testCalcHandler.avgCpuUsage(byCreatedDate, CalendarType.HOUR);
        Double minValue = testCalcHandler.minCpuUsage(byCreatedDate, CalendarType.HOUR);
        Double maxValue = testCalcHandler.maxCpuUsage(byCreatedDate, CalendarType.HOUR);

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
//    @DisplayName("일 단위 조회: 지정한 날짜 구간의 일 단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    @Deprecated
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
        newCal2.set(2024, Calendar.JUNE, 21, 23, 59, 0);
        Date endSearchDate = newCal2.getTime();

        // when
        List<TestEntity> testEntities = testRepository.findByCreatedDateIsBetween(startSearchDate, endSearchDate);

        List<TestResultDto> resultDtos = testCalcHandler.avgCpuUsage(testEntities, CalendarType.DAY);
        Double minValue = testCalcHandler.minCpuUsage(testEntities, CalendarType.DAY);
        Double maxValue = testCalcHandler.maxCpuUsage(testEntities, CalendarType.DAY);

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
        Assertions.assertEquals(7, resultDtos.size());
        Assertions.assertEquals(min, minValue);
        Assertions.assertEquals(max, maxValue);

        // print
        resultDtos.forEach(result -> {
            System.out.printf("cpu Avg -> %s, days -> %s\n", result.getAvg(),
                    result.getDays());
        });
    }

    @Test
    @DisplayName("분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.")
    public void retrieveCpuUsagePerMinute2() {
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

        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal1.set(2024, Calendar.JUNE, 21, 0, 0, 0);
        Calendar newCal2 = Calendar.getInstance();
        newCal2.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal2.set(2024, Calendar.JUNE, 21, 1, 0, 0);

        Calendar newCal3 = Calendar.getInstance();
        newCal3.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal3.set(2024, Calendar.JUNE, 21, 0, 0, 0);
        Date startDate = newCal1.getTime();
        Date endDate = newCal2.getTime();
        Date findDate = newCal3.getTime();

        // when
        List<TestEntity> createdDate = testRepository.findByCreatedDateAndCreatedTimeIsBetween(findDate, startDate, endDate);

        // then
        Assertions.assertEquals(60, createdDate.size()); // 60 0시~1시까지의 분 단위

        // print

        createdDate.forEach(testEntity -> {
            System.out.printf("cpu ->%s, date -> %s, time-> %s\n", testEntity.getCpuUsage(),
                    testEntity.getCreatedDate(), testEntity.getCreatedTime());
        });
    }

    @Test
    @DisplayName("시 단위 조회: 지정한 날짜의 시 단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    public void minMaxAvgCpuUsagePerHour2() {
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
        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal1.set(2024, Calendar.JUNE, 21, 0, 0, 00);
        Date searchDate = newCal1.getTime();


        // when
        List<TestEntity> createdDate = testRepository.findByCreatedDate(searchDate);
        List<TestResultDto> testPerHourResultDtos = testCalcHandler.avgCpuUsage(createdDate, CalendarType.HOUR);
        Double maxValue = testCalcHandler.maxCpuUsage(createdDate, CalendarType.HOUR);
        Double minValue = testCalcHandler.minCpuUsage(createdDate, CalendarType.HOUR);

        // then
        Assertions.assertEquals(1440, createdDate.size());
        Assertions.assertEquals(24, testPerHourResultDtos.size()); // 시간 당 평균값이므로, 24

        // print
        System.out.println("minValue = " + minValue);
        System.out.println("maxValue = " + maxValue);
        System.out.println("testPerHourResultDtos.size() = " + testPerHourResultDtos.size());

        testPerHourResultDtos.stream().forEach(testPerHourResultDto -> {
            System.out.printf("hour -> %s,avg -> %s\n", testPerHourResultDto.getHour(), testPerHourResultDto.getAvg());
        });
    }

    @Test
    @DisplayName("일 단위의 평균값 계산")
    public void minMaxAvgCpuUsagePerDay2() {
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

        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.Seoul));
        newCal1.set(2024, Calendar.JUNE, 18, 0, 0, 00);
        Date searchDate = newCal1.getTime();

        // when
        List<TestEntity> createdDate = testRepository.findByCreatedDate(searchDate);
        List<TestResultDto> testResultDtos = testCalcHandler.avgCpuUsage(createdDate, CalendarType.DAY);

        // then
        Assertions.assertEquals(1440, createdDate.size());
        Assertions.assertEquals(1, testResultDtos.size()); // 일 당 평균값이므로 1


    }
}
