package com.aurorapoc.datagenerator.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.StreamSupport;

import static com.aurorapoc.datagenerator.validation.ValidationUtils.*;

public class CrossStreamCorrelationValidator implements ScenarioValidator {

    private static final Logger logger = LoggerFactory.getLogger(CrossStreamCorrelationValidator.class);

    @Override
    public void validate(Map<String, JsonNode> eventStreams) {
        logger.info("🔗 Validating Cross-Stream Event Correlations");

        // 1. Validate AI Initiative Correlation
        int aiCorrelationStreams = validateAIInitiativeCorrelation(eventStreams);

        // 2. Validate Senior Cohort Correlation
        boolean cohortCorrelation = validateSeniorCohortCorrelation(eventStreams);

        // Assert correlations exist
        Assertions.assertTrue(aiCorrelationStreams >= 2,
                String.format("AI correlation should appear in 2+ streams, found in: %d", aiCorrelationStreams));
        Assertions.assertTrue(cohortCorrelation, "Should find senior cohort correlation in HR events");

        logger.info("✅ Cross-stream correlations validated successfully");
        logger.info("   - AI initiative correlation found in {} streams", aiCorrelationStreams);
        logger.info("   - Senior cohort correlation validated");
    }

    private int validateAIInitiativeCorrelation(Map<String, JsonNode> eventStreams) {
        String aiCorrelationId = "ai-initiative-2024";
        int streamsWithCorrelation = 0;

        for (Map.Entry<String, JsonNode> entry : eventStreams.entrySet()) {
            ArrayNode events = (ArrayNode) entry.getValue().get("events");
            boolean hasCorrelation = StreamSupport.stream(events.spliterator(), false)
                    .anyMatch(event -> hasCorrelationId(event, aiCorrelationId));

            if (hasCorrelation) {
                streamsWithCorrelation++;
                logger.info("   - Found AI initiative correlation in: {}", entry.getKey());
            }
        }

        return streamsWithCorrelation;
    }

    private boolean validateSeniorCohortCorrelation(Map<String, JsonNode> eventStreams) {
        String cohortCorrelationId = "senior-promotion-cohort-q1-2023";
        ArrayNode hrEvents = (ArrayNode) eventStreams.get("hr.employment").get("events");

        return StreamSupport.stream(hrEvents.spliterator(), false)
                .anyMatch(event -> hasCorrelationId(event, cohortCorrelationId));
    }

    @Override
    public String getScenarioName() {
        return "Cross-Stream Correlations";
    }

    @Override
    public String getDescription() {
        return "Validates that related events across different streams have proper correlation IDs";
    }
}