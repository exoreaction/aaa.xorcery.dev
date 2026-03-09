package com.aurorapoc.datagenerator.generators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public abstract class BaseEventGenerator {
    protected final ObjectMapper objectMapper;
    protected final Random random;
    protected final OrganizationConfiguration config;
    protected int eventIdCounter = 1;

    // Common constants
    protected static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public BaseEventGenerator(ObjectMapper objectMapper, OrganizationConfiguration config) {
        this.objectMapper = objectMapper;
        this.config = config;
        this.random = new Random(42); // Fixed seed for reproducible results
    }

    public BaseEventGenerator(ObjectMapper objectMapper, int startingEventId, OrganizationConfiguration config) {
        this.objectMapper = objectMapper;
        this.config = config;
        this.random = new Random(42);
        this.eventIdCounter = startingEventId;
    }

    // Abstract methods that each generator must implement
    public abstract String getStreamName();
    public abstract String getVersion();
    public abstract void generateEvents(ArrayNode events);

    // Common helper methods
    protected String generateEventId() {
        return "evt-" + String.format("%06d", eventIdCounter++);
    }

    protected ObjectNode createBaseEvent(String eventType, String entityId, String entityType) {
        ObjectNode event = objectMapper.createObjectNode();
        event.put("eventId", generateEventId());
        event.put("eventType", eventType);
        event.put("entityId", entityId);
        event.put("entityType", entityType);
        event.put("tenantId", config.getTenantId());

        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("userId", "system@" + config.getTenantId().replace("-", "") + ".com");
        event.set("metadata", metadata);

        return event;
    }

    protected ArrayNode createArrayNode(String... items) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String item : items) {
            arrayNode.add(item);
        }
        return arrayNode;
    }

    protected ArrayNode createArrayNode(List<String> items) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String item : items) {
            arrayNode.add(item);
        }
        return arrayNode;
    }

    // Timestamp utility methods
    protected String generateTimestamp(String baseTimestamp, int maxDaysRange) {
        return addDaysToTimestamp(baseTimestamp, random.nextInt(maxDaysRange));
    }

    protected String addDaysToTimestamp(String baseTimestamp, int days) {
        LocalDateTime base = LocalDateTime.parse(baseTimestamp.replace("Z", ""));
        return base.plusDays(days).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }

    protected String addMinutesToTimestamp(String baseTimestamp, int minutes) {
        LocalDateTime base = LocalDateTime.parse(baseTimestamp.replace("Z", ""));
        return base.plusMinutes(minutes).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }

    protected String generatePastDate(String baseDate, int maxDaysBack) {
        return addDaysToTimestamp(baseDate + "T09:00:00Z", -random.nextInt(maxDaysBack)).split("T")[0];
    }

    // Random data helpers
    protected String getRandomLocation() {
        String[] locations = {"New York", "San Francisco", "Seattle", "Austin", "Boston", "Chicago"};
        return locations[random.nextInt(locations.length)];
    }

    protected String getRandomDegree() {
        String[] degrees = {"BS Computer Science", "MS Software Engineering", "MBA", "PhD Computer Science", "MS Data Science"};
        return degrees[random.nextInt(degrees.length)];
    }

    protected String getRandomUniversity() {
        String[] universities = {"Stanford", "MIT", "Carnegie Mellon", "UC Berkeley", "Harvard", "Columbia", "NYU"};
        return universities[random.nextInt(universities.length)];
    }
}