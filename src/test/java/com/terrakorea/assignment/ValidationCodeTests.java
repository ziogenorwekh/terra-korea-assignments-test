package com.terrakorea.assignment;

import com.terrakorea.assignment.monitoring.CustomTimer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

class ValidationCodeTests {

    private CustomTimer customTimer;

    @Test
    @DisplayName("CPU 사용량 확인 테스트")
    public void getMyWebCpuStatus() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();

        long before = System.currentTimeMillis();
        // wait 3s
        double systemCpuLoad = cpu.getSystemCpuLoad(3000L);

        long now = System.currentTimeMillis() - before;

        System.out.printf("cpu usage(%%) -> %f%%\n", systemCpuLoad);
        System.out.printf("cpu usage(Double) -> %f\n",systemCpuLoad);
        System.out.printf("%dms\n", now);
    }
}
