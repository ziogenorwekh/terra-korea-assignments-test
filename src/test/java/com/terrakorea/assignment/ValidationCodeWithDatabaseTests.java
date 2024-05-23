package com.terrakorea.assignment;

import com.terrakorea.assignment.entity.TestEntity;
import com.terrakorea.assignment.entity.TestRepository;
import com.terrakorea.assignment.entity.TestHandler;
import com.terrakorea.assignment.monitoring.CustomTimer;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SpringBootTest
@TestPropertySource(value = "/application-test.yml")
public class ValidationCodeWithDatabaseTests {

    private CustomTimer timer = new CustomTimer();

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestHandler testHandler;

    private final int testSaveMinuteValue = 60*24;

    @Test
    @DisplayName("cpu usage 저장 및 조회")
    public void saveCpuUsagePerSecond() {

        // given
        for (int i = 0; i < 5; i++) {
            Double cpuUsage = getCpu();
            LocalDateTime now = LocalDateTime.now();
            Date date = Date.from(now.atZone(ZoneId.of(timer.seoul)).toInstant());
            TestEntity testEntity = new TestEntity(cpuUsage, date, date);
            testRepository.save(testEntity);
        }

        // when
        List<TestEntity> testEntities = testRepository.findAll();

        // then
        Assertions.assertEquals(5, testEntities.size());

        // print
        testEntities.forEach(result -> System.out.printf(" cpu usage -> %s , createdDate -> %s , " +
                "createdTime -> %s \n", result.getCpuUsage(), result.getCreatedDate(), result.getCreatedTime()));
    }

    @Test
    @DisplayName("분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.")
    public void retrieveCpuUsagePerMinute() {

        // given
        for (int i = 0; i < 300; i++) { // 300분
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(timer.seoul));
            calendar.set(2024, Calendar.JUNE, 23, 6, i, 00);
            Date date = calendar.getTime();
            double random = ((int) (Math.random() * 420) + 1) / 100.0; // 사용률 42% 이내 랜덤값
            TestEntity testEntity = new TestEntity(random, date, date);
            testRepository.save(testEntity);
        }

        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.seoul));
        newCal1.set(2024, Calendar.JUNE, 23, 8, 0, 0);
        Date startDateTime = newCal1.getTime();
        Calendar newCal2 = Calendar.getInstance();
        newCal2.setTimeZone(TimeZone.getTimeZone(timer.seoul));
        newCal2.set(2024, Calendar.JUNE, 23, 10, 0, 0);
        Date endDateTime = newCal2.getTime();

        // when
        List<TestEntity> testEntities = testRepository.findByCreatedDateIsBetween(startDateTime, endDateTime);
        // then
        Assertions.assertEquals(120, testEntities.size());

        // print
        testEntities.forEach(result -> {
            System.out.printf("cpu -> %s, date -> %s, time -> %s\n", result.getCpuUsage(),
                    result.getCreatedDate(), result.getCreatedTime());
        });

    }

    @Test
    @DisplayName("시 단위 조회: 지정한 날짜의 시  단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    public void minMaxAvgCpuUsagePerHourOfDay() {

        // given
        for (int i = 0; i < testSaveMinuteValue; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(timer.seoul));
            calendar.set(2024, Calendar.JUNE, 20, 1, i, 00);
            Date date = calendar.getTime();
            double random = ((int) (Math.random() * 50) + 1) / 100.0;
            TestEntity testEntity = new TestEntity(random, date, date);
            testRepository.save(testEntity);
        }

        Double max = 0.0;
        Double avg = 0.0;
        Double min = 100.0;

        // 3시부터 5시 조회
        Calendar newCal1 = Calendar.getInstance();
        newCal1.setTimeZone(TimeZone.getTimeZone(timer.seoul));
        newCal1.set(2024, Calendar.JUNE, 20, 3, 0, 00);
        Date startSearchDate = newCal1.getTime();

        Calendar newCal2 = Calendar.getInstance();
        newCal2.setTimeZone(TimeZone.getTimeZone(timer.seoul));
        newCal2.set(2024, Calendar.JUNE, 20, 5, 0, 00);
        Date endSearchDate = newCal2.getTime();

        // when
        List<TestEntity> testEntities = testRepository.findAll();
        List<TestEntity> byCreatedDate = testRepository.findByCreatedDateIsBetween(startSearchDate, endSearchDate);

        Double minValue = testHandler.minCpuUsage(testEntities);
        Double avgValue = testHandler.avgCpuUsage(testEntities);
        Double maxValue = testHandler.maxCpuUsage(testEntities);
        for (TestEntity testEntity : testEntities) {
            double value = testEntity.getCpuUsage().doubleValue();
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
            avg += value;
        }
        avg /= testEntities.size();

        // then
        Assertions.assertEquals(testSaveMinuteValue, testEntities.size());
        Assertions.assertEquals(120, byCreatedDate.size());
        Assertions.assertEquals(min, minValue);
        Assertions.assertEquals(max, maxValue);
        Assertions.assertEquals(Math.round(avg * 1000.0) / 1000.0, Math.round(avgValue * 1000.0) / 1000.0);

        // print
//        byCreatedDate.forEach(result -> {
//            System.out.printf("cpu -> %s, date -> %s, time -> %s\n", result.getCpuUsage(),
//                    result.getCreatedDate(), result.getCreatedTime());
//        });
    }

    @Test
    @DisplayName("일 단위 조회: 지정한 날짜 구간의 일  단위 CPU 최소/최대/평균 사용률을 조회합니다.")
    public void minMaxAvgCpuUsagePerDay() {

        // given (20일부터 24일까지 분 단위 랜덤 값 저장)
        for (int i = 0; i < testSaveMinuteValue*4; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(timer.seoul));
            calendar.set(2024, Calendar.JUNE, 20, 1, i, 00);
            Date date = calendar.getTime();
            double random = ((int) (Math.random() * 50) + 1) / 100.0;
            TestEntity testEntity = new TestEntity(random, date, date);
            testRepository.save(testEntity);
        }

        // when
        List<TestEntity> all = testRepository.findAll();

        // then
        System.out.printf("%d\n",all.size());
        all.forEach(result -> {
            System.out.printf("cpu -> %s, date -> %s, time -> %s\n", result.getCpuUsage(),
                    result.getCreatedDate(), result.getCreatedTime());
        });
    }

    private Double getCpu() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        return cpu.getSystemCpuLoad(timer.aSecond);
    }

}
