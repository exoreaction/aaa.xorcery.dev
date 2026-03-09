package com.aurorapoc.datagenerator.reports.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data container for event analysis results across multiple business systems.
 * Now includes key events for narrative highlighting.
 */
public class EventAnalysis {

    // Stream-level aggregations
    private final Map<String, Integer> totalEventsByStream = new HashMap<>();
    private final Map<String, Map<String, Integer>> eventTypesByStream = new HashMap<>();

    // HR Events
    private int hiringEvents = 0;
    private int departureEvents = 0;
    private int promotionEvents = 0;
    private int aiSpecialistHires = 0;
    private int seniorDepartures = 0;

    // Project Events
    private int projectsCreated = 0;
    private int projectsCompleted = 0;
    private int projectDelays = 0;
    private int budgetVariances = 0;
    private int aiProjectsCreated = 0;

    // Client Events
    private int clientEscalations = 0;
    private int satisfactionSurveys = 0;
    private double totalSatisfactionScore = 0.0;
    private int satisfactionScoreCount = 0;

    // Skills Events
    private int certificationsCompleted = 0;
    private int skillAssessments = 0;

    // Allocation Events
    private int allocationDecisions = 0;
    private int allocationOverrides = 0;
    private int benchAssignments = 0;

    // Key Events for Narrative (NEW)
    private final List<JsonNode> keyAllocationOverrides = new ArrayList<>();
    private final List<JsonNode> keyClientEscalations = new ArrayList<>();
    private final List<JsonNode> keyDepartureEvents = new ArrayList<>();
    private final List<JsonNode> keyPromotionEvents = new ArrayList<>();
    private final List<JsonNode> keyProjectDelays = new ArrayList<>();
    private final List<JsonNode> keyBudgetVariances = new ArrayList<>();
    private final List<JsonNode> keyCertificationEvents = new ArrayList<>();

    // Getters for stream-level data
    public Map<String, Integer> getTotalEventsByStream() {
        return new HashMap<>(totalEventsByStream);
    }

    public Map<String, Map<String, Integer>> getEventTypesByStream() {
        Map<String, Map<String, Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : eventTypesByStream.entrySet()) {
            copy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return copy;
    }

    public void setTotalEventsForStream(String streamName, int count) {
        totalEventsByStream.put(streamName, count);
    }

    public void setEventTypesForStream(String streamName, Map<String, Integer> eventTypes) {
        eventTypesByStream.put(streamName, new HashMap<>(eventTypes));
    }

    // HR Event getters and setters
    public int getHiringEvents() { return hiringEvents; }
    public void setHiringEvents(int hiringEvents) { this.hiringEvents = hiringEvents; }
    public void incrementHiringEvents() { this.hiringEvents++; }

    public int getDepartureEvents() { return departureEvents; }
    public void setDepartureEvents(int departureEvents) { this.departureEvents = departureEvents; }
    public void incrementDepartureEvents() { this.departureEvents++; }

    public int getPromotionEvents() { return promotionEvents; }
    public void setPromotionEvents(int promotionEvents) { this.promotionEvents = promotionEvents; }
    public void incrementPromotionEvents() { this.promotionEvents++; }

    public int getAiSpecialistHires() { return aiSpecialistHires; }
    public void setAiSpecialistHires(int aiSpecialistHires) { this.aiSpecialistHires = aiSpecialistHires; }
    public void incrementAiSpecialistHires() { this.aiSpecialistHires++; }

    public int getSeniorDepartures() { return seniorDepartures; }
    public void setSeniorDepartures(int seniorDepartures) { this.seniorDepartures = seniorDepartures; }
    public void incrementSeniorDepartures() { this.seniorDepartures++; }

    // Project Event getters and setters
    public int getProjectsCreated() { return projectsCreated; }
    public void setProjectsCreated(int projectsCreated) { this.projectsCreated = projectsCreated; }
    public void incrementProjectsCreated() { this.projectsCreated++; }

    public int getProjectsCompleted() { return projectsCompleted; }
    public void setProjectsCompleted(int projectsCompleted) { this.projectsCompleted = projectsCompleted; }
    public void incrementProjectsCompleted() { this.projectsCompleted++; }

    public int getProjectDelays() { return projectDelays; }
    public void setProjectDelays(int projectDelays) { this.projectDelays = projectDelays; }
    public void incrementProjectDelays() { this.projectDelays++; }

    public int getBudgetVariances() { return budgetVariances; }
    public void setBudgetVariances(int budgetVariances) { this.budgetVariances = budgetVariances; }
    public void incrementBudgetVariances() { this.budgetVariances++; }

    public int getAiProjectsCreated() { return aiProjectsCreated; }
    public void setAiProjectsCreated(int aiProjectsCreated) { this.aiProjectsCreated = aiProjectsCreated; }
    public void incrementAiProjectsCreated() { this.aiProjectsCreated++; }

    // Client Event getters and setters
    public int getClientEscalations() { return clientEscalations; }
    public void setClientEscalations(int clientEscalations) { this.clientEscalations = clientEscalations; }
    public void incrementClientEscalations() { this.clientEscalations++; }

    public int getSatisfactionSurveys() { return satisfactionSurveys; }
    public void setSatisfactionSurveys(int satisfactionSurveys) { this.satisfactionSurveys = satisfactionSurveys; }
    public void incrementSatisfactionSurveys() { this.satisfactionSurveys++; }

    public double getTotalSatisfactionScore() { return totalSatisfactionScore; }
    public void setTotalSatisfactionScore(double totalSatisfactionScore) { this.totalSatisfactionScore = totalSatisfactionScore; }
    public void addToTotalSatisfactionScore(double score) { this.totalSatisfactionScore += score; }

    public int getSatisfactionScoreCount() { return satisfactionScoreCount; }
    public void setSatisfactionScoreCount(int satisfactionScoreCount) { this.satisfactionScoreCount = satisfactionScoreCount; }
    public void incrementSatisfactionScoreCount() { this.satisfactionScoreCount++; }

    // Skills Event getters and setters
    public int getCertificationsCompleted() { return certificationsCompleted; }
    public void setCertificationsCompleted(int certificationsCompleted) { this.certificationsCompleted = certificationsCompleted; }
    public void incrementCertificationsCompleted() { this.certificationsCompleted++; }

    public int getSkillAssessments() { return skillAssessments; }
    public void setSkillAssessments(int skillAssessments) { this.skillAssessments = skillAssessments; }
    public void incrementSkillAssessments() { this.skillAssessments++; }

    // Allocation Event getters and setters
    public int getAllocationDecisions() { return allocationDecisions; }
    public void setAllocationDecisions(int allocationDecisions) { this.allocationDecisions = allocationDecisions; }
    public void incrementAllocationDecisions() { this.allocationDecisions++; }

    public int getAllocationOverrides() { return allocationOverrides; }
    public void setAllocationOverrides(int allocationOverrides) { this.allocationOverrides = allocationOverrides; }
    public void incrementAllocationOverrides() { this.allocationOverrides++; }

    public int getBenchAssignments() { return benchAssignments; }
    public void setBenchAssignments(int benchAssignments) { this.benchAssignments = benchAssignments; }
    public void incrementBenchAssignments() { this.benchAssignments++; }

    // NEW: Key Events Methods for Narrative Highlighting
    public void addKeyAllocationOverride(JsonNode event) {
        if (keyAllocationOverrides.size() < 5) { // Limit to top 5 for performance
            keyAllocationOverrides.add(event);
        }
    }

    public List<JsonNode> getKeyAllocationOverrides() {
        return new ArrayList<>(keyAllocationOverrides);
    }

    public void addKeyClientEscalation(JsonNode event) {
        if (keyClientEscalations.size() < 5) {
            keyClientEscalations.add(event);
        }
    }

    public List<JsonNode> getKeyClientEscalations() {
        return new ArrayList<>(keyClientEscalations);
    }

    public void addKeyDepartureEvent(JsonNode event) {
        if (keyDepartureEvents.size() < 3) {
            keyDepartureEvents.add(event);
        }
    }

    public List<JsonNode> getKeyDepartureEvents() {
        return new ArrayList<>(keyDepartureEvents);
    }

    public void addKeyPromotionEvent(JsonNode event) {
        if (keyPromotionEvents.size() < 3) {
            keyPromotionEvents.add(event);
        }
    }

    public List<JsonNode> getKeyPromotionEvents() {
        return new ArrayList<>(keyPromotionEvents);
    }

    public void addKeyProjectDelay(JsonNode event) {
        if (keyProjectDelays.size() < 5) {
            keyProjectDelays.add(event);
        }
    }

    public List<JsonNode> getKeyProjectDelays() {
        return new ArrayList<>(keyProjectDelays);
    }

    public void addKeyBudgetVariance(JsonNode event) {
        if (keyBudgetVariances.size() < 5) {
            keyBudgetVariances.add(event);
        }
    }

    public List<JsonNode> getKeyBudgetVariances() {
        return new ArrayList<>(keyBudgetVariances);
    }

    public void addKeyCertificationEvent(JsonNode event) {
        if (keyCertificationEvents.size() < 5) {
            keyCertificationEvents.add(event);
        }
    }

    public List<JsonNode> getKeyCertificationEvents() {
        return new ArrayList<>(keyCertificationEvents);
    }

    // Convenience methods
    public double getAverageSatisfactionScore() {
        return satisfactionScoreCount > 0 ? totalSatisfactionScore / satisfactionScoreCount : 0.0;
    }

    public double getAllocationOverrideRate() {
        return allocationDecisions > 0 ? (double) allocationOverrides / allocationDecisions * 100 : 0.0;
    }
}