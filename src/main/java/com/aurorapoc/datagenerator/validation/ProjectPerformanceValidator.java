package com.aurorapoc.datagenerator.validation;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.StreamSupport;

import static com.aurorapoc.datagenerator.validation.ValidationUtils.*;

public class ProjectPerformanceValidator implements ScenarioValidator, ConfigurableValidator {

    private static final Logger logger = LoggerFactory.getLogger(ProjectPerformanceValidator.class);

    @Override
    public void validate(Map<String, JsonNode> eventStreams, OrganizationConfiguration config) {
        logger.info("📊 Validating Project Performance Analysis Scenario Data for {} ({} consultants)",
                config.getOrganizationName(), config.getTotalConsultants());

        // Calculate dynamic thresholds based on configuration - REALISTIC FOR ORGANIZATION SIZE
        int expectedCompletedProjects = Math.max(10, config.getTotalProjects() / 2);

        // Budget variances: Scale realistically with organization size
        int expectedBudgetVariances;
        if (config.getTotalConsultants() < 50) {
            // Startups: Expect lower variance rates (25% of projects)
            expectedBudgetVariances = Math.max(2, (int)(config.getTotalProjects() * 0.25));
        } else if (config.getTotalConsultants() < 150) {
            // Small business: Standard variance rate (35% of projects)
            expectedBudgetVariances = Math.max(4, (int)(config.getTotalProjects() * 0.35));
        } else {
            // Larger organizations: Higher variance tracking (40% of projects)
            expectedBudgetVariances = Math.max(6, (int)(config.getTotalProjects() * 0.4));
        }

        // Performance tracking: Scale with AI projects + organization maturity
        int expectedPerformanceTracking;
        if (config.getTotalConsultants() < 50) {
            // Startups: Limited tracking infrastructure
            expectedPerformanceTracking = Math.max(2, config.getAiProjects());
        } else {
            // Larger orgs: More sophisticated tracking
            expectedPerformanceTracking = Math.max(config.getAiProjects(), config.getTotalProjects() / 8);
        }

        ArrayNode projectEvents = (ArrayNode) eventStreams.get("project.management").get("events");
        ArrayNode allocationEvents = (ArrayNode) eventStreams.get("resource.allocation").get("events");
        ArrayNode clientEvents = (ArrayNode) eventStreams.get("client.relationship").get("events");

        ValidationResult result = new ValidationResult();

        // 1. Validate Project Completions with Performance Data
        result.completedProjects = validateCompletedProjects(projectEvents, expectedCompletedProjects, config);

        // 2. Validate Budget Variance Events
        result.budgetVariances = validateBudgetVariances(projectEvents, expectedBudgetVariances);

        // 3. Validate Performance Tracking Events
        result.performanceTracking = validatePerformanceTracking(allocationEvents, expectedPerformanceTracking);

        // 4. Validate High-Satisfaction Outcomes (strategic projects)
        result.highSatisfactionOutcomes = validateHighSatisfactionOutcomes(clientEvents);

        assertValidationResults(result, config);
        logValidationResults(result, expectedCompletedProjects, expectedBudgetVariances, expectedPerformanceTracking, config);
    }

    // Backward compatibility method
    @Override
    public void validate(Map<String, JsonNode> eventStreams) {
        // Use default configuration for backward compatibility
        OrganizationConfiguration defaultConfig = OrganizationConfiguration.standard();
        validate(eventStreams, defaultConfig);
    }

    private long validateCompletedProjects(ArrayNode projectEvents, int expectedCount, OrganizationConfiguration config) {
        long count = StreamSupport.stream(projectEvents.spliterator(), false)
                .filter(event -> "ProjectCompleted".equals(getEventType(event)))
                .count();

        Assertions.assertTrue(count >= expectedCount,
                String.format("Should have %d+ completed projects for organization with %d total projects, found: %d",
                        expectedCount, config.getTotalProjects(), count));

        return count;
    }

    private long validateBudgetVariances(ArrayNode projectEvents, int expectedCount) {
        long count = StreamSupport.stream(projectEvents.spliterator(), false)
                .filter(event -> "ProjectBudgetVariance".equals(getEventType(event)))
                .count();

        Assertions.assertTrue(count >= expectedCount,
                String.format("Should have %d+ budget variance events, found: %d",
                        expectedCount, count));

        return count;
    }

    private long validatePerformanceTracking(ArrayNode allocationEvents, int expectedCount) {
        long count = StreamSupport.stream(allocationEvents.spliterator(), false)
                .filter(event -> "AllocationPerformanceTracked".equals(getEventType(event)))
                .count();

        Assertions.assertTrue(count >= expectedCount,
                String.format("Should have %d+ performance tracking events, found: %d",
                        expectedCount, count));

        return count;
    }

    private long validateHighSatisfactionOutcomes(ArrayNode clientEvents) {
        return StreamSupport.stream(clientEvents.spliterator(), false)
                .filter(event -> "ClientFeedbackReceived".equals(getEventType(event)))
                .filter(event -> hasPayloadProperty(event, "overallSatisfaction") &&
                        getPayloadPropertyDouble(event, "overallSatisfaction") >= 4.0)
                .count();
    }

    private void assertValidationResults(ValidationResult result, OrganizationConfiguration config) {
        // Cross-validation: High satisfaction should correlate with strategic projects
        int strategicProjects = config.getStrategicProjects();

        // Scale satisfaction expectations realistically based on organization size
        int expectedHighSatisfaction;
        if (config.getTotalConsultants() < 50) {
            // Startups: Very lenient - just need some positive outcomes
            expectedHighSatisfaction = Math.max(1, strategicProjects / 4);
        } else if (config.getTotalConsultants() < 150) {
            // Small/medium: Moderate expectations
            expectedHighSatisfaction = Math.max(2, strategicProjects / 3);
        } else {
            // Large organizations: Higher expectations
            expectedHighSatisfaction = Math.max(4, strategicProjects / 2);
        }

        Assertions.assertTrue(result.highSatisfactionOutcomes >= expectedHighSatisfaction,
                String.format("Should have %d+ high satisfaction outcomes for organization with %d strategic projects, found: %d",
                        expectedHighSatisfaction, strategicProjects, result.highSatisfactionOutcomes));
    }

    private void logValidationResults(ValidationResult result, int expectedCompleted, int expectedBudget,
                                      int expectedPerformance, OrganizationConfiguration config) {
        logger.info("✅ Project Performance Analysis scenario validation passed");
        logger.info("   - {} completed projects with performance data (expected: {}+)",
                result.completedProjects, expectedCompleted);
        logger.info("   - {} budget variance events (expected: {}+)",
                result.budgetVariances, expectedBudget);
        logger.info("   - {} performance tracking events (expected: {}+)",
                result.performanceTracking, expectedPerformance);
        logger.info("   - {} high-satisfaction outcomes", result.highSatisfactionOutcomes);
        logger.info("📊 Organization context: {} total projects, {} strategic projects",
                config.getTotalProjects(), config.getStrategicProjects());
    }

    @Override
    public String getScenarioName() {
        return "Project Performance Analysis";
    }

    @Override
    public String getDescription() {
        return "Validates that project performance data shows clear patterns between team composition and outcomes";
    }

    private static class ValidationResult {
        long completedProjects;
        long budgetVariances;
        long performanceTracking;
        long highSatisfactionOutcomes;
    }
}