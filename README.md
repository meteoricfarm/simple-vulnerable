# Simple Vulnerable

이 저장소는 SQL Injection 취약점을 인위적으로 남겨둔 간단한 Spring Boot 애플리케이션입니다. 정적 분석(SAST)과 동적 분석(DAST) 도구가 취약점을 탐지하는 연습용으로 사용할 수 있습니다.

## 구성 요소
- `src/main/java/com/example/vulnerableapp/VulnerableAppApplication.java`: `/search` 엔드포인트에서 사용자 입력을 SQL 문자열에 그대로 연결합니다.
- `src/main/resources/data.sql`: 애플리케이션 시작 시 H2 인메모리 DB에 기본 사용자(`alice`, `bob`)를 생성합니다.
- `application.properties`: 서버 포트를 `8081`로 설정합니다.

## 사전 준비
- Java 11 이상
- Maven 3.6 이상

> ⚠️ **주의**  
> 이 프로젝트는 보안을 고려하지 않은 의도된 취약 코드를 포함합니다. 교육 및 테스트 목적 외의 용도로 사용하지 마세요.

## 빌드 및 실행
```bash
# 의존성 설치 및 애플리케이션 실행
mvn spring-boot:run
```

정상적으로 시작되면 `http://localhost:8081`에서 애플리케이션을 확인할 수 있습니다.  
패키징된 JAR로 실행하려면 다음을 사용하세요.

```bash
mvn clean package
java -jar target/vulnerable-app-0.0.1-SNAPSHOT.jar
```

## SQL Injection 재현 방법
애플리케이션은 `/search?username=...` 요청으로 사용자 정보를 조회합니다.

1. **정상 요청 예시**
   ```bash
   curl "http://localhost:8081/search?username=alice"
   ```
   기대 결과: `alice` 사용자 한 명만 반환합니다.

2. **취약점 악용 예시**
   ```bash
   curl "http://localhost:8081/search?username=' OR '1'='1"
   ```
   또는 URL 인코딩 버전:
   ```bash
   curl "http://localhost:8081/search?username=%27%20OR%20%271%27%3D%271"
   ```
   기대 결과: 조건이 항상 참이 되어 전체 사용자 목록(`alice`, `bob`)이 반환됩니다.

3. **로그 확인**
   터미널 로그에는 실행된 SQL문이 그대로 출력되며, 악의적인 입력이 그대로 포함된 것을 확인할 수 있습니다.

## 추가 실험 아이디어
- `curl` 대신 브라우저, Postman, 또는 ZAP과 같은 DAST 도구를 사용해 동일한 페이로드를 전송해 보세요.
- 정적 분석 도구(CodeQL 등)를 실행하여 `searchUser` 메서드에서 SQL Injection 경고가 발생하는지 확인하세요.

