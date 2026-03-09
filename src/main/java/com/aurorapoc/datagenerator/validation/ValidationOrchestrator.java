package com.aurorapoc.datagenerator.validation;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValidationOrchestrator {

    private static final Logger logger = LoggerFactory.getLogger(ValidationOrchestrator.class);

    private final List<ScenarioValidator> validators;

    public ValidationOrchestrator() {
        this.validators = Arrays.asList(
                new AITalentParadoxValidator(),
                new SeniorRetentionCrisisValidator(),
                new ProjectPerformanceValidator(),
                new CrossStreamCorrelationValidator()
        );
    }

    public void validateAll(Map<String, JsonNode> eventStreams, OrganizationConfiguration config) {
        logger.info("🔍 Validating Scenario Data Patterns for WHY Questions");

        if (config != null) {
            logger.info("📊 Organization: {} ({} consultants)",
                    config.getOrganizationName(), config.getTotalConsultants());
        }

        for (ScenarioValidator validator : validators) {
            try {
                logger.debug("Running validator: {} - {}", validator.getScenarioName(), validator.getDescription());

                // Pass configuration to validators that support it
                if (validator instanceof ConfigurableValidator && config != null) {
                    ((ConfigurableValidator) validator).validate(eventStreams, config);
                } else {
                    validator.validate(eventStreams);
                }

            } catch (Exception e) {
                logger.error("❌ Validation failed for {}: {}", validator.getScenarioName(), e.getMessage());
                throw e; // Re-throw to fail the test
            }
        }

        logger.info("✅ All scenario data patterns validated successfully");
        logger.info("🎯 Generated data supports all three WHY questions");
    }

    // Backward compatibility method
    public void validateAll(Map<String, JsonNode> eventStreams) {
        validateAll(eventStreams, null);
    }

    public List<ScenarioValidator> getValidators() {
        return validators;
    }
}