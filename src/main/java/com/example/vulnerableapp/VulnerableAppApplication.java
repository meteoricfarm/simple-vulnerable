package com.example.vulnerableapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class VulnerableAppApplication {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(VulnerableAppApplication.class, args);
    }

    //
    // ZAP이 /search를 "발견"할 수 있도록 HTML 링크를 포함시킵니다.
    @GetMapping(value="/", produces="text/html")
    public String home() {
        // ZAP Spider가 이 href 태그를 읽고 /search 엔드포인트를 스캔합니다.
        // ?username=test 처럼 파라미터 예시를 주면 ZAP이 더 잘 인지합니다.
        return "<html><body>" +
               "  <h1>Welcome!</h1>" +
               "  <a href='/search?username=test'>Go to Test Search</a>" +
               "</body></html>";
    }

    /**
     * !!! 취약점 발생 지점 !!!
     * SAST (CodeQL, Coverity)는 이 코드를 스캔하여 SQL Injection을 탐지합니다.
     * DAST (Zaproxy)는 실행 중인 이 엔드포인트를 스캔하여 SQL Injection을 탐지합니다.
     *
     * @param username 사용자로부터 입력받는 파라미터
     * @return DB 조회 결과
     */
    @GetMapping("/search")
    public List<Map<String, Object>> searchUser(@RequestParam String username) {
        // [취약점] 파라미터를 정제하지 않고 쿼리 문자열에 직접 연결합니다.
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        
        System.out.println("Executing query: " + sql);
        
        return jdbcTemplate.queryForList(sql);
    }
}