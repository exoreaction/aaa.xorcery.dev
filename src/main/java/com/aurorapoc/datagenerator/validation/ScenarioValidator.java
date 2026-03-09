package com.aurorapoc.datagenerator.validation;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface ScenarioValidator {

    /**
     * Validates the scenario using the provided event streams
     * @param eventStreams Map of stream name to stream data
     * @throws AssertionError if validation fails
     */
    void validate(Map<String, JsonNode> eventStreams);

    /**
     * Returns the human-readable name of this scenario
     */
    String getScenarioName();

    /**
     * Returns a brief description of what this validator checks
     */
    String getDescription();
}