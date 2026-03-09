package com.aurorapoc.datagenerator.generators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ClientRelationshipEventGenerator extends BaseEventGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ClientRelationshipEventGenerator.class);

    // Configuration-driven values
    private final int CLIENTS_COUNT;
    private final int AI_PROJECTS_COUNT;
    private final List<ClientProfile> clientProfiles;

    // Client Profile data - now scales with configuration
    private static final List<ClientProfile> BASE_CLIENT_PROFILES = Arrays.asList(
            new ClientProfile("TechCorp Finance", "STRATEGIC", "18+ months", "HIGH", 0.85, "Financial Services"),
            new ClientProfile("MegaBank Corp", "STRATEGIC", "24+ months", "CRITICAL", 0.92, "Financial Services"),
            new ClientProfile("StartupCo", "OPERATIONAL", "12+ months", "MEDIUM", 0.65, "Professional Services"),
            new ClientProfile("Global Manufacturing", "STRATEGIC", "18+ months", "HIGH", 0.78, "Manufacturing"),
            new ClientProfile("HealthSystem Inc", "STRATEGIC", "36+ months", "CRITICAL", 0.95, "Healthcare"),
            new ClientProfile("RetailChain Ltd", "OPERATIONAL", "15+ months", "MEDIUM", 0.68, "Retail"),
            new ClientProfile("FinanceGroup", "STRATEGIC", "24+ months", "HIGH", 0.82, "Financial Services"),
            new ClientProfile("TechStart Ventures", "OPERATIONAL", "6+ months", "LOW", 0.55, "Technology"),
            new ClientProfile("Enterprise Corp", "STRATEGIC", "18+ months", "HIGH", 0.88, "Professional Services"),
            new ClientProfile("Innovation Labs", "OPERATIONAL", "12+ months", "MEDIUM", 0.70, "Professional Services"),
            new ClientProfile("Corporate Services", "OPERATIONAL", "9+ months", "LOW", 0.58, "Professional Services"),
            new ClientProfile("Strategic Partners", "STRATEGIC", "30+ months", "CRITICAL", 0.98, "Professional Services")
    );

    // Additional client profiles for larger organizations
    private static final List<ClientProfile> EXTENDED_CLIENT_PROFILES = Arrays.asList(
            new ClientProfile("Global Tech Solutions", "STRATEGIC", "24+ months", "HIGH", 0.87, "Technology"),
            new ClientProfile("Finance Dynamics", "STRATEGIC", "18+ months", "CRITICAL", 0.91, "Financial Services"),
            new ClientProfile("Manufacturing Plus", "OPERATIONAL", "12+ months", "MEDIUM", 0.72, "Manufacturing"),
            new ClientProfile("Healthcare Partners", "STRATEGIC", "30+ months", "HIGH", 0.89, "Healthcare"),
            new ClientProfile("Retail Innovations", "OPERATIONAL", "15+ months", "MEDIUM", 0.66, "Retail"),
            new ClientProfile("Energy Solutions", "STRATEGIC", "36+ months", "HIGH", 0.84, "Energy"),
            new ClientProfile("Digital Ventures", "OPERATIONAL", "9+ months", "LOW", 0.62, "Technology"),
            new ClientProfile("Consulting Group", "STRATEGIC", "24+ months", "CRITICAL", 0.94, "Professional Services"),
            new ClientProfile("Industrial Corp", "OPERATIONAL", "18+ months", "MEDIUM", 0.75, "Manufacturing"),
            new ClientProfile("Financial Advisors", "STRATEGIC", "30+ months", "HIGH", 0.86, "Financial Services"),
            new ClientProfile("Tech Startups Inc", "OPERATIONAL", "6+ months", "LOW", 0.59, "Technology"),
            new ClientProfile("Healthcare Systems", "STRATEGIC", "42+ months", "CRITICAL", 0.97, "Healthcare")
    );

    // Helper Classes
    static class ClientProfile {
        final String name;
        final String tier;
        final String experienceRequirement;
        final String pressureLevel;
        final double qualityThreshold;
        final String industry;

        ClientProfile(String name, String tier, String experienceRequirement,
                      String pressureLevel, double qualityThreshold, String industry) {
            this.name = name;
            this.tier = tier;
            this.experienceRequirement = experienceRequirement;
            this.pressureLevel = pressureLevel;
            this.qualityThreshold = qualityThreshold;
            this.industry = industry;
        }
    }

    public ClientRelationshipEventGenerator(ObjectMapper objectMapper, OrganizationConfiguration config) {
        super(objectMapper, config);
        this.CLIENTS_COUNT = config.getClients();
        this.AI_PROJECTS_COUNT = config.getAiProjects();
        this.clientProfiles = createScaledClientProfiles(config);
    }

    public ClientRelationshipEventGenerator(ObjectMapper objectMapper, int startingEventId, OrganizationConfiguration config) {
        super(objectMapper, startingEventId, config);
        this.CLIENTS_COUNT = config.getClients();
        this.AI_PROJECTS_COUNT = config.getAiProjects();
        this.clientProfiles = createScaledClientProfiles(config);
    }

    private List<ClientProfile> createScaledClientProfiles(OrganizationConfiguration config) {
        int clientsNeeded = config.getClients();
        List<ClientProfile> profiles = new java.util.ArrayList<>();

        // Start with base profiles
        profiles.addAll(BASE_CLIENT_PROFILES);

        // Add extended profiles if we need more clients
        if (clientsNeeded > BASE_CLIENT_PROFILES.size()) {
            profiles.addAll(EXTENDED_CLIENT_PROFILES);
        }

        // If we still need more, create variations
        while (profiles.size() < clientsNeeded) {
            ClientProfile base = BASE_CLIENT_PROFILES.get(profiles.size() % BASE_CLIENT_PROFILES.size());
            String suffix = " " + ((profiles.size() / BASE_CLIENT_PROFILES.size()) + 1);

            profiles.add(new ClientProfile(
                    base.name + suffix,
                    base.tier,
                    base.experienceRequirement,
                    base.pressureLevel,
                    base.qualityThreshold + (random.nextDouble() - 0.5) * 0.1, // Small variation
                    base.industry
            ));
        }

        // Return only the number we need
        return profiles.subList(0, clientsNeeded);
    }

    @Override
    public String getStreamName() {
        return "client.relationship";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void generateEvents(ArrayNode events) {
        // Generate Client Creation Events
        logger.info("🏢 Generating client entities and onboarding ({} clients)...", CLIENTS_COUNT);
        generateClientCreationEvents(events);

        // Generate Client Expectation Evolution (AI scenario key events)
        logger.info("📋 Generating client expectation shifts (AI projects: {})...", AI_PROJECTS_COUNT);
        generateClientExpectationEvolution(events);

        // Generate Client Satisfaction Surveys
        logger.info("😊 Generating client satisfaction surveys...");
        generateClientSatisfactionSurveys(events);

        // Generate Client Escalations and Quality Incidents
        logger.info("🚨 Generating client escalations and quality incidents...");
        generateClientEscalationsAndIncidents(events);

        // Generate Client Feedback on Projects
        logger.info("💬 Generating project-specific client feedback...");
        generateProjectClientFeedback(events);
    }

    private void generateClientCreationEvents(ArrayNode events) {
        for (int i = 0; i < clientProfiles.size(); i++) {
            ClientProfile client = clientProfiles.get(i);

            ObjectNode clientEvent = createBaseEvent("ClientCreated", "client-" + (i + 1), "Client");
            clientEvent.put("timestamp", addDaysToTimestamp("2022-01-01T09:00:00Z", i * 30));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("displayName", client.name);
            payload.put("industry", client.industry);
            payload.put("tier", client.tier);
            payload.put("size", random.nextBoolean() ? "LARGE" : "MEDIUM");
            payload.put("headquarters", getRandomLocation());
            payload.put("relationship", "ESTABLISHED");

            // Scale potential value based on organization size
            int baseValue = client.tier.equals("STRATEGIC") ? 3000000 : 500000;
            int variance = client.tier.equals("STRATEGIC") ? 7000000 : 1500000;
            // Larger organizations have larger potential values
            double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            payload.put("potentialValue", (int)(baseValue * organizationMultiplier + random.nextInt(variance)));

            payload.put("riskProfile", client.pressureLevel.equals("CRITICAL") ? "HIGH" : "MEDIUM");

            clientEvent.set("payload", payload);
            events.add(clientEvent);
        }
    }

    private void generateClientExpectationEvolution(ArrayNode events) {
        // Key evolution: Experience requirements increase due to quality issues (AI scenario)
        int aiClientCount = Math.min(AI_PROJECTS_COUNT, CLIENTS_COUNT); // Can't have more AI clients than total clients

        for (int i = 0; i < aiClientCount; i++) {
            String clientId = "client-" + (i + 1);

            // Initial expectation setting
            ObjectNode initialExp = createBaseEvent("ClientExpectationSet", clientId, "Client");
            initialExp.put("timestamp", "2024-07-15T14:00:00Z");

            ObjectNode payload1 = objectMapper.createObjectNode();
            payload1.put("expectationType", "CONSULTANT_EXPERIENCE");
            payload1.put("requirement", "Minimum 12 months experience");
            payload1.put("priority", "MEDIUM");
            payload1.put("flexibility", "NEGOTIABLE");

            initialExp.set("payload", payload1);
            events.add(initialExp);

            // Quality incident (triggers expectation change)
            ObjectNode incident = createBaseEvent("QualityIncidentReported", clientId, "Client");
            incident.put("timestamp", addDaysToTimestamp("2024-08-15T16:30:00Z", i * 7));

            ObjectNode incidentPayload = objectMapper.createObjectNode();
            incidentPayload.put("incidentType", "KNOWLEDGE_GAP");
            incidentPayload.put("description", "Junior consultant lacks domain knowledge, causing project delays");
            incidentPayload.put("consultantExperience", (8 + random.nextInt(8)) + " months");
            incidentPayload.put("projectImpact", (1 + random.nextInt(3)) + " week delay");
            incidentPayload.put("clientSatisfactionImpact", -1.0 - random.nextDouble());

            incident.set("payload", incidentPayload);
            events.add(incident);

            // Updated expectation (key AI scenario driver)
            ObjectNode updatedExp = createBaseEvent("ClientExpectationUpdated", clientId, "Client");
            updatedExp.put("timestamp", addDaysToTimestamp("2024-08-22T09:00:00Z", i * 7));

            ObjectNode payload2 = objectMapper.createObjectNode();
            payload2.put("expectationType", "CONSULTANT_EXPERIENCE");
            payload2.put("previousRequirement", "Minimum 12 months experience");

            // Use the client profile's experience requirement
            if (i < clientProfiles.size()) {
                payload2.put("newRequirement", clientProfiles.get(i).experienceRequirement);
            } else {
                payload2.put("newRequirement", "18+ months"); // fallback
            }

            payload2.put("priority", "CRITICAL");
            payload2.put("flexibility", "NON_NEGOTIABLE");
            payload2.put("justification", "Recent quality issues require more experienced consultants");

            updatedExp.set("payload", payload2);
            events.add(updatedExp);
        }
    }

    private void generateClientSatisfactionSurveys(ArrayNode events) {
        String[] surveyDates = {"2024-03-31", "2024-06-30", "2024-09-30"};

        for (int clientIndex = 0; clientIndex < clientProfiles.size(); clientIndex++) {
            ClientProfile client = clientProfiles.get(clientIndex);
            String clientId = "client-" + (clientIndex + 1);

            for (int surveyIndex = 0; surveyIndex < surveyDates.length; surveyIndex++) {
                ObjectNode survey = createBaseEvent("ClientSatisfactionSurvey",
                        "survey-" + clientId + "-" + surveyIndex,
                        "ClientSatisfactionSurvey");
                survey.put("timestamp", surveyDates[surveyIndex] + "T17:00:00Z");

                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("clientId", clientId);

                // Satisfaction declines over time for AI projects due to skill mismatches
                boolean isAIClient = clientIndex < AI_PROJECTS_COUNT; // First N are AI project clients
                double baseSatisfaction = client.qualityThreshold;
                double decline = isAIClient ? surveyIndex * 0.4 : 0; // AI clients get increasingly dissatisfied
                double satisfaction = Math.max(2.0, baseSatisfaction - decline + (random.nextDouble() - 0.5) * 0.5);

                payload.put("overallSatisfaction", Math.round(satisfaction * 10.0) / 10.0);
                payload.put("previousSatisfaction", surveyIndex == 0 ? satisfaction + 0.5 : satisfaction + 0.3);

                // Detailed category scores
                ObjectNode categories = objectMapper.createObjectNode();
                categories.put("technicalCompetence", Math.max(1.5, satisfaction - 0.2 + random.nextDouble() * 0.4));
                categories.put("domainExpertise", Math.max(1.5, satisfaction - 0.3 + random.nextDouble() * 0.4));
                categories.put("communicationSkills", satisfaction + 0.3 + random.nextDouble() * 0.4);
                categories.put("projectDelivery", Math.max(1.8, satisfaction - 0.1 + random.nextDouble() * 0.3));
                payload.set("categories", categories);

                if (isAIClient && satisfaction < 3.0) {
                    payload.put("comments", "Team lacks AI/ML expertise, causing delays and quality issues");
                    payload.put("recommendation", "Unlikely to recommend");
                    payload.put("renewalRisk", "HIGH");
                } else {
                    payload.put("comments", "Generally satisfied with service delivery");
                    payload.put("recommendation", satisfaction > 4.0 ? "Likely to recommend" : "Neutral");
                    payload.put("renewalRisk", satisfaction > 3.5 ? "LOW" : "MEDIUM");
                }

                survey.set("payload", payload);
                events.add(survey);
            }
        }
    }

    private void generateClientEscalationsAndIncidents(ArrayNode events) {
        // Generate escalations for AI projects (key scenario events)
        int escalationCount = Math.min(AI_PROJECTS_COUNT, CLIENTS_COUNT);

        for (int i = 0; i < escalationCount; i++) {
            String clientId = "client-" + (i + 1);

            ObjectNode escalation = createBaseEvent("ClientEscalation", clientId, "Client");
            escalation.put("timestamp", addDaysToTimestamp("2024-11-18T14:20:00Z", i * 3));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("escalationType", "PROJECT_DELAY");
            payload.put("message", "AI project must start immediately or we invoke contract penalties");
            payload.put("delayTolerance", "0 days");

            // Scale penalty based on organization size and client value
            int basePenalty = 30000;
            double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int penalty = (int)(basePenalty * organizationMultiplier) + random.nextInt(30000);
            payload.put("contractualImplications", "$" + penalty + " penalty per week");

            payload.put("businessImpact", "Q4 targets at risk");
            payload.put("ultimatum", "Resolve resource issues within 48 hours");

            if (i < clientProfiles.size()) {
                payload.put("severity", clientProfiles.get(i).pressureLevel);
            } else {
                payload.put("severity", "HIGH");
            }

            escalation.set("payload", payload);
            events.add(escalation);
        }

        // Generate additional quality incidents for other clients
        int additionalIncidents = Math.min(4, CLIENTS_COUNT - escalationCount);
        for (int i = 0; i < additionalIncidents; i++) {
            String clientId = "client-" + (escalationCount + i + 1);

            ObjectNode incident = createBaseEvent("QualityIncidentReported", clientId, "Client");
            incident.put("timestamp", addDaysToTimestamp("2024-09-01T10:30:00Z", i * 5));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("incidentType", random.nextBoolean() ? "COMMUNICATION_BREAKDOWN" : "DELIVERY_DELAY");
            payload.put("description", "Project milestone missed due to team coordination issues");
            payload.put("severity", "MEDIUM");
            payload.put("projectImpact", (3 + random.nextInt(7)) + " days delay");
            payload.put("clientSatisfactionImpact", -0.5 - random.nextDouble() * 0.5);
            payload.put("correctionAction", "Team restructuring and improved communication protocols");

            incident.set("payload", payload);
            events.add(incident);
        }
    }

    private void generateProjectClientFeedback(ArrayNode events) {
        // Scale completed projects based on organization size
        int completedProjects = Math.max(10, config.getTotalProjects() / 2); // Half of total projects are completed

        // Generate feedback for completed projects
        for (int i = 1; i <= completedProjects; i++) {
            String projectId = "proj-completed-" + i;
            String clientId = "client-" + ((i % CLIENTS_COUNT) + 1);

            ObjectNode feedback = createBaseEvent("ClientFeedbackReceived",
                    "feedback-" + projectId,
                    "ClientFeedback");
            feedback.put("timestamp", addDaysToTimestamp("2023-02-01T16:30:00Z", i * 20));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("projectId", projectId);
            payload.put("clientId", clientId);
            payload.put("feedbackType", "PROJECT_COMPLETION");

            boolean wasStrategicProject = i <= completedProjects * 0.6; // 60% strategic
            double satisfaction = wasStrategicProject ? 4.0 + random.nextDouble() : 3.0 + random.nextDouble();
            payload.put("overallSatisfaction", Math.round(satisfaction * 10.0) / 10.0);

            ObjectNode categories = objectMapper.createObjectNode();
            categories.put("technicalCompetence", satisfaction - 0.1 + random.nextDouble() * 0.3);
            categories.put("projectManagement", satisfaction + 0.1 + random.nextDouble() * 0.2);
            categories.put("communicationSkills", satisfaction + 0.2 + random.nextDouble() * 0.2);
            payload.set("categories", categories);

            if (satisfaction > 4.0) {
                payload.set("positives", createArrayNode(
                        "Excellent technical delivery",
                        "Strong client communication",
                        "On-time project completion"
                ));
                payload.put("impactOnRenewal", "POSITIVE");
            } else {
                payload.set("concerns", createArrayNode(
                        "Some technical gaps in team",
                        "Communication could be improved"
                ));
                payload.put("impactOnRenewal", "NEUTRAL");
            }

            // Add specific feedback details based on project type and organization scale
            if (wasStrategicProject) {
                payload.put("strategicValue", "High impact on client's digital transformation goals");

                int baseOpportunity = 200000;
                double multiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
                int opportunity = (int)(baseOpportunity * multiplier) + random.nextInt(800000);
                payload.put("followUpOpportunities", "$" + opportunity + " in identified next phase work");
            } else {
                payload.put("operationalEfficiency", "Improved internal processes by " + (10 + random.nextInt(25)) + "%");
            }

            feedback.set("payload", payload);
            events.add(feedback);
        }

        // Generate ongoing project feedback (mid-project reviews)
        int ongoingProjects = Math.min(12, config.getTotalProjects() / 3); // 1/3 of projects are ongoing with feedback

        for (int i = 1; i <= ongoingProjects; i++) {
            String projectId = i <= AI_PROJECTS_COUNT ? "proj-ai-" + i : "proj-strategic-" + (i - AI_PROJECTS_COUNT);
            String clientId = "client-" + i;

            ObjectNode midProjectFeedback = createBaseEvent("ClientFeedbackReceived",
                    "mid-feedback-" + projectId,
                    "ClientFeedback");
            midProjectFeedback.put("timestamp", addDaysToTimestamp("2024-10-15T14:00:00Z", i * 3));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("projectId", projectId);
            payload.put("clientId", clientId);
            payload.put("feedbackType", "MID_PROJECT_REVIEW");

            // AI projects have lower satisfaction due to skill gaps
            boolean isAIProject = i <= AI_PROJECTS_COUNT;
            double satisfaction = isAIProject ? 2.5 + random.nextDouble() * 1.0 : 3.5 + random.nextDouble() * 1.0;
            payload.put("overallSatisfaction", Math.round(satisfaction * 10.0) / 10.0);

            ObjectNode categories = objectMapper.createObjectNode();
            categories.put("progressToDate", satisfaction);
            categories.put("teamPerformance", isAIProject ? satisfaction - 0.5 : satisfaction + 0.2);
            categories.put("communicationQuality", satisfaction + 0.3);
            categories.put("issueResolution", isAIProject ? satisfaction - 0.3 : satisfaction + 0.1);
            payload.set("categories", categories);

            if (isAIProject) {
                payload.set("concerns", createArrayNode(
                        "Team struggling with AI/ML implementation complexity",
                        "Delays in achieving technical milestones",
                        "Need more experienced AI specialists on team"
                ));
                payload.put("requestedChanges", "Replace junior team members with AI-experienced consultants");
                payload.put("riskLevel", "HIGH");
            } else {
                payload.set("positives", createArrayNode(
                        "Good progress on deliverables",
                        "Strong team collaboration",
                        "Clear communication from project lead"
                ));
                payload.put("riskLevel", "LOW");
            }

            payload.put("nextReviewDate", addDaysToTimestamp("2024-10-15T00:00:00Z", 30 + i * 3).split("T")[0]);

            midProjectFeedback.set("payload", payload);
            events.add(midProjectFeedback);
        }
    }
}