package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Composite analyzer that coordinates multiple event analyzers and provides
 * the same interface as the original analyzeEvents method.
 */
public class CompositeEventAnalyzer {

    private final List<EventAnalyzer> analyzers;
    private final Map<String, EventAnalyzer> analyzersByStream;

    public CompositeEventAnalyzer() {
        this.analyzers = Arrays.asList(
                new HREventAnalyzer(),
                new ProjectEventAnalyzer(),
                new ClientEventAnalyzer(),
                new SkillsEventAnalyzer(),
                new AllocationEventAnalyzer()
        );

        this.analyzersByStream = new HashMap<>();
        for (EventAnalyzer analyzer : analyzers) {
            analyzersByStream.put(analyzer.getStreamName(), analyzer);
        }
    }

    /**
     * Analyzes all event streams and returns populated EventAnalysis.
     * This replaces the original analyzeEvents method in NaturalLanguageExecutiveReportGenerator.
     */
    public EventAnalysis analyzeEvents(Map<String, JsonNode> eventStreams) {
        EventAnalysis analysis = new EventAnalysis();

        for (Map.Entry<String, JsonNode> entry : eventStreams.entrySet()) {
            String streamName = entry.getKey();
            JsonNode stream = entry.getValue();
            JsonNode events = stream.get("events");

            if (events != null && events.isArray()) {
                // Set stream-level totals
                analysis.setTotalEventsForStream(streamName, events.size());

                // Analyze events by type for each stream
                Map<String, Integer> eventTypeCounts = new HashMap<>();
                for (JsonNode event : events) {
                    String eventType = event.has("eventType") ? event.get("eventType").asText() : "unknown";
                    eventTypeCounts.merge(eventType, 1, Integer::sum);
                }
                analysis.setEventTypesForStream(streamName, eventTypeCounts);

                // Use the appropriate analyzer for this stream
                EventAnalyzer analyzer = analyzersByStream.get(streamName);
                if (analyzer != null) {
                    analyzer.analyze(events, analysis);
                }
            }
        }

        return analysis;
    }

    /**
     * Gets all available analyzers.
     */
    public List<EventAnalyzer> getAnalyzers() {
        return analyzers;
    }

    /**
     * Gets analyzer for a specific stream.
     */
    public EventAnalyzer getAnalyzerForStream(String streamName) {
        return analyzersByStream.get(streamName);
    }
}