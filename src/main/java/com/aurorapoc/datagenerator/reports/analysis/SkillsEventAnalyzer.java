package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Analyzes skills certification events and updates EventAnalysis with skills-related metrics.
 * Now captures key events for narrative highlighting.
 */
public class SkillsEventAnalyzer implements EventAnalyzer {

    @Override
    public void analyze(JsonNode events, EventAnalysis analysis) {
        if (events != null && events.isArray()) {
            for (JsonNode event : events) {
                if (event.has("eventType")) {
                    String eventType = event.get("eventType").asText();

                    switch (eventType) {
                        case "SkillCertificationCompleted":
                        case "CertificationCompleted":
                        case "SkillCertificationEarned":
                            analysis.incrementCertificationsCompleted();
                            analysis.addKeyCertificationEvent(event); // Capture real certification
                            break;

                        case "SkillCertificationFailed":
                            // Could track failures if needed
                            break;

                        case "SkillAssessmentCompleted":
                        case "SkillEvaluated":
                            analysis.incrementSkillAssessments();
                            break;

                        case "SkillMarketDemandUpdated":
                        case "SalarySurveyCompleted":
                        case "SkillGapIdentified":
                        case "SkillDemandForecast":
                        case "CertificationBottleneckIdentified":
                        case "CertificationProcessOptimized":
                            // These could be tracked for additional metrics if needed
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getStreamName() {
        return "skills.certification";
    }
}