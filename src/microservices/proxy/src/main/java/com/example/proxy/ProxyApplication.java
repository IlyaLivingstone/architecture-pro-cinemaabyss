package com.example.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

@SpringBootApplication
public class ProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }
}

@RestController
class MovieProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${MONOLITH_URL}")
    private String monolithUrl;

    @Value("${MOVIES_SERVICE_URL}")
    private String moviesServiceUrl;

    @Value("${GRADUAL_MIGRATION: false}")
    private boolean gradualMigration;

    @Value("${MOVIES_MIGRATION_PERCENT: 0}")
    private int migrationPercent;

    private final Random random = new Random();

    @GetMapping("/api/movies")
    public ResponseEntity<String> getMovies() {
        String targetUrl;

        if (gradualMigration && random.nextInt(100) < migrationPercent) {
            targetUrl = moviesServiceUrl + "/api/movies";
        } else {
            targetUrl = monolithUrl + "/api/movies";
        }

        System.out.println("Routing /api/movies to: " + targetUrl);

        try {
            return restTemplate.getForEntity(targetUrl, String.class);
        } catch (Exception e) {
            System.err.println("Error calling " + targetUrl + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Service unavailable");
        }
    }

    @RequestMapping(value = "/api/**", method = RequestMethod.GET)
    public ResponseEntity<String> proxyGet(HttpServletRequest request) {
        String path = request.getRequestURI();
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        String targetUrl = monolithUrl + path + queryString;

        System.out.println("Proxying GET " + path + " to " + targetUrl);

        try {
            return restTemplate.getForEntity(targetUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Service unavailable");
        }
    }
}