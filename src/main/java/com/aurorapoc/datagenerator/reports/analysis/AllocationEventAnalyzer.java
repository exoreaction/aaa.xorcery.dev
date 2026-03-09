package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Analyzes resource allocation events and updates EventAnalysis with allocation-related metrics.
 * Now captures key events for narrative highlighting.
 */
public class AllocationEventAnalyzer implements EventAnalyzer {

    @Override
    public void analyze(JsonNode events, EventAnalysis analysis) {
        if (events != null && events.isArray()) {
            for (JsonNode event : events) {
                if (event.has("eventType")) {
                    String eventType = event.get("eventType").asText();
                    JsonNode payload = event.get("payload");

                    switch (eventType) {
                        case "AllocationDecisionMade":
                            analysis.incrementAllocationDecisions();

                            // Check if this was an override and capture the actual event
                            if (payload != null && payload.has("decisionType")) {
                                String decisionType = payload.get("decisionType").asText();
                                if ("OVERRIDE_RECOMMENDATION".equals(decisionType)) {
                                    analysis.incrementAllocationOverrides();
                                    analysis.addKeyAllocationOverride(event); // Capture real event
                                }
                            }
                            break;

                        case "AllocationRequestReceived":
                            // These are requests, not decisions
                            break;

                        case "AllocationAnalysisCompleted":
                            // These are analysis completions, not final decisions
                            break;

                        case "ResourceUtilizationAnalysis":
                            // These are utilization reports
                            break;

                        case "AllocationPerformanceTracked":
                            // These are performance tracking events
                            break;

                        case "AllocationCorrectionInitiated":
                            // These are corrections after the fact
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getStreamName() {
        return "resource.allocation";
    }
}