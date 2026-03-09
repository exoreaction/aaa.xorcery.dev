package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Analyzes project management events and updates EventAnalysis with project-related metrics.
 * Now captures key events for narrative highlighting.
 */
public class ProjectEventAnalyzer implements EventAnalyzer {

    @Override
    public void analyze(JsonNode events, EventAnalysis analysis) {
        if (events != null && events.isArray()) {
            for (JsonNode event : events) {
                if (event.has("eventType")) {
                    String eventType = event.get("eventType").asText();
                    JsonNode payload = event.get("payload");

                    switch (eventType) {
                        case "ProjectCreated":
                            analysis.incrementProjectsCreated();

                            // Check if it's an AI project
                            if (payload != null && payload.has("technology")) {
                                JsonNode tech = payload.get("technology");
                                if (tech.isArray()) {
                                    for (JsonNode techItem : tech) {
                                        String techStr = techItem.asText().toLowerCase();
                                        if (techStr.contains("machine learning") || techStr.contains("ai") || techStr.contains("ml")) {
                                            analysis.incrementAiProjectsCreated();
                                            break;
                                        }
                                    }
                                }
                            }
                            break;

                        case "ProjectStatusChanged":
                            if (payload != null && payload.has("newStatus")) {
                                String newStatus = payload.get("newStatus").asText();
                                if ("RESOURCE_CONSTRAINED".equals(newStatus) ||
                                        "DELAYED".equals(newStatus) ||
                                        "OVERDUE".equals(newStatus)) {
                                    analysis.incrementProjectDelays();
                                    analysis.addKeyProjectDelay(event); // Capture real delay event
                                }
                            }
                            break;

                        case "ProjectTimelineVariance":
                            analysis.incrementProjectDelays();
                            analysis.addKeyProjectDelay(event); // Capture timeline issues
                            break;

                        case "ProjectBudgetVariance":
                            analysis.incrementBudgetVariances();
                            analysis.addKeyBudgetVariance(event); // Capture budget issues
                            break;

                        case "ProjectCompleted":
                            analysis.incrementProjectsCompleted();
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getStreamName() {
        return "project.management";
    }
}