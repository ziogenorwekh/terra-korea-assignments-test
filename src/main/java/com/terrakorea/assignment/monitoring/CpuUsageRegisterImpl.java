package com.terrakorea.assignment.monitoring;

import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.repository.CpuUsageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Component
public class CpuUsageRegisterImpl implements CpuUsageRegister {

    private final CpuUsageRepository cpuUsageRepository;

    @Autowired
    public CpuUsageRegisterImpl(CpuUsageRepository cpuUsageRepository) {
        this.cpuUsageRepository = cpuUsageRepository;
    }

    @Override
    @Scheduled(fixedRate = 1000L * 60)
    public void calculateCpuUsage() {
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            CentralProcessor cpu = hal.getProcessor();

            double systemCpuLoad = 0.0;

            for (int i = 0; i < 60; i++) {
                systemCpuLoad += cpu.getSystemCpuLoad(999L);
            }

            double avgCpuLoad = systemCpuLoad / 60;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
            Date registerDate = calendar.getTime();
            cpuUsageRepository.save(new CpuUsageEntity(avgCpuLoad, registerDate, registerDate));
            log.info("CPU usage report saved to database");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}", e.getMessage());
        }
    }
}
