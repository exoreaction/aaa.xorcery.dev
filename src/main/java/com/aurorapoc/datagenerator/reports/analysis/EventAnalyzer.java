package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Interface for analyzing specific types of events and updating EventAnalysis data.
 */
public interface EventAnalyzer {

    /**
     * Analyzes events from a specific stream and updates the EventAnalysis object.
     *
     * @param events JsonNode containing the events array to analyze
     * @param analysis EventAnalysis object to update with findings
     */
    void analyze(JsonNode events, EventAnalysis analysis);

    /**
     * Returns the name of the event stream this analyzer handles.
     *
     * @return stream name (e.g., "hr.employment", "project.management")
     */
    String getStreamName();
}