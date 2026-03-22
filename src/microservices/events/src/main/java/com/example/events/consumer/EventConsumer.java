package com.example.events.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "movie-events", groupId = "events-group")
    public void consumeMovieEvent(String message) {
        log.info("🎬 Received movie event: {}", message);
    }

    @KafkaListener(topics = "user-events", groupId = "events-group")
    public void consumeUserEvent(String message) {
        log.info("👤 Received user event: {}", message);
    }

    @KafkaListener(topics = "payment-events", groupId = "events-group")
    public void consumePaymentEvent(String message) {
        log.info("💳 Received payment event: {}", message);
    }
}