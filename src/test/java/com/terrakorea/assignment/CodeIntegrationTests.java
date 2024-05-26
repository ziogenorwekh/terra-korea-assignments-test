package com.terrakorea.assignment;

import com.terrakorea.assignment.api.CpuUsageResources;
import com.terrakorea.assignment.monitoring.CpuUsageRegister;
import com.terrakorea.assignment.repository.CpuUsageRepository;
import com.terrakorea.assignment.service.CpuUsageService;
import com.terrakorea.assignment.vo.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;


//통합 테스트: 컨트롤러 계층의 통합 테스트를 작성하세요.
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockBean(CpuUsageRegister.class)
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

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

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
        factory.close();
    }


    @Test
    @DisplayName("분 단위의 데이터 조회 API 테스트 및 Validation 테스트")
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

        // given
        CpuUsageRequest cpuUsageRequest = new CpuUsageRequest(2024, 14, 10, 0);

        // when
        Set<ConstraintViolation<CpuUsageRequest>> violation = validator.validate(cpuUsageRequest);

        // then
        Assertions.assertTrue(!violation.isEmpty());
        violation.forEach(error -> {
            Assertions.assertEquals("Month must be less than or equal to 12", error.getMessage());
        });

    }

    @Test
    @DisplayName("시간 단위의 데이터 조회 API 테스트 및 Validation 테스트")
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

        // given
        CpuUsageRequest cpuUsageRequest = new CpuUsageRequest(1800, 12, 10, 0);

        // when
        Set<ConstraintViolation<CpuUsageRequest>> violation = validator.validate(cpuUsageRequest);

        // then
        Assertions.assertTrue(!violation.isEmpty());
        violation.forEach(error -> {
            Assertions.assertEquals("Year must be greater than or equal to 1990", error.getMessage());
        });
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
