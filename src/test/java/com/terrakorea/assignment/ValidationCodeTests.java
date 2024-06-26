package com.terrakorea.assignment;

import com.terrakorea.assignment.monitoring.CustomTimer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Calendar;
import java.util.TimeZone;

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


    @Test
    @DisplayName("최근 일주일 이내 데이터 제공 비즈니스로직")
    public void recentProvideDataInAWeek() {

        // given
        // 7일 전
        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
        calender.add(Calendar.DAY_OF_MONTH, -7);

        // 8일 전
        Calendar calender2 = Calendar.getInstance();
        calender2.setTimeZone(TimeZone.getTimeZone(CustomTimer.Seoul));
        calender2.add(Calendar.DAY_OF_MONTH, -8);

        // when
        if (calender2.after(calender)) {
            System.out.println("뒤쳐 짐");
        } else {
            System.out.println("안 뒤쳐짐");
        }


    }
}
