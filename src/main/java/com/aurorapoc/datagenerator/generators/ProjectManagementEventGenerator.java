package com.aurorapoc.datagenerator.generators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectManagementEventGenerator extends BaseEventGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ProjectManagementEventGenerator.class);

    // Configuration-driven values
    private final int AI_PROJECTS_COUNT;
    private final int TOTAL_PROJECTS_COUNT;
    private final int SENIOR_COHORT_COUNT;
    private final int CLIENTS_COUNT;

    // Calculated values
    private final int strategicProjectsCount;
    private final int operationalProjectsCount;
    private final int completedProjectsCount;

    // Timeline Constants
    private static final String AI_INITIATIVE_START = "2024-07-15T10:00:00Z";

    // Project Technology Stack
    private static final List<String> AI_SKILLS = Arrays.asList(
            "Machine Learning", "Deep Learning", "Computer Vision", "Natural Language Processing",
            "TensorFlow", "PyTorch", "Python", "R", "AWS SageMaker", "Azure ML",
            "Scikit-learn", "Keras", "Apache Spark", "Data Engineering", "MLOps"
    );

    private static final List<String> PROJECT_TECHNOLOGIES = Arrays.asList(
            "Python", "Java", "JavaScript", "TypeScript", "React", "Angular", "Node.js",
            "Spring Boot", "Microservices", "Kubernetes", "Docker", "AWS", "Azure",
            "PostgreSQL", "MongoDB", "Redis", "Elasticsearch"
    );

    // Base client names that scale with organization size
    private static final List<String> BASE_CLIENT_NAMES = Arrays.asList(
            "TechCorp Finance", "MegaBank Corp", "StartupCo", "Global Manufacturing",
            "HealthSystem Inc", "RetailChain Ltd", "FinanceGroup", "TechStart Ventures",
            "Enterprise Corp", "Innovation Labs", "Corporate Services", "Strategic Partners"
    );

    // Extended client names for larger organizations
    private static final List<String> EXTENDED_CLIENT_NAMES = Arrays.asList(
            "Global Tech Solutions", "Finance Dynamics", "Manufacturing Plus", "Healthcare Partners",
            "Retail Innovations", "Energy Solutions", "Digital Ventures", "Consulting Group",
            "Industrial Corp", "Financial Advisors", "Tech Startups Inc", "Healthcare Systems",
            "Banking Solutions", "Logistics Corp", "Media Group", "Insurance Partners"
    );

    // Consultant Names (for cross-references)
    private static final List<String> BASE_CONSULTANT_NAMES = Arrays.asList(
            "Sarah Chen", "Marcus Rodriguez", "David Kim", "Jennifer Walsh",
            "Michael Chang", "Lisa Park", "Tom Wilson", "Anna Zhang",
            "Carlos Martinez", "Emily Johnson", "Raja Patel", "Sophie Miller",
            "Alex Thompson", "Maria Garcia", "James Liu", "Nina Patel",
            "Robert Taylor", "Priya Sharma", "Kevin O'Connor", "Zara Ahmed",
            "Jason Williams", "Amanda Foster", "Ryan Chen", "Isabella Martinez"
    );

    private final List<String> clientNames;
    private final List<String> consultantNames;

    public ProjectManagementEventGenerator(ObjectMapper objectMapper, OrganizationConfiguration config) {
        super(objectMapper, config);
        this.AI_PROJECTS_COUNT = config.getAiProjects();
        this.TOTAL_PROJECTS_COUNT = config.getTotalProjects();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.CLIENTS_COUNT = config.getClients();

        // Calculate project distributions
        this.strategicProjectsCount = (int)((TOTAL_PROJECTS_COUNT - AI_PROJECTS_COUNT) * 0.6); // 60% strategic
        this.operationalProjectsCount = TOTAL_PROJECTS_COUNT - AI_PROJECTS_COUNT - strategicProjectsCount;
        this.completedProjectsCount = Math.max(10, TOTAL_PROJECTS_COUNT / 2); // Half of total projects are completed

        // Create scaled name lists
        this.clientNames = createScaledClientNames(config);
        this.consultantNames = createScaledConsultantNames(config);
    }

    public ProjectManagementEventGenerator(ObjectMapper objectMapper, int startingEventId, OrganizationConfiguration config) {
        super(objectMapper, startingEventId, config);
        this.AI_PROJECTS_COUNT = config.getAiProjects();
        this.TOTAL_PROJECTS_COUNT = config.getTotalProjects();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.CLIENTS_COUNT = config.getClients();

        // Calculate project distributions
        this.strategicProjectsCount = (int)((TOTAL_PROJECTS_COUNT - AI_PROJECTS_COUNT) * 0.6); // 60% strategic
        this.operationalProjectsCount = TOTAL_PROJECTS_COUNT - AI_PROJECTS_COUNT - strategicProjectsCount;
        this.completedProjectsCount = Math.max(10, TOTAL_PROJECTS_COUNT / 2); // Half of total projects are completed

        // Create scaled name lists
        this.clientNames = createScaledClientNames(config);
        this.consultantNames = createScaledConsultantNames(config);
    }

    private List<String> createScaledClientNames(OrganizationConfiguration config) {
        int namesNeeded = config.getClients();
        List<String> names = new ArrayList<>();

        // Start with base names
        names.addAll(BASE_CLIENT_NAMES);

        // Add extended names if needed
        if (namesNeeded > BASE_CLIENT_NAMES.size()) {
            names.addAll(EXTENDED_CLIENT_NAMES);
        }

        // Generate additional names if still needed
        while (names.size() < namesNeeded) {
            String baseName = BASE_CLIENT_NAMES.get(names.size() % BASE_CLIENT_NAMES.size());
            String suffix = " " + ((names.size() / BASE_CLIENT_NAMES.size()) + 1);
            names.add(baseName + suffix);
        }

        return names.subList(0, namesNeeded);
    }

    private List<String> createScaledConsultantNames(OrganizationConfiguration config) {
        int namesNeeded = Math.min(50, config.getTotalConsultants()); // Limit for project assignments
        List<String> names = new ArrayList<>();

        names.addAll(BASE_CONSULTANT_NAMES);

        // Generate additional names if needed
        while (names.size() < namesNeeded) {
            String baseName = BASE_CONSULTANT_NAMES.get(names.size() % BASE_CONSULTANT_NAMES.size());
            String suffix = " " + ((names.size() / BASE_CONSULTANT_NAMES.size()) + 1);
            names.add(baseName + suffix);
        }

        return names.subList(0, namesNeeded);
    }

    @Override
    public String getStreamName() {
        return "project.management";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void generateEvents(ArrayNode events) {
        // Generate AI Projects (for AI talent scenario)
        logger.info("🤖 Generating AI projects ({} projects)...", AI_PROJECTS_COUNT);
        generateAIProjects(events);

        // Generate Strategic vs Operational Projects (for retention scenario)
        logger.info("🎯 Generating strategic and operational projects ({} strategic, {} operational)...",
                strategicProjectsCount, operationalProjectsCount);
        generateStrategicOperationalProjects(events);

        // Generate Project Status Changes and Resource Constraints
        logger.info("⚠️ Generating project status changes and constraints...");
        generateProjectStatusChanges(events);

        // Generate Project Allocations (successful and failed attempts)
        logger.info("👥 Generating project allocations and attempts...");
        generateProjectAllocations(events);

        // Generate Project Performance Tracking
        logger.info("📊 Generating project performance events...");
        generateProjectPerformanceEvents(events);

        // Generate Project Milestone Events
        logger.info("🎯 Generating project milestones and deliverables...");
        generateProjectMilestones(events);

        // Generate Project Budget and Timeline Events
        logger.info("💰 Generating budget and timeline events...");
        generateProjectBudgetTimelineEvents(events);

        // Generate Project Completion Events
        logger.info("✅ Generating project completion events ({} completed projects)...", completedProjectsCount);
        generateProjectCompletionEvents(events);
    }

    private void generateAIProjects(ArrayNode events) {
        String[] projectNames = {
                "Customer Churn Prediction Platform",
                "Fraud Detection ML Pipeline",
                "Recommendation Engine Optimization",
                "Predictive Maintenance System",
                "Natural Language Processing Suite",
                "Computer Vision Analytics Platform",
                "Real-time Analytics Engine",
                "Automated Decision Making System",
                "Deep Learning Model Platform",
                "AI-Powered Business Intelligence"
        };

        String baseDate = AI_INITIATIVE_START;

        for (int i = 0; i < AI_PROJECTS_COUNT; i++) {
            String projectId = "proj-ai-" + (i + 1);

            // Project Creation Event
            ObjectNode projectEvent = createBaseEvent("ProjectCreated", projectId, "Project");
            projectEvent.put("timestamp", addDaysToTimestamp(baseDate, i * 15));

            ObjectNode metadata = (ObjectNode) projectEvent.get("metadata");
            metadata.put("correlationId", "ai-initiative-2024");
            metadata.put("reason", "AI practice strategic project");

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("displayName", projectNames[i % projectNames.length]);
            payload.put("clientId", "client-" + (i + 1));
            payload.put("status", "APPROVED");
            payload.put("tier", "STRATEGIC");

            // Scale budget with organization size
            int baseBudget = 400000;
            double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int budget = (int)(baseBudget * organizationMultiplier) + random.nextInt(800000);
            payload.put("budget", budget);

            payload.put("plannedStartDate", addDaysToTimestamp(baseDate, 30 + i * 15));
            payload.put("plannedEndDate", addDaysToTimestamp(baseDate, 120 + i * 15));
            payload.put("complexity", "HIGH");
            payload.put("strategicImportance", 0.8 + random.nextDouble() * 0.2);

            // Technology stack (AI-focused)
            ArrayNode technologies = objectMapper.createArrayNode();
            technologies.add("Python");
            technologies.add("Machine Learning");
            technologies.add(AI_SKILLS.get(random.nextInt(AI_SKILLS.size())));
            technologies.add("AWS");
            payload.set("technology", technologies);

            projectEvent.set("payload", payload);
            events.add(projectEvent);

            // Project Requirements
            generateProjectRequirements(events, projectId, projectEvent.get("timestamp").asText());
        }
    }

    private void generateProjectRequirements(ArrayNode events, String projectId, String baseTimestamp) {
        // Generate 2-4 skill requirements per AI project
        int requirementCount = 2 + random.nextInt(3);

        for (int i = 0; i < requirementCount; i++) {
            ObjectNode reqEvent = createBaseEvent("ProjectRequirementAdded", projectId, "Project");
            reqEvent.put("timestamp", addMinutesToTimestamp(baseTimestamp, i * 5));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("requirementType", "SKILL_REQUIREMENT");

            if (i == 0) {
                // Always require ML/AI skills for first requirement
                payload.put("skillId", "skill-machine-learning");
                payload.put("skillName", "Machine Learning");
                payload.put("category", "AI_ML");
                payload.put("requiredLevel", "EXPERT");
                payload.put("requiredHeadcount", 2 + random.nextInt(2));
            } else {
                // Other AI/technical skills
                String skill = AI_SKILLS.get(random.nextInt(AI_SKILLS.size()));
                payload.put("skillId", "skill-" + skill.toLowerCase().replace(" ", "-"));
                payload.put("skillName", skill);
                payload.put("category", "AI_ML");
                payload.put("requiredLevel", random.nextBoolean() ? "ADVANCED" : "EXPERT");
                payload.put("requiredHeadcount", 1 + random.nextInt(2));
            }

            payload.put("priority", "CRITICAL");
            reqEvent.set("payload", payload);
            events.add(reqEvent);
        }
    }

    private void generateStrategicOperationalProjects(ArrayNode events) {
        String baseDate = "2023-01-01T09:00:00Z";

        // Generate Strategic Projects
        for (int i = 0; i < strategicProjectsCount; i++) {
            generateProject(events, "proj-strategic-" + (i + 1), "STRATEGIC", baseDate, i);
        }

        // Generate Operational Projects
        for (int i = 0; i < operationalProjectsCount; i++) {
            generateProject(events, "proj-operational-" + (i + 1), "OPERATIONAL", baseDate, strategicProjectsCount + i);
        }
    }

    private void generateProject(ArrayNode events, String projectId, String tier, String baseDate, int index) {
        ObjectNode projectEvent = createBaseEvent("ProjectCreated", projectId, "Project");
        projectEvent.put("timestamp", addDaysToTimestamp(baseDate, index * 10));

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("displayName", generateProjectName(tier));

        // Scale client assignment based on available clients
        int clientIndex = tier.equals("STRATEGIC") ?
                (index % Math.min(4, CLIENTS_COUNT)) + 1 :
                CLIENTS_COUNT; // Internal client
        payload.put("clientId", tier.equals("STRATEGIC") ?
                "client-strategic-" + clientIndex :
                "client-internal");

        payload.put("status", "ACTIVE");
        payload.put("tier", tier);

        // Scale budgets with organization size
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);

        if (tier.equals("STRATEGIC")) {
            int baseBudget = 800000;
            int variance = 1700000;
            payload.put("budget", (int)(baseBudget * organizationMultiplier) + random.nextInt(variance));
            payload.put("complexity", "HIGH");
            payload.put("strategicImportance", 0.7 + random.nextDouble() * 0.3);
            payload.put("clientVisibility", "C_LEVEL");
            payload.put("careerImpactScore", 0.8 + random.nextDouble() * 0.2);
        } else {
            int baseBudget = 100000;
            int variance = 400000;
            payload.put("budget", (int)(baseBudget * organizationMultiplier) + random.nextInt(variance));
            payload.put("complexity", "MEDIUM");
            payload.put("strategicImportance", 0.1 + random.nextDouble() * 0.4);
            payload.put("clientVisibility", "INTERNAL_ONLY");
            payload.put("careerImpactScore", 0.2 + random.nextDouble() * 0.3);
        }

        payload.put("plannedStartDate", addDaysToTimestamp(baseDate, 30 + index * 10));
        payload.put("plannedEndDate", addDaysToTimestamp(baseDate, 180 + index * 10));

        // Technology stack
        ArrayNode technologies = objectMapper.createArrayNode();
        List<String> techStack = getRandomTechnologies(3 + random.nextInt(3));
        for (String tech : techStack) {
            technologies.add(tech);
        }
        payload.set("technology", technologies);

        projectEvent.set("payload", payload);
        events.add(projectEvent);
    }

    private void generateProjectStatusChanges(ArrayNode events) {
        // Generate status changes for AI projects (resource constrained)
        for (int i = 1; i <= AI_PROJECTS_COUNT; i++) {
            String projectId = "proj-ai-" + i;

            ObjectNode statusEvent = createBaseEvent("ProjectStatusChanged", projectId, "Project");
            statusEvent.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 60 + i * 10));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("previousStatus", "APPROVED");
            payload.put("newStatus", "RESOURCE_CONSTRAINED");
            payload.put("statusReason", "Insufficient AI/ML certified consultants available");
            payload.put("delayDays", 10 + random.nextInt(50));
            payload.put("impactAssessment", "HIGH");
            payload.put("clientNotified", true);

            // Skill gaps
            ArrayNode skillGaps = objectMapper.createArrayNode();
            ObjectNode gap = objectMapper.createObjectNode();
            gap.put("skill", "Machine Learning");
            gap.put("required", 3);
            gap.put("available", random.nextInt(2));
            gap.put("gap", 3 - gap.get("available").asInt());
            skillGaps.add(gap);
            payload.set("skillGaps", skillGaps);

            // ADD CORRELATION
            ObjectNode metadata = (ObjectNode) statusEvent.get("metadata");
            metadata.put("correlationId", "ai-initiative-2024");

            statusEvent.set("payload", payload);
            events.add(statusEvent);
        }
    }

    private void generateProjectAllocations(ArrayNode events) {
        // Generate allocation attempts for AI projects (failed allocations)
        for (int i = 1; i <= AI_PROJECTS_COUNT; i++) {
            String projectId = "proj-ai-" + i;

            // Multiple allocation attempts per project
            for (int attempt = 1; attempt <= 2; attempt++) {
                ObjectNode failedAllocation = createBaseEvent("ProjectAllocationAttempted", projectId, "Project");
                failedAllocation.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 90 + i * 15 + attempt * 7));

                ObjectNode metadata = (ObjectNode) failedAllocation.get("metadata");
                metadata.put("userId", attempt == 1 ? "emp-alice-johnson" : "emp-bob-martinez");
                metadata.put("reason", attempt == 1 ? "Emergency allocation under client pressure" : "Second allocation attempt");
                metadata.put("pressureLevel", 0.6 + random.nextDouble() * 0.4);

                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("consultantId", "emp-consultant-" + (attempt + random.nextInt(10)));

                // Use scaled consultant names
                int consultantNameIndex = (attempt + random.nextInt(10)) % consultantNames.size();
                payload.put("consultantName", consultantNames.get(consultantNameIndex));

                payload.put("role", "Senior Developer");
                payload.put("allocationPercentage", 0.8 + random.nextDouble() * 0.2);
                payload.put("skillMatchScore", attempt == 1 ? 0.15 + random.nextDouble() * 0.15 : 0.3 + random.nextDouble() * 0.2);
                payload.put("matchJustification", attempt == 1 ? "Immediate availability" : "Some transferable skills");
                payload.put("riskLevel", "HIGH");

                failedAllocation.set("payload", payload);
                events.add(failedAllocation);
            }
        }

        // Generate successful allocations for strategic projects (retention scenario)
        int strategicAllocations = Math.min(SENIOR_COHORT_COUNT, strategicProjectsCount);

        for (int i = 1; i <= strategicAllocations; i++) {
            String projectId = "proj-strategic-" + ((i % strategicProjectsCount) + 1);
            String consultantId = "emp-senior-cohort-" + i;

            ObjectNode allocation = createBaseEvent("ProjectAllocationCompleted", projectId, "Project");
            allocation.put("timestamp", addDaysToTimestamp("2023-03-15T10:00:00Z", i * 5));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("consultantId", consultantId);
            payload.put("consultantName", consultantNames.get(i % consultantNames.size()));
            payload.put("role", "Senior Consultant");
            payload.put("allocationPercentage", 0.8 + random.nextDouble() * 0.2);
            payload.put("skillMatchScore", 0.75 + random.nextDouble() * 0.2);
            payload.put("clientExposure", "DIRECT");
            payload.put("leadershipRole", random.nextBoolean());

            allocation.set("payload", payload);
            events.add(allocation);
        }

        // Generate operational project allocations (for operational track seniors)
        int operationalAllocations = Math.min(operationalProjectsCount * 2, SENIOR_COHORT_COUNT - strategicAllocations);

        for (int i = 0; i < operationalAllocations; i++) {
            int consultantIndex = strategicAllocations + i + 1;
            if (consultantIndex > SENIOR_COHORT_COUNT) break;

            String projectId = "proj-operational-" + ((i % operationalProjectsCount) + 1);
            String consultantId = "emp-senior-cohort-" + consultantIndex;

            ObjectNode allocation = createBaseEvent("ProjectAllocationCompleted", projectId, "Project");
            allocation.put("timestamp", addDaysToTimestamp("2023-03-15T10:00:00Z", consultantIndex * 5));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("consultantId", consultantId);
            payload.put("consultantName", consultantNames.get(consultantIndex % consultantNames.size()));
            payload.put("role", "Senior Consultant");
            payload.put("allocationPercentage", 0.9 + random.nextDouble() * 0.1);
            payload.put("skillMatchScore", 0.8 + random.nextDouble() * 0.15);
            payload.put("clientExposure", "INTERNAL_ONLY");
            payload.put("leadershipRole", false);

            allocation.set("payload", payload);
            events.add(allocation);
        }
    }

    private void generateProjectPerformanceEvents(ArrayNode events) {
        // Generate performance tracking for failed AI allocations
        for (int i = 1; i <= AI_PROJECTS_COUNT; i++) {
            String projectId = "proj-ai-" + i;

            ObjectNode performance = createBaseEvent("ProjectPerformanceTracked", projectId, "Project");
            performance.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 120 + i * 15));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("allocationId", "alloc-" + projectId + "-failed");
            payload.put("consultantId", "emp-consultant-" + (1 + random.nextInt(10)));
            payload.put("projectId", projectId);

            // Poor performance metrics due to skill mismatch
            ObjectNode metrics = objectMapper.createObjectNode();
            metrics.put("taskCompletion", 0.3 + random.nextDouble() * 0.3);
            metrics.put("codeQuality", 0.2 + random.nextDouble() * 0.3);
            metrics.put("clientSatisfaction", 1.5 + random.nextDouble() * 1.0);
            metrics.put("knowledgeTransfer", 0.1 + random.nextDouble() * 0.3);
            payload.set("performanceMetrics", metrics);

            ArrayNode issues = objectMapper.createArrayNode();
            issues.add("Lack of ML domain knowledge");
            issues.add("Unable to implement core algorithms");
            issues.add("Client concerns about technical competence");
            payload.set("issues", issues);

            ObjectNode businessImpact = objectMapper.createObjectNode();
            businessImpact.put("projectDelay", (2 + random.nextInt(4)) + " weeks");

            // Scale business impact with organization size
            int baseBudgetImpact = 50000;
            double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int budgetImpact = (int)(baseBudgetImpact * organizationMultiplier) + random.nextInt(100000);
            businessImpact.put("budgetImpact", budgetImpact);

            businessImpact.put("clientSatisfactionDrop", -1.0 - random.nextDouble());
            payload.set("businessImpact", businessImpact);

            payload.put("correctionRequired", true);

            performance.set("payload", payload);
            events.add(performance);
        }
    }

    private void generateProjectMilestones(ArrayNode events) {
        // Generate 2-3 milestones per strategic project (to show career impact)
        for (int i = 1; i <= Math.min(15, strategicProjectsCount); i++) {
            String projectId = "proj-strategic-" + i;
            String baseDate = addDaysToTimestamp("2023-03-15T10:00:00Z", i * 10);

            // Milestone 1: Project Kickoff
            ObjectNode kickoff = createBaseEvent("ProjectMilestoneCompleted", projectId, "Project");
            kickoff.put("timestamp", addDaysToTimestamp(baseDate, 7));

            ObjectNode kickoffPayload = objectMapper.createObjectNode();
            kickoffPayload.put("milestoneName", "Project Kickoff");
            kickoffPayload.put("completionStatus", "COMPLETED");
            kickoffPayload.put("completionDate", addDaysToTimestamp(baseDate, 7).split("T")[0]);
            kickoffPayload.put("clientSatisfaction", 4.0 + random.nextDouble());
            kickoffPayload.put("budgetStatus", "ON_TRACK");
            kickoff.set("payload", kickoffPayload);
            events.add(kickoff);

            // Milestone 2: Mid-project Review
            ObjectNode midpoint = createBaseEvent("ProjectMilestoneCompleted", projectId, "Project");
            midpoint.put("timestamp", addDaysToTimestamp(baseDate, 60));

            ObjectNode midPayload = objectMapper.createObjectNode();
            midPayload.put("milestoneName", "Mid-project Review");
            midPayload.put("completionStatus", random.nextBoolean() ? "COMPLETED" : "DELAYED");
            midPayload.put("clientSatisfaction", 3.5 + random.nextDouble() * 1.5);
            midPayload.put("budgetStatus", random.nextBoolean() ? "ON_TRACK" : "OVER_BUDGET");
            midpoint.set("payload", midPayload);
            events.add(midpoint);

            // Milestone 3: Final Delivery
            ObjectNode delivery = createBaseEvent("ProjectMilestoneCompleted", projectId, "Project");
            delivery.put("timestamp", addDaysToTimestamp(baseDate, 120));

            ObjectNode deliveryPayload = objectMapper.createObjectNode();
            deliveryPayload.put("milestoneName", "Final Delivery");
            deliveryPayload.put("completionStatus", "COMPLETED");
            deliveryPayload.put("clientSatisfaction", 4.0 + random.nextDouble());
            deliveryPayload.put("budgetStatus", random.nextDouble() > 0.7 ? "OVER_BUDGET" : "ON_TRACK");
            delivery.set("payload", deliveryPayload);
            events.add(delivery);
        }
    }

    private void generateProjectBudgetTimelineEvents(ArrayNode events) {
        // Generate budget variance events for projects
        int projectsWithVariance = (int)(TOTAL_PROJECTS_COUNT * 0.4); // 40% have budget issues

        for (int i = 1; i <= projectsWithVariance; i++) {
            String projectId;
            String baseDate;

            if (i <= AI_PROJECTS_COUNT) {
                projectId = "proj-ai-" + i;
                baseDate = addDaysToTimestamp(AI_INITIATIVE_START, i * 15);
            } else if (i <= AI_PROJECTS_COUNT + strategicProjectsCount) {
                projectId = "proj-strategic-" + (i - AI_PROJECTS_COUNT);
                baseDate = addDaysToTimestamp("2023-01-01T09:00:00Z", (i - AI_PROJECTS_COUNT) * 10);
            } else {
                projectId = "proj-operational-" + (i - AI_PROJECTS_COUNT - strategicProjectsCount);
                baseDate = addDaysToTimestamp("2023-01-01T09:00:00Z", (i - AI_PROJECTS_COUNT - strategicProjectsCount) * 10);
            }

            // Budget variance event
            ObjectNode budgetEvent = createBaseEvent("ProjectBudgetVariance", projectId, "Project");
            budgetEvent.put("timestamp", addDaysToTimestamp(baseDate, 45));

            ObjectNode payload = objectMapper.createObjectNode();
            double variance = (random.nextDouble() - 0.5) * 0.6; // -30% to +30%
            payload.put("budgetVariance", variance);

            // Scale budgets with organization size
            double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int originalBudget = (int)((500000 + random.nextInt(1000000)) * organizationMultiplier);

            payload.put("originalBudget", originalBudget);
            payload.put("forecastBudget", originalBudget * (1 + variance));
            payload.put("varianceReason", variance > 0 ? "Resource constraints requiring premium contractors" : "Efficient delivery");
            budgetEvent.set("payload", payload);
            events.add(budgetEvent);

            // Timeline variance event
            if (random.nextDouble() > 0.5) { // 50% also have timeline changes
                ObjectNode timelineEvent = createBaseEvent("ProjectTimelineVariance", projectId, "Project");
                timelineEvent.put("timestamp", addDaysToTimestamp(baseDate, 60));

                ObjectNode timelinePayload = objectMapper.createObjectNode();
                int delayDays = random.nextInt(60) - 15; // -15 to +45 days
                timelinePayload.put("timelineVarianceDays", delayDays);
                timelinePayload.put("originalDuration", 90 + random.nextInt(180));
                timelinePayload.put("forecastDuration", timelinePayload.get("originalDuration").asInt() + delayDays);
                timelinePayload.put("varianceReason", delayDays > 0 ? "Resource allocation delays" : "Accelerated delivery");
                timelineEvent.set("payload", timelinePayload);
                events.add(timelineEvent);
            }
        }
    }

    private void generateProjectCompletionEvents(ArrayNode events) {
        // Generate completion events for completed projects
        for (int i = 1; i <= completedProjectsCount; i++) {
            String projectId = "proj-completed-" + i;

            // Project creation (backdated)
            ObjectNode creation = createBaseEvent("ProjectCreated", projectId, "Project");
            creation.put("timestamp", addDaysToTimestamp("2022-06-01T09:00:00Z", i * 15));

            ObjectNode creationPayload = objectMapper.createObjectNode();
            boolean isStrategic = i <= (completedProjectsCount * 0.6); // 60% strategic
            creationPayload.put("displayName", generateProjectName(isStrategic ? "STRATEGIC" : "OPERATIONAL"));
            creationPayload.put("status", "ACTIVE");
            creationPayload.put("tier", isStrategic ? "STRATEGIC" : "OPERATIONAL");

            // Scale budget with organization size
            double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int baseBudget = isStrategic ? 800000 : 100000;
            int variance = isStrategic ? 1200000 : 400000;
            creationPayload.put("budget", (int)(baseBudget * organizationMultiplier) + random.nextInt(variance));

            creation.set("payload", creationPayload);
            events.add(creation);

            // Project completion
            ObjectNode completion = createBaseEvent("ProjectCompleted", projectId, "Project");
            completion.put("timestamp", addDaysToTimestamp("2023-01-01T17:00:00Z", i * 20));

            ObjectNode completionPayload = objectMapper.createObjectNode();
            completionPayload.put("previousStatus", "ACTIVE");
            completionPayload.put("newStatus", "COMPLETED");
            completionPayload.put("actualDuration", 120 + random.nextInt(180));
            completionPayload.put("plannedDuration", 150);

            // Success metrics vary by project tier and team composition
            boolean hadStrategicTeam = isStrategic;
            completionPayload.put("clientSatisfaction", hadStrategicTeam ? 4.0 + random.nextDouble() : 3.0 + random.nextDouble());
            completionPayload.put("budgetPerformance", hadStrategicTeam ? 0.95 + random.nextDouble() * 0.15 : 1.0 + random.nextDouble() * 0.3);
            completionPayload.put("deliveryOnTime", hadStrategicTeam ? random.nextDouble() > 0.2 : random.nextDouble() > 0.4);
            completionPayload.put("teamStability", hadStrategicTeam ? 0.8 + random.nextDouble() * 0.2 : 0.6 + random.nextDouble() * 0.3);

            // Scale financial outcomes with organization size
            if (hadStrategicTeam) {
                int baseRevenue = 500000;
                int additionalRevenue = (int)(baseRevenue * organizationMultiplier) + random.nextInt(300000);
                completionPayload.put("additionalRevenueGenerated", additionalRevenue);
            }

            completion.set("payload", completionPayload);
            events.add(completion);
        }
    }

    // Helper methods
    private String generateProjectName(String tier) {
        String[] strategicProjects = {
                "Fortune 500 Digital Transformation",
                "Banking Modernization Initiative",
                "Healthcare AI Platform",
                "Supply Chain Optimization",
                "Customer Experience Platform",
                "Enterprise Cloud Migration",
                "Data Analytics Modernization",
                "Cybersecurity Enhancement",
                "Digital Innovation Program",
                "Omnichannel Customer Platform",
                "Advanced Analytics Implementation",
                "Business Intelligence Modernization",
                "Customer Data Platform",
                "AI-Powered Decision Engine",
                "Digital Commerce Platform",
                "Real-time Data Processing System"
        };

        String[] operationalProjects = {
                "Internal IT Modernization",
                "Legacy System Migration",
                "Compliance System Update",
                "Employee Portal Upgrade",
                "Reporting System Enhancement",
                "Database Optimization",
                "Network Infrastructure Update",
                "Security Patch Management",
                "Internal Process Automation",
                "HR System Integration",
                "Financial Reporting Automation",
                "Document Management System",
                "Workflow Optimization Platform",
                "Internal Communication Tool",
                "Resource Planning System",
                "Quality Assurance Framework"
        };

        if (tier.equals("STRATEGIC")) {
            return strategicProjects[random.nextInt(strategicProjects.length)];
        } else {
            return operationalProjects[random.nextInt(operationalProjects.length)];
        }
    }

    private List<String> getRandomTechnologies(int count) {
        List<String> technologies = new ArrayList<>(PROJECT_TECHNOLOGIES);
        Collections.shuffle(technologies, random);
        return technologies.subList(0, Math.min(count, technologies.size()));
    }
}