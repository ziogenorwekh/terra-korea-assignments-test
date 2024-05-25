package com.terrakorea.assignment;

import com.terrakorea.assignment.api.CpuUsageResources;
import com.terrakorea.assignment.entity.CpuUsageEntity;
import com.terrakorea.assignment.monitoring.CustomTimer;
import com.terrakorea.assignment.repository.CpuUsageRepository;
import com.terrakorea.assignment.service.CpuUsageService;
import com.terrakorea.assignment.service.CpuUsageServiceImpl;
import com.terrakorea.assignment.vo.*;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.*;


//통합 테스트: 컨트롤러 계층의 통합 테스트를 작성하세요.
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(value = "/application-test.yml")
public class CodeIntegrationTests {


    @Mock
    private CpuUsageService mockCpuUsageService;

    @Mock
    private CpuUsageRepository cpuUsageRepository;

    @InjectMocks
    private CpuUsageResources cpuUsageResources;

    private List<CpuUsageMinuteResponse> minuteResponses;
    private CpuUsageHourResponse cpuUsageHour;
    private CpuUsageDayResponse cpuUsageDay;

    @BeforeAll
    public void setUp() {
    }

    @BeforeEach
    public void cleanUp() {
        mockCpuUsageService = Mockito.mock(CpuUsageService.class);
        cpuUsageResources = new CpuUsageResources(mockCpuUsageService);
    }

    @AfterEach
    public void tearDown() {
    }


    @Test
    @DisplayName("분 단위의 데이터 조회 API 테스트")
    public void retrieveAMinuteResources() {

        // given
        Mockito.when(mockCpuUsageService.findCpuUsageMinute(Mockito.any())).thenReturn(minuteResponses);

        // when
        ResponseEntity<List<CpuUsageMinuteResponse>> responseEntity =
                cpuUsageResources.retrieveAMinutesCpuUsage(2024, 5, 20, 1);

        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(minuteResponses, responseEntity.getBody());
        Mockito.verify(mockCpuUsageService, Mockito.times(1)).findCpuUsageMinute(Mockito.any());
    }

    @Test
    @DisplayName("시간 단위의 데이터 조회 API 테스트")
    public void retrieveAHourResources() {
        // given
        Mockito.when(mockCpuUsageService.findCpuUsageHour(Mockito.any())).thenReturn(cpuUsageHour);

        // when
        ResponseEntity<CpuUsageHourResponse> responseEntity =
                cpuUsageResources.retrieveAHoursCpuUsage(2024, 5, 20);

        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(cpuUsageHour, responseEntity.getBody());
        Mockito.verify(mockCpuUsageService, Mockito.times(1)).findCpuUsageHour(Mockito.any());
    }

    @Test
    @DisplayName("일 단위의 데이터 조회 API 테스트")
    public void retrieveDayRangeResources() {
        // given
        Mockito.when(mockCpuUsageService.findCpuUsageDay(Mockito.any())).thenReturn(cpuUsageDay);

        // when
        ResponseEntity<CpuUsageDayResponse> responseEntity =
                cpuUsageResources.retrieveADaysCpuUsage(2024, 3, 10, 2024, 4, 24);

        // then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(cpuUsageDay, responseEntity.getBody());
        Mockito.verify(mockCpuUsageService, Mockito.times(1)).findCpuUsageDay(Mockito.any());
    }
}
