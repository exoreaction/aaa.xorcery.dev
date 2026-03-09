package com.aurorapoc.datagenerator.generators.hr;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.config.OrganizationExpectations;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RetentionCrisisGenerator {
    private static final Logger logger = LoggerFactory.getLogger(RetentionCrisisGenerator.class);

    private final ObjectMapper objectMapper;
    private final OrganizationConfiguration config;
    private final Random random;
    private int eventIdCounter;

    public RetentionCrisisGenerator(ObjectMapper objectMapper, OrganizationConfiguration config) {
        this.objectMapper = objectMapper;
        this.config = config;
        this.random = new Random(42);
        this.eventIdCounter = 5000; // Start high to avoid conflicts
    }

    public void generateRetentionPatterns(ArrayNode events) {
        OrganizationExpectations expectations = config.getExpectations();
        int expectedDepartures = expectations.getExpectedOperationalDepartures();

        if (expectedDepartures == 0 || config.getTotalConsultants() < 100) {
            logger.info("📊 No retention crisis patterns expected for {} - organization too small or stable",
                    config.getOrganizationName());
            return;
        }

        logger.info("📉 Generating retention crisis: {} operational track departures for {}",
                expectedDepartures, config.getOrganizationName());

        generateOperationalTrackDepartures(events, expectedDepartures);
        logger.info("✅ Generated {} operational track departures", expectedDepartures);
    }

    private void generateOperationalTrackDepartures(ArrayNode events, int departureCount) {
        // Retention crises happen in waves over 12-18 months
        String[] departureWaves = {
                "2023-09-15", "2023-12-10", "2024-03-20",
                "2024-06-15", "2024-09-25", "2024-12-05"
        };

        int departuresPerWave = Math.max(1, departureCount / departureWaves.length);
        int remainderDepartures = departureCount % departureWaves.length;

        int departureIndex = 0;
        for (int wave = 0; wave < departureWaves.length && departureIndex < departureCount; wave++) {
            int departuresThisWave = departuresPerWave + (wave < remainderDepartures ? 1 : 0);

            for (int dep = 0; dep < departuresThisWave && departureIndex < departureCount; dep++) {
                generateSingleOperationalDeparture(events, departureIndex, departureWaves[wave]);
                departureIndex++;
            }
        }
    }

    private void generateSingleOperationalDeparture(ArrayNode events, int departureIndex, String baseDate) {
        // Operational track = senior cohort members in the later 40% (index 60%+)
        int seniorCohort = config.getSeniorCohort();
        int operationalStart = (int)(seniorCohort * 0.6) + 1; // After strategic track
        int availableOperational = seniorCohort - operationalStart + 1;
        int consultantIndex = operationalStart + (departureIndex % availableOperational);

        String consultantId = "emp-senior-cohort-" + consultantIndex;

        // Generate the specific event the validator is looking for
        ObjectNode departureEvent = createBaseEvent("EmploymentStatusChanged", consultantId, "Employment");
        String departureDate = addDaysToTimestamp(baseDate + "T17:00:00Z", departureIndex * 3);
        departureEvent.put("timestamp", departureDate);

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("consultantId", consultantId);
        payload.put("previousStatus", "ACTIVE");
        payload.put("newStatus", "DEPARTED");
        payload.put("departureDate", departureDate.split("T")[0]);
        payload.put("track", "operational");

        // Add realistic business context
        payload.put("departureReason", getOperationalDepartureReason());
        payload.put("finalLevel", "Senior Consultant");
        payload.put("tenure", (24 + random.nextInt(36)) + " months"); // 2-5 years
        payload.put("lastPromotion", (18 + random.nextInt(24)) + " months ago");
        payload.put("managerChanges", 2 + random.nextInt(2)); // 2-3 changes
        payload.put("clientExposure", "Limited - primarily internal projects");

        departureEvent.set("payload", payload);
        events.add(departureEvent);

        // Also generate supporting events for realism
        generateExitInterview(events, consultantId, departureDate);
    }

    private void generateExitInterview(ArrayNode events, String consultantId, String departureDate) {
        ObjectNode exitEvent = createBaseEvent("ExitInterviewCompleted",
                "exit-" + consultantId, "HR");
        exitEvent.put("timestamp", addDaysToTimestamp(departureDate, 7));

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("consultantId", consultantId);
        payload.put("primaryReason", "Limited career advancement opportunities");
        payload.put("secondaryReason", "Lack of strategic project assignments");
        payload.put("managerRating", 2.5 + random.nextDouble() * 1.5);
        payload.put("careerSupportRating", 2.0 + random.nextDouble() * 1.0);
        payload.put("recommendCompany", random.nextBoolean() && random.nextBoolean()); // 25%
        payload.put("newRoleLevel", "Senior+ at competitor");

        exitEvent.set("payload", payload);
        events.add(exitEvent);
    }

    private String getOperationalDepartureReason() {
        String[] reasons = {
                "Limited growth opportunities",
                "Lack of strategic project exposure",
                "Career advancement stalled",
                "Better role at competitor",
                "Insufficient client interaction"
        };
        return reasons[random.nextInt(reasons.length)];
    }

    // Utility methods (will eventually be in base class)
    private ObjectNode createBaseEvent(String eventType, String entityId, String entityType) {
        ObjectNode event = objectMapper.createObjectNode();
        event.put("eventId", "evt-" + String.format("%06d", eventIdCounter++));
        event.put("eventType", eventType);
        event.put("entityId", entityId);
        event.put("entityType", entityType);
        event.put("tenantId", config.getTenantId());

        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("userId", "system@" + config.getTenantId().replace("-", "") + ".com");
        event.set("metadata", metadata);

        return event;
    }

    private String addDaysToTimestamp(String baseTimestamp, int days) {
        LocalDateTime base = LocalDateTime.parse(baseTimestamp.replace("Z", ""));
        return base.plusDays(days).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
    }
}