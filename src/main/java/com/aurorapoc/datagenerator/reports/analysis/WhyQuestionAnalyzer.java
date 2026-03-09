package com.aurorapoc.datagenerator.reports.analysis;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Interface for WHY question analyzers that examine business patterns
 * and provide natural language explanations of root causes.
 */
public interface WhyQuestionAnalyzer {

    /**
     * Analyzes events to answer a specific WHY question
     */
    WhyQuestionResult analyze(OrganizationConfiguration config, EventAnalysis eventAnalysis, JsonNode eventStreams);

    /**
     * Returns the natural language WHY question this analyzer addresses
     */
    String getWhyQuestion(OrganizationConfiguration config);

    /**
     * Returns the executive persona most likely to ask this question
     */
    OrganizationConfiguration.PersonaProfile getPersona(OrganizationConfiguration config);

    /**
     * Returns the scenario title/name
     */
    String getScenarioTitle();

    /**
     * Returns the scenario number for ordering
     */
    int getScenarioNumber();
}