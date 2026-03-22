package com.example.events.producer;

import com.example.events.model.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EventProducer {

    private static final Logger log = LoggerFactory.getLogger(EventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate,
                         ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMovieEvent() {
        sendEvent("movie-events", new Event("MOVIE"));
    }

    public void sendUserEvent() {
        sendEvent("user-events", new Event("USER"));
    }

    public void sendPaymentEvent() {
        sendEvent("payment-events", new Event("PAYMENT"));
    }

    private void sendEvent(String topic, Event event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, message);
            log.info("Sent to {}: {}", topic, message);
        } catch (JacksonException e) {
            log.error("Failed to serialize event: {}", e.getMessage(), e);
        }
    }
}