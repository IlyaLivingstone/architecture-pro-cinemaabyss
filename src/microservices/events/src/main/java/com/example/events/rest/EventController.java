package com.example.events.rest;

import com.example.events.producer.EventProducer;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventProducer eventProducer;

    public EventController(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @PostMapping("/movie")
    public ResponseEntity<Map<String, String>> createMovieEvent() {
        eventProducer.sendMovieEvent();
        return ResponseEntity.status(201).body(Map.of("status", "success"));
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, String>> createUserEvent() {
        eventProducer.sendUserEvent();
        return ResponseEntity.status(201).body(Map.of("status", "success"));
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, String>> createPaymentEvent() {
        eventProducer.sendPaymentEvent();
        return ResponseEntity.status(201).body(Map.of("status", "success"));
    }

    @GetMapping("/health")
    public Map<String, Boolean> health() {
        return Map.of(
                "status", true
        );
    }
}