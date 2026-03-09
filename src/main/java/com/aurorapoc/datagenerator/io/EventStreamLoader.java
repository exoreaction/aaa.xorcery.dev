package com.aurorapoc.datagenerator.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventStreamLoader {

    private static final Logger logger = LoggerFactory.getLogger(EventStreamLoader.class);

    private final ObjectMapper objectMapper;
    private final String inputDirectory;

    // Default file names to load
    private static final List<String> DEFAULT_STREAM_FILES = Arrays.asList(
            "hr-employment-events-expanded.json",
            "project-management-events-expanded.json",
            "client-relationship-events-expanded.json",
            "skills-certification-events-expanded.json",
            "resource-allocation-events-expanded.json"
    );

    public EventStreamLoader(ObjectMapper objectMapper, String inputDirectory) {
        this.objectMapper = objectMapper;
        this.inputDirectory = inputDirectory;
    }

    /**
     * Loads all default event stream files from the input directory
     *
     * @return Map of stream name to JsonNode (raw data for validation)
     * @throws IOException if file reading fails
     */
    public Map<String, JsonNode> loadAllEventStreams() throws IOException {
        return loadEventStreams(DEFAULT_STREAM_FILES);
    }

    /**
     * Loads specified event stream files
     *
     * @param filenames List of filenames to load
     * @return Map of stream name to JsonNode
     * @throws IOException if file reading fails
     */
    public Map<String, JsonNode> loadEventStreams(List<String> filenames) throws IOException {
        Map<String, JsonNode> streams = new HashMap<>();

        logger.info("🔍 Loading event streams from directory: {}", new File(inputDirectory).getAbsolutePath());

        for (String filename : filenames) {
            try {
                LoaderResult result = loadSingleEventStream(filename);
                if (result.isSuccess()) {
                    streams.put(result.getStreamName(), result.getEventStream());
                    logger.info("✅ Successfully loaded {}: {} events",
                            result.getStreamName(), result.getEventCount());
                } else {
                    logger.warn("⚠️ Skipped {}: {}", filename, result.getErrorMessage());
                }
            } catch (Exception e) {
                logger.error("❌ Error loading file {}: {}", filename, e.getMessage());
                // Continue with other files instead of failing completely
            }
        }

        logger.info("📊 Total streams loaded: {} (expected: {})", streams.size(), filenames.size());
        if (streams.size() < filenames.size()) {
            logger.warn("⚠️ Missing streams. Loaded: {}", streams.keySet());
        }

        return streams;
    }

    /**
     * Loads all event streams and converts them to ObjectNode for writing operations
     *
     * @return Map of stream name to ObjectNode (for summary generation)
     * @throws IOException if file reading fails
     */
    public Map<String, ObjectNode> loadAllEventStreamsAsObjectNodes() throws IOException {
        Map<String, JsonNode> rawStreams = loadAllEventStreams();
        Map<String, ObjectNode> objectNodeStreams = new HashMap<>();

        for (Map.Entry<String, JsonNode> entry : rawStreams.entrySet()) {
            if (entry.getValue() instanceof ObjectNode) {
                objectNodeStreams.put(entry.getKey(), (ObjectNode) entry.getValue());
            }
        }

        return objectNodeStreams;
    }

    /**
     * Loads a single event stream file
     *
     * @param filename The filename to load
     * @return LoaderResult containing the loaded data or error information
     */
    public LoaderResult loadSingleEventStream(String filename) {
        try {
            File file = new File(inputDirectory, filename);

            if (!file.exists()) {
                return LoaderResult.failure(filename, "File not found");
            }

            logger.debug("📖 Reading file: {} ({} bytes)", filename, file.length());
            JsonNode stream = objectMapper.readTree(file);

            // Validate required properties
            JsonNode eventStreamNode = stream.get("eventStream");
            JsonNode eventsNode = stream.get("events");

            if (eventStreamNode == null) {
                return LoaderResult.failure(filename,
                        "Missing 'eventStream' property. Available: " + getJsonProperties(stream));
            }

            if (eventsNode == null) {
                return LoaderResult.failure(filename,
                        "Missing 'events' property. Available: " + getJsonProperties(stream));
            }

            String streamName = eventStreamNode.asText();
            int eventCount = eventsNode.size();

            return LoaderResult.success(filename, streamName, stream, eventCount, file.length());

        } catch (Exception e) {
            return LoaderResult.failure(filename, "Exception: " + e.getMessage());
        }
    }

    private String getJsonProperties(JsonNode node) {
        if (node == null) return "null";

        StringBuilder props = new StringBuilder("[");
        node.fieldNames().forEachRemaining(fieldName -> {
            if (props.length() > 1) props.append(", ");
            props.append(fieldName);
        });
        props.append("]");
        return props.toString();
    }

    // ===========================================
    // RESULT CLASS
    // ===========================================

    /**
     * Result object containing information about a loaded file
     */
    public static class LoaderResult {
        private final String filename;
        private final boolean success;
        private final String streamName;
        private final JsonNode eventStream;
        private final int eventCount;
        private final long fileSizeBytes;
        private final String errorMessage;

        private LoaderResult(String filename, boolean success, String streamName,
                             JsonNode eventStream, int eventCount, long fileSizeBytes, String errorMessage) {
            this.filename = filename;
            this.success = success;
            this.streamName = streamName;
            this.eventStream = eventStream;
            this.eventCount = eventCount;
            this.fileSizeBytes = fileSizeBytes;
            this.errorMessage = errorMessage;
        }

        public static LoaderResult success(String filename, String streamName,
                                           JsonNode eventStream, int eventCount, long fileSizeBytes) {
            return new LoaderResult(filename, true, streamName, eventStream, eventCount, fileSizeBytes, null);
        }

        public static LoaderResult failure(String filename, String errorMessage) {
            return new LoaderResult(filename, false, null, null, 0, 0, errorMessage);
        }

        // Getters
        public String getFilename() { return filename; }
        public boolean isSuccess() { return success; }
        public String getStreamName() { return streamName; }
        public JsonNode getEventStream() { return eventStream; }
        public int getEventCount() { return eventCount; }
        public long getFileSizeBytes() { return fileSizeBytes; }
        public long getFileSizeKB() { return fileSizeBytes / 1024; }
        public String getErrorMessage() { return errorMessage; }

        @Override
        public String toString() {
            if (success) {
                return String.format("LoaderResult{filename='%s', stream='%s', events=%d, size=%dKB}",
                        filename, streamName, eventCount, getFileSizeKB());
            } else {
                return String.format("LoaderResult{filename='%s', error='%s'}", filename, errorMessage);
            }
        }
    }
}