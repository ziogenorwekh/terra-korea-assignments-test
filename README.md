### 1. 프로젝트 설정

* Spring Boot 사용
* Gradle 사용
* Java 11 이상 사용

### 2. 데이터베이스 설정

* H2를 개발 및 테스트에 사용
* MariaDB를 운용 데이터베이스로 사용
* JPA 사용

### 3. 기능 요구 사항

#### 데이터 수집 및 저장

* CPU 사용률 수집: 서버의 CPU 사용률을 분 단위로 수집합니다.
* 데이터 저장: 수집된 데이터를 데이터베이스에 저장합니다.
* 데이터 조회 API

* 분 단위 조회: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.
* 시 단위 조회: 지정한 날짜의 시  단위 CPU 최소/최대/평균 사용률을 조회합니다.
* 일 단위 조회: 지정한 날짜 구간의 일  단위 CPU 최소/최대/평균 사용률을 조회합니다.
* Swagger를 사용하여 API 문서화를 설정하세요.

#### 데이터 제공 기한

* 분 단위 API : 최근 1주 데이터 제공
* 시 단위 API : 최근 3달 데이터 제공
* 일 단위 API : 최근 1년 데이터 제공

### 4. 예외 처리

* 데이터 수집 실패 시 예외를 처리하고 로그를 남깁니다.
* API 요청 시 잘못된 파라미터에 대한 예외를 처리합니다.

### 5. 테스트

* 유닛 테스트: 서비스 계층과 데이터베이스 계층의 유닛 테스트를 작성하세요.
* 통합 테스트: 컨트롤러 계층의 통합 테스트를 작성하세요.

## 실행 방법
* 환경(Java 17, Gradle)
1. mariaDB 컨테이너 생성(예시: docker run -d -e MYSQL_ROOT_PASSWORD=testassign -p 10000:3306 --name mariadb mariadb)
2. database 생성(예시: assign)
3. resources/create-table.sql 파일의 테이블을 생성합니다.
4. 프로젝트 실행(메인클래스 - AssignmentApplication.class)
5. API 문서 접속(url: http://localhost:9000/swagger-ui/index.html)

### 요구 사항의 해결 방법
* OSHI 라이브러리를 사용하여 시스템의 CPU 사용량을 실시간으로 모니터링합니다.
* 웹 애플리케이션이 실행되면, 스케줄러를 사용하여 매 초마다 CPU 사용량을 측정하고, 
이를 분 단위 평균으로 계산하여 데이터베이스에 저장합니다.
* H2 데이터베이스를 활용하여 더미 데이터를 저장하고 테스트를 수행합니다.
* 테스트 클래스를 작성하여 기능을 검증하고, 테스트가 
통과하면 테스트 클래스 소스를 운영 소스 코드에 적용합니다.
* 제약 사항을 설정하여 요구 사항에 부합하는 데이터를 제공합니다.
* Spring Validation을 사용하여 잘못된 파라미터에 대해 예외를 처리하고, 적절한 에러 메시지를 반환합니다.
* Lombok을 활용하여 예외 발생 시 로그를 출력합니다.

## API 문서
##### 이 문서는 서버의 CPU 사용률을 시간 간격(일, 시간, 분)으로 사용량을 분석하는 API입니다.

### 분 단위 API
#### 지정된 시간 간격에 대한 분 단위 CPU 사용률을 검색합니다.

| Request | HTTP Method | URI                                              | Status Code    |
|------|-----|--------------------------------------------------|----------------|
|조회 일자 | GET| /api/v1/usage/minute/{year}/{month}/{day}/{hour} | 200            |
#### 매개변수
* {year}: 원하는 데이터의 연도 (예시: 2024)
* {month}: 원하는 데이터의 월 (1-12)
* {day}: 원하는 데이터의 일 (1-31)
* {hour}: 원하는 데이터의 시간 (0-23)

#### 응답 결과
* 분 단위 CPU 사용률 데이터를 나타내는 CpuUsageMinuteResponse 객체
```json
    {
        "cpuUsage": 0.0,
        "date": "2024-05-26",
        "time": "16:04:07"
    }
```
### 시간 단위 API
#### 지정된 날짜의 시간 단위 CPU 사용률을 검색합니다.

| Request | HTTP Method | URI                                              | Status Code    |
|------|-----|--------------------------------------------------|----------------|
|조회 일자 | GET| /api/v1/usage/hour/{year}/{month}/{day} | 200            |

#### 매개변수
* {year} - 원하는 데이터의 연도 (예시: 2024)
* {month} - 원하는 데이터의 월 (1-12)
* {day} - 원하는 데이터의 일 (1-31)

#### 응답 결과
- 시간 단위 CPU 사용률 데이터를 나타내는 CpuUsageHourResponse 객체
```json
{
  "date": "2024-05-26",
  "minCpuUsage": 0.0,
  "maxCpuUsage": 0.0,
  "List of average values by hour": [
    {
      "avgCpuUsage": 0.0,
      "hour": 16
    }
  ]
}
```

### 일 단위 API
#### 지정된 날짜 범위의 일 단위 CPU 사용률을 검색합니다.


| Request | HTTP Method | URI                                              | Status Code    |
|------|-----|--------------------------------------------------|----------------|
|조회 일자 | GET| /api/v1/usage/day/from/{fromYear}/{fromMonth}/{fromDay}/to/{toYear}/{toMonth}/{toDay} | 200            |


#### 매개변수
* {fromYear} - 날짜 범위의 시작 연도 (예시: 2024)
* {fromMonth} - 날짜 범위의 시작 월 (1-12)
* {fromDay} - 날짜 범위의 시작 일 (1-31)
* {toYear} - 날짜 범위의 종료 연도 (예시: 2024)
* {toMonth} - 날짜 범위의 종료 월 (1-12)
* {toDay} - 날짜 범위의 종료 일 (1-31)

#### 응답 결과
* 지정된 날짜 범위 내의 일 단위 CPU 사용률 데이터를 나타내는 CpuUsageDayResponse 객체
```json
{
    "minCpuUsage": 0.0,
    "maxCpuUsage": 0.0,
    "dayResponses": [
        {
            "avgCpuUsage": 0.0,
            "date": "2024-05-26"
        }
    ]
}
```

### 참고자료
* ChatGPT
* https://github.com/oshi/oshi?tab=readme-ov-file#usage
* https://springdoc.org/#getting-started
* https://dzone.com/articles/extending-swagger-and-spring-doc-open-api
* https://springdoc.org/
* https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html
* https://velog.io/@wonizizi99/Spring-mockito-%EB%8B%A8%EC%9C%84%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%8B%9C-Valid-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%98%88%EC%99%B8-%ED%85%8C%EC%8A%A4%ED%8A%B8
* https://github.com/springdoc/springdoc-openapi-demos/tree/2.x