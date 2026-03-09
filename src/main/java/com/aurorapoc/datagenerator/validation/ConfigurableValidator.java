package com.aurorapoc.datagenerator.validation;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

/**
 * Interface for validators that can use organization configuration
 * to adjust validation thresholds and expectations
 */
public interface ConfigurableValidator extends ScenarioValidator {

    /**
     * Validates the scenario using the provided event streams and organization configuration
     * @param eventStreams Map of stream name to stream data
     * @param config Organization configuration for dynamic thresholds
     * @throws AssertionError if validation fails
     */
    void validate(Map<String, JsonNode> eventStreams, OrganizationConfiguration config);
}