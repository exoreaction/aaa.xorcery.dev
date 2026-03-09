package com.aurorapoc.datagenerator.io;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.generators.BaseEventGenerator;
import com.aurorapoc.datagenerator.reports.ExecutiveSummaryGenerator;
import com.aurorapoc.datagenerator.reports.NaturalLanguageExecutiveReportGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class EventStreamWriter {

    private static final Logger logger = LoggerFactory.getLogger(EventStreamWriter.class);

    private final ObjectMapper objectMapper;
    private final String outputDirectory;
    private final String tenantId;

    public EventStreamWriter(ObjectMapper objectMapper, String outputDirectory, String tenantId) {
        this.objectMapper = objectMapper;
        this.outputDirectory = outputDirectory;
        this.tenantId = tenantId;

        // Ensure output directory exists
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    /**
     * Creates and writes an event stream using the provided generator
     *
     * @param generator The event generator to use
     * @param filename The output filename (without path)
     * @return WriterResult containing the file info and event count
     * @throws IOException if file writing fails
     */
    public WriterResult writeEventStream(BaseEventGenerator generator, String filename) throws IOException {
        logger.info("📝 Writing event stream: {} using {}", filename, generator.getClass().getSimpleName());

        // Create event stream structure
        ObjectNode rootNode = createEventStreamStructure(generator);

        // Generate events
        ArrayNode eventsArray = objectMapper.createArrayNode();
        generator.generateEvents(eventsArray);
        rootNode.set("events", eventsArray);

        // Write to file
        File outputFile = new File(outputDirectory, filename);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, rootNode);

        // Create result
        WriterResult result = new WriterResult(
                outputFile,
                eventsArray.size(),
                generator.getStreamName(),
                generator.getClass().getSimpleName()
        );

        logger.info("✅ {} generated {} events ({} KB)",
                result.getGeneratorName(), result.getEventCount(), result.getFileSizeKB());

        return result;
    }

    /**
     * Writes a standalone event stream object to file
     *
     * @param eventStream The pre-built event stream object
     * @param filename The output filename
     * @return WriterResult containing file information
     * @throws IOException if file writing fails
     */
    public WriterResult writeEventStream(ObjectNode eventStream, String filename) throws IOException {
        File outputFile = new File(outputDirectory, filename);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, eventStream);

        int eventCount = eventStream.has("events") ? eventStream.get("events").size() : 0;
        String streamName = eventStream.has("eventStream") ? eventStream.get("eventStream").asText() : "unknown";

        WriterResult result = new WriterResult(outputFile, eventCount, streamName, "Manual");

        logger.info("📄 File: {} ({} KB)", result.getFilename(), result.getFileSizeKB());

        return result;
    }

    /**
     * Writes a comprehensive summary report
     *
     * @param eventStreams Map of loaded event streams
     * @param additionalMetrics Optional additional metrics to include
     * @return WriterResult for the summary file
     * @throws IOException if file writing fails
     */
    public WriterResult writeSummaryReport(Map<String, ObjectNode> eventStreams,
                                           ObjectNode additionalMetrics) throws IOException {
        logger.info("📋 Writing scenario summary report...");

        ObjectNode summaryReport = objectMapper.createObjectNode();
        summaryReport.put("generatedDate", LocalDateTime.now().toString());
        summaryReport.put("totalEventStreams", eventStreams.size());

        // Calculate totals and create stream summaries
        int totalEvents = 0;
        ObjectNode streamSummaries = objectMapper.createObjectNode();

        for (Map.Entry<String, ObjectNode> entry : eventStreams.entrySet()) {
            ObjectNode streamSummary = createStreamSummary(entry.getValue());
            streamSummaries.set(entry.getKey(), streamSummary);
            totalEvents += streamSummary.get("eventCount").asInt();
        }

        summaryReport.put("totalEvents", totalEvents);
        summaryReport.set("streamSummaries", streamSummaries);

        // Add additional metrics if provided
        if (additionalMetrics != null) {
            additionalMetrics.fieldNames().forEachRemaining(fieldName ->
                    summaryReport.set(fieldName, additionalMetrics.get(fieldName)));
        }

        // Write summary report
        File summaryFile = new File(outputDirectory, "scenario-summary-report.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(summaryFile, summaryReport);

        WriterResult result = new WriterResult(summaryFile, totalEvents, "summary", "SummaryReportWriter");

        logger.info("✅ Summary report generated with {} total events across {} streams",
                totalEvents, eventStreams.size());

        return result;
    }

    /**
     * Convenience method for writing summary without additional metrics
     */
    public WriterResult writeSummaryReport(Map<String, ObjectNode> eventStreams) throws IOException {
        return writeSummaryReport(eventStreams, null);
    }

    // ===========================================
    // PRIVATE HELPER METHODS
    // ===========================================

    private ObjectNode createEventStreamStructure(BaseEventGenerator generator) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("eventStream", generator.getStreamName());
        rootNode.put("version", generator.getVersion());
        rootNode.put("tenant", tenantId);
        return rootNode;
    }

    private ObjectNode createStreamSummary(ObjectNode eventStream) {
        ObjectNode summary = objectMapper.createObjectNode();
        ArrayNode events = (ArrayNode) eventStream.get("events");

        if (events != null) {
            summary.put("eventCount", events.size());

            // Count event types
            Map<String, Long> eventTypeCounts = new java.util.HashMap<>();
            events.forEach(event -> {
                String eventType = event.get("eventType").asText();
                eventTypeCounts.merge(eventType, 1L, Long::sum);
            });

            ObjectNode eventTypes = objectMapper.createObjectNode();
            eventTypeCounts.forEach((type, count) -> eventTypes.put(type, count));
            summary.set("eventTypes", eventTypes);
        } else {
            summary.put("eventCount", 0);
        }

        return summary;
    }

    // ===========================================
    // RESULT CLASS
    // ===========================================

    /**
     * Result object containing information about a written file
     */
    public static class WriterResult {
        private final File file;
        private final int eventCount;
        private final String streamName;
        private final String generatorName;

        public WriterResult(File file, int eventCount, String streamName, String generatorName) {
            this.file = file;
            this.eventCount = eventCount;
            this.streamName = streamName;
            this.generatorName = generatorName;
        }

        public File getFile() { return file; }
        public String getFilename() { return file.getName(); }
        public String getAbsolutePath() { return file.getAbsolutePath(); }
        public long getFileSizeBytes() { return file.length(); }
        public long getFileSizeKB() { return file.length() / 1024; }
        public int getEventCount() { return eventCount; }
        public String getStreamName() { return streamName; }
        public String getGeneratorName() { return generatorName; }

        @Override
        public String toString() {
            return String.format("WriterResult{filename='%s', events=%d, size=%dKB, stream='%s'}",
                    getFilename(), eventCount, getFileSizeKB(), streamName);
        }
    }

    /**
     * Writes a configuration report showing organization setup and expected metrics
     *
     * @param config The organization configuration
     * @return WriterResult for the configuration report file
     * @throws IOException if file writing fails
     */
    public WriterResult writeConfigurationReport(OrganizationConfiguration config) throws IOException {
        logger.info("📊 Writing organization configuration report...");

        ObjectNode configReport = objectMapper.createObjectNode();
        configReport.put("generatedDate", LocalDateTime.now().toString());
        configReport.put("organizationName", config.getOrganizationName());
        configReport.put("tenantId", config.getTenantId());

        // Organization structure
        ObjectNode structure = objectMapper.createObjectNode();
        structure.put("totalConsultants", config.getTotalConsultants());
        structure.put("aiSpecialists", config.getAiSpecialists());
        structure.put("seniorCohort", config.getSeniorCohort());
        structure.put("totalProjects", config.getTotalProjects());
        structure.put("aiProjects", config.getAiProjects());
        structure.put("clients", config.getClients());
        configReport.set("organizationStructure", structure);

        // Event targets
        ObjectNode targets = objectMapper.createObjectNode();
        targets.put("hrEvents", config.getTargetHREvents());
        targets.put("projectEvents", config.getTargetProjectEvents());
        targets.put("clientEvents", config.getTargetClientEvents());
        targets.put("skillsEvents", config.getTargetSkillsEvents());
        targets.put("allocationEvents", config.getTargetAllocationEvents());
        targets.put("totalExpectedEvents", config.getTotalExpectedEvents());
        configReport.set("eventTargets", targets);

        // Market conditions
        ObjectNode market = objectMapper.createObjectNode();
        market.put("marketVolatility", config.getMarketVolatility());
        market.put("turnoverRate", config.getTurnoverRate());
        configReport.set("marketConditions", market);

        // Business scenarios expected
        ArrayNode scenarios = objectMapper.createArrayNode();
        scenarios.add("AI Talent Paradox: " + config.getAiSpecialists() + " specialists hired but benched while " +
                config.getAiProjects() + " projects delayed");
        scenarios.add("Senior Retention Crisis: " + config.getSeniorCohort() + " seniors split between strategic/operational tracks");
        scenarios.add("Project Performance Analysis: " + config.getTotalProjects() + " projects with performance correlation patterns");
        configReport.set("expectedScenarios", scenarios);

        // Write configuration report
        File configFile = new File(outputDirectory, "organization-configuration-report.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile, configReport);

        WriterResult result = new WriterResult(configFile, 0, "configuration", "ConfigurationReportWriter");

        logger.info("✅ Configuration report generated for {}", config.getOrganizationName());

        return result;
    }

    /**
     * Writes an executive summary report
     */
    public WriterResult writeExecutiveSummary(OrganizationConfiguration config,
                                              Map<String, JsonNode> eventStreams,
                                              int totalEvents) throws IOException {
        logger.info("📋 Writing executive summary report...");

        ExecutiveSummaryGenerator summaryGenerator = new ExecutiveSummaryGenerator(objectMapper);
        ObjectNode executiveSummary = summaryGenerator.generateExecutiveSummary(config, eventStreams, totalEvents);

        File summaryFile = new File(outputDirectory, "executive-summary-report.json");
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(summaryFile, executiveSummary);

        WriterResult result = new WriterResult(summaryFile, totalEvents, "executive-summary", "ExecutiveSummaryGenerator");

        logger.info("✅ Executive summary generated for {}", config.getOrganizationName());

        return result;
    }

    /**
     * Writes a natural language executive report in markdown format
     */
    public WriterResult writeNaturalLanguageExecutiveReport(OrganizationConfiguration config,
                                                            Map<String, JsonNode> eventStreams,
                                                            int totalEvents) throws IOException {
        logger.info("📝 Writing natural language executive report...");

        NaturalLanguageExecutiveReportGenerator reportGenerator = new NaturalLanguageExecutiveReportGenerator();
        String reportContent = reportGenerator.generateExecutiveReport(config, eventStreams, totalEvents);

        File reportFile = new File(outputDirectory, "executive-report.md");

        // Write as plain text file
        try (java.io.FileWriter writer = new java.io.FileWriter(reportFile)) {
            writer.write(reportContent);
        }

        WriterResult result = new WriterResult(reportFile, totalEvents, "executive-report", "NaturalLanguageExecutiveReportGenerator");

        logger.info("✅ Natural language executive report generated for {}", config.getOrganizationName());

        return result;
    }
}