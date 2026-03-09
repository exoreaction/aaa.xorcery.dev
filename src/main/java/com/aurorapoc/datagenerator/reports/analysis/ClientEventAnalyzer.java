package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Analyzes client relationship events and updates EventAnalysis with client-related metrics.
 * Now captures key events for narrative highlighting.
 */
public class ClientEventAnalyzer implements EventAnalyzer {

    @Override
    public void analyze(JsonNode events, EventAnalysis analysis) {
        if (events != null && events.isArray()) {
            for (JsonNode event : events) {
                if (event.has("eventType")) {
                    String eventType = event.get("eventType").asText();
                    JsonNode payload = event.get("payload");

                    switch (eventType) {
                        case "ClientEscalation":
                            analysis.incrementClientEscalations();
                            analysis.addKeyClientEscalation(event); // Capture real escalation event
                            break;

                        case "ClientSatisfactionSurvey":
                            analysis.incrementSatisfactionSurveys();
                            if (payload != null && payload.has("overallSatisfaction")) {
                                double score = payload.get("overallSatisfaction").asDouble();
                                analysis.addToTotalSatisfactionScore(score);
                                analysis.incrementSatisfactionScoreCount();
                            }
                            break;

                        case "QualityIncidentReported":
                            analysis.incrementClientEscalations();
                            analysis.addKeyClientEscalation(event); // Also capture quality incidents
                            break;

                        case "ClientFeedbackReceived":
                            // Additional feedback events
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getStreamName() {
        return "client.relationship";
    }
}