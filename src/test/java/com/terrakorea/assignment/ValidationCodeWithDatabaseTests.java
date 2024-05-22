package com.terrakorea.assignment;

import com.terrakorea.assignment.entity.TestEntity;
import com.terrakorea.assignment.entity.TestRepository;
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
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestPropertySource(value = "/application-test.yml")
public class ValidationCodeWithDatabaseTests {

    private CustomTimer timer = new CustomTimer();

    @Autowired
    private TestRepository testRepository;

    @Test
    @DisplayName("cpu usage 저장 및 조회")
    public void saveCpuUsagePerSecond() {

        for (int i = 0; i < 5; i++) {
            Double cpuUsage = getCpu();
            LocalDateTime now = LocalDateTime.now();
            Date date = Date.from(now.atZone(ZoneId.of(timer.seoul)).toInstant());
            TestEntity testEntity = new TestEntity(cpuUsage, date, date);
            testRepository.save(testEntity);
        }

        List<TestEntity> testEntities = testRepository.findAll();

        Assertions.assertEquals(5, testEntities.size());

        testEntities.forEach(result -> System.out.printf(" cpu usage -> %s , createdDate -> %s , " +
                "createdTime -> %s \n", result.getCpuUsage(), result.getCreatedDate(),result.getCreatedTime()));
    }

    private Double getCpu() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        return cpu.getSystemCpuLoad(timer.aSecond);
    }
}
