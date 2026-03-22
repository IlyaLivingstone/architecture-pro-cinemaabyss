package com.example.events.model;

public record Event(String type, long timestamp) {
    public Event(String type) {
        this(type, System.currentTimeMillis());
    }
}
