package com.aurorapoc.datagenerator.validation;

import com.fasterxml.jackson.databind.JsonNode;

public final class ValidationUtils {

    private ValidationUtils() {} // Utility class

    // Event Property Accessors
    public static String getEventType(JsonNode event) {
        JsonNode eventType = event.get("eventType");
        return eventType != null ? eventType.asText() : "";
    }

    public static String getEntityId(JsonNode event) {
        JsonNode entityId = event.get("entityId");
        return entityId != null ? entityId.asText() : "";
    }

    public static String getTimestamp(JsonNode event) {
        JsonNode timestamp = event.get("timestamp");
        return timestamp != null ? timestamp.asText() : "";
    }

    // Payload Property Checks
    public static boolean hasPayloadProperty(JsonNode event, String property) {
        JsonNode payload = event.get("payload");
        return payload != null && payload.has(property) && payload.get(property) != null;
    }

    public static String getPayloadProperty(JsonNode event, String property) {
        JsonNode payload = event.get("payload");
        if (payload != null && payload.has(property) && payload.get(property) != null) {
            return payload.get(property).asText();
        }
        return null;
    }

    public static double getPayloadPropertyDouble(JsonNode event, String property) {
        JsonNode payload = event.get("payload");
        if (payload != null && payload.has(property) && payload.get(property) != null) {
            JsonNode propertyNode = payload.get(property);
            if (propertyNode.isNumber()) {
                return propertyNode.asDouble();
            }
        }
        return 0.0;
    }

    public static int getPayloadPropertyInt(JsonNode event, String property) {
        JsonNode payload = event.get("payload");
        if (payload != null && payload.has(property) && payload.get(property) != null) {
            JsonNode propertyNode = payload.get(property);
            if (propertyNode.isNumber()) {
                return propertyNode.asInt();
            }
        }
        return 0;
    }

    public static boolean getPayloadPropertyBoolean(JsonNode event, String property) {
        JsonNode payload = event.get("payload");
        if (payload != null && payload.has(property) && payload.get(property) != null) {
            JsonNode propertyNode = payload.get(property);
            if (propertyNode.isBoolean()) {
                return propertyNode.asBoolean();
            }
        }
        return false;
    }

    // Metadata Accessors
    public static boolean hasMetadataProperty(JsonNode event, String property) {
        JsonNode metadata = event.get("metadata");
        return metadata != null && metadata.has(property) && metadata.get(property) != null;
    }

    public static String getMetadataProperty(JsonNode event, String property) {
        JsonNode metadata = event.get("metadata");
        if (metadata != null && metadata.has(property) && metadata.get(property) != null) {
            return metadata.get(property).asText();
        }
        return null;
    }

    // Timestamp Utilities
    public static boolean hasTimestampPrefix(JsonNode event, String prefix) {
        String timestamp = getTimestamp(event);
        return timestamp != null && timestamp.startsWith(prefix);
    }

    public static boolean hasTimestampBetween(JsonNode event, String startDate, String endDate) {
        String timestamp = getTimestamp(event);
        return timestamp != null && timestamp.compareTo(startDate) >= 0 && timestamp.compareTo(endDate) <= 0;
    }

    // Entity ID Pattern Matching
    public static boolean entityIdContains(JsonNode event, String pattern) {
        String entityId = getEntityId(event);
        return entityId != null && entityId.contains(pattern);
    }

    public static boolean entityIdMatches(JsonNode event, String regex) {
        String entityId = getEntityId(event);
        return entityId != null && entityId.matches(regex);
    }

    // Correlation ID Utilities
    public static boolean hasCorrelationId(JsonNode event, String correlationId) {
        String metadataCorrelationId = getMetadataProperty(event, "correlationId");
        return correlationId.equals(metadataCorrelationId);
    }
}