package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Analyzes HR employment events and updates EventAnalysis with HR-related metrics.
 * Now captures key events for narrative highlighting.
 */
public class HREventAnalyzer implements EventAnalyzer {

    @Override
    public void analyze(JsonNode events, EventAnalysis analysis) {
        if (events != null && events.isArray()) {
            for (JsonNode event : events) {
                if (event.has("eventType")) {
                    String eventType = event.get("eventType").asText();
                    JsonNode payload = event.get("payload");

                    switch (eventType) {
                        case "PersonCreated":
                        case "EmploymentCreated":
                            analysis.incrementHiringEvents();

                            // Check if this is an AI specialist hire
                            if (payload != null && payload.has("specialization")) {
                                String specialization = payload.get("specialization").asText().toLowerCase();
                                if (specialization.contains("ai") || specialization.contains("ml") || specialization.contains("machine learning")) {
                                    analysis.incrementAiSpecialistHires();
                                }
                            }
                            break;

                        case "EmploymentStatusChanged":
                            if (payload != null && payload.has("newStatus")) {
                                String newStatus = payload.get("newStatus").asText();
                                if ("DEPARTED".equals(newStatus) || "TERMINATED".equals(newStatus)) {
                                    analysis.incrementDepartureEvents();
                                    analysis.addKeyDepartureEvent(event); // Capture real departure

                                    // Check if this is a senior departure
                                    if (payload.has("level") && payload.get("level").asText().contains("SENIOR")) {
                                        analysis.incrementSeniorDepartures();
                                    }
                                }
                            }
                            break;

                        case "LevelChanged":
                            analysis.incrementPromotionEvents();
                            analysis.addKeyPromotionEvent(event); // Capture real promotion
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getStreamName() {
        return "hr.employment";
    }
}