package com.aurorapoc.datagenerator.generators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ResourceAllocationEventGenerator extends BaseEventGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ResourceAllocationEventGenerator.class);

    // Configuration-driven values
    private final int AI_SPECIALISTS_COUNT;
    private final int AI_PROJECTS_COUNT;
    private final int SENIOR_COHORT_COUNT;
    private final int TOTAL_PROJECTS_COUNT;
    private final int TOTAL_CONSULTANTS;

    // Calculated values - make them class-level variables
    private final int strategicAllocationsCount;
    private final int operationalAllocationsCount;

    // Timeline Constants
    private static final String AI_INITIATIVE_START = "2024-07-15T10:00:00Z";

    // Base consultant names that scale with organization size
    private static final List<String> BASE_CONSULTANT_NAMES = Arrays.asList(
            "Sarah Chen", "Marcus Rodriguez", "David Kim", "Jennifer Walsh",
            "Michael Chang", "Lisa Park", "Tom Wilson", "Anna Zhang",
            "Carlos Martinez", "Emily Johnson", "Raja Patel", "Sophie Miller",
            "Alex Thompson", "Maria Garcia", "James Liu", "Nina Patel",
            "Robert Taylor", "Priya Sharma", "Kevin O'Connor", "Zara Ahmed",
            "Jason Williams", "Amanda Foster", "Ryan Chen", "Isabella Martinez"
    );

    // Extended consultant names for larger organizations
    private static final List<String> EXTENDED_CONSULTANT_NAMES = Arrays.asList(
            "Ahmed Hassan", "Grace Liu", "Diego Morales", "Fatima Al-Rashid",
            "Lucas Santos", "Mei Wang", "Gabriel Okafor", "Asha Patel",
            "Benjamin Clark", "Yuki Tanaka", "Elena Volkov", "Omar Singh",
            "Chloe Anderson", "Raj Mehta", "Sofia Gonzalez", "Kwame Asante",
            "Lily Zhang", "Hassan Ali", "Emma Johansson", "Arjun Nair",
            "Maya Patel", "Viktor Petrov", "Zoe Campbell", "Amit Sharma",
            "Nora Al-Zahra", "Luis Herrera", "Sakura Yamamoto", "Ibrahim Kone"
    );

    private final List<String> consultantNames;

    public ResourceAllocationEventGenerator(ObjectMapper objectMapper, OrganizationConfiguration config) {
        super(objectMapper, config);
        this.AI_SPECIALISTS_COUNT = config.getAiSpecialists();
        this.AI_PROJECTS_COUNT = config.getAiProjects();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.TOTAL_PROJECTS_COUNT = config.getTotalProjects();
        this.TOTAL_CONSULTANTS = config.getTotalConsultants();
        this.consultantNames = createScaledConsultantNames(config);

        // Calculate allocation counts once
        this.strategicAllocationsCount = Math.min(SENIOR_COHORT_COUNT, (TOTAL_PROJECTS_COUNT * 6) / 10); // 60% strategic
        this.operationalAllocationsCount = SENIOR_COHORT_COUNT - strategicAllocationsCount;
    }

    public ResourceAllocationEventGenerator(ObjectMapper objectMapper, int startingEventId, OrganizationConfiguration config) {
        super(objectMapper, startingEventId, config);
        this.AI_SPECIALISTS_COUNT = config.getAiSpecialists();
        this.AI_PROJECTS_COUNT = config.getAiProjects();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.TOTAL_PROJECTS_COUNT = config.getTotalProjects();
        this.TOTAL_CONSULTANTS = config.getTotalConsultants();
        this.consultantNames = createScaledConsultantNames(config);

        // Calculate allocation counts once
        this.strategicAllocationsCount = Math.min(SENIOR_COHORT_COUNT, (TOTAL_PROJECTS_COUNT * 6) / 10); // 60% strategic
        this.operationalAllocationsCount = SENIOR_COHORT_COUNT - strategicAllocationsCount;
    }

    private List<String> createScaledConsultantNames(OrganizationConfiguration config) {
        int namesNeeded = config.getTotalConsultants();
        List<String> names = new java.util.ArrayList<>();

        // Start with base names
        names.addAll(BASE_CONSULTANT_NAMES);

        // Add extended names if needed
        if (namesNeeded > BASE_CONSULTANT_NAMES.size()) {
            names.addAll(EXTENDED_CONSULTANT_NAMES);
        }

        // Generate additional names if still needed
        while (names.size() < namesNeeded) {
            String baseName = BASE_CONSULTANT_NAMES.get(names.size() % BASE_CONSULTANT_NAMES.size());
            String suffix = " " + ((names.size() / BASE_CONSULTANT_NAMES.size()) + 1);
            names.add(baseName + suffix);
        }

        return names.subList(0, namesNeeded);
    }

    @Override
    public String getStreamName() {
        return "resource.allocation";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void generateEvents(ArrayNode events) {
        // Generate Allocation Requests and Decisions
        logger.info("📋 Generating allocation requests and decisions ({} AI projects, {} total projects)...",
                AI_PROJECTS_COUNT, TOTAL_PROJECTS_COUNT);
        generateAllocationRequestsAndDecisions(events);

        // Generate Allocation Performance Tracking
        logger.info("📊 Generating allocation performance tracking...");
        generateAllocationPerformanceTracking(events);

        // Generate Utilization Reports
        logger.info("📈 Generating utilization reports ({} consultants)...", TOTAL_CONSULTANTS);
        generateUtilizationReports(events);
    }

    private void generateAllocationRequestsAndDecisions(ArrayNode events) {
        // Generate allocation requests for AI projects
        for (int i = 1; i <= AI_PROJECTS_COUNT; i++) {
            String projectId = "proj-ai-" + i;

            // Allocation request
            ObjectNode request = createBaseEvent("AllocationRequestReceived", "req-" + projectId, "AllocationRequest");
            request.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 45 + i * 10));

            ObjectNode requestPayload = objectMapper.createObjectNode();
            requestPayload.put("projectId", projectId);
            requestPayload.set("requiredSkills", createArrayNode("Machine Learning", "Python"));
            requestPayload.put("headcount", 3);
            requestPayload.put("urgency", "HIGH");
            requestPayload.put("clientId", "client-" + i);

            request.set("payload", requestPayload);
            events.add(request);

            // Allocation analysis (algorithm recommendation)
            ObjectNode analysis = createBaseEvent("AllocationAnalysisCompleted", "req-" + projectId, "AllocationRequest");
            analysis.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 47 + i * 10));

            ObjectNode analysisPayload = objectMapper.createObjectNode();
            ArrayNode availableConsultants = objectMapper.createArrayNode();

            // AI specialists (high match, available)
            if (i <= AI_SPECIALISTS_COUNT) {
                ObjectNode aiConsultant = objectMapper.createObjectNode();
                aiConsultant.put("consultantId", "emp-ai-specialist-" + i);
                aiConsultant.put("skillMatch", 0.88 + random.nextDouble() * 0.1);
                aiConsultant.put("availability", 1.0);
                aiConsultant.put("overallScore", 0.85 + random.nextDouble() * 0.1);
                availableConsultants.add(aiConsultant);
            }

            // Non-AI consultant (poor match, available)
            ObjectNode nonAiConsultant = objectMapper.createObjectNode();
            nonAiConsultant.put("consultantId", "emp-consultant-" + i);
            nonAiConsultant.put("skillMatch", 0.15 + random.nextDouble() * 0.15);
            nonAiConsultant.put("availability", 1.0);
            nonAiConsultant.put("overallScore", 0.3 + random.nextDouble() * 0.1);
            availableConsultants.add(nonAiConsultant);

            analysisPayload.set("availableConsultants", availableConsultants);
            analysisPayload.put("recommendedConsultant",
                    i <= AI_SPECIALISTS_COUNT ? "emp-ai-specialist-" + i : "emp-consultant-" + i);
            analysisPayload.put("gapCount", 2); // Still need 2 more

            analysis.set("payload", analysisPayload);
            events.add(analysis);

            // Allocation decision (poor match due to pressure - key AI paradox)
            ObjectNode decision = createBaseEvent("AllocationDecisionMade", "req-" + projectId, "AllocationRequest");
            decision.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 50 + i * 10));

            ObjectNode decisionPayload = objectMapper.createObjectNode();
            decisionPayload.put("decisionType", "OVERRIDE_RECOMMENDATION");
            decisionPayload.put("allocatedConsultant", "emp-consultant-" + i);
            decisionPayload.put("algorithmRecommendation",
                    i <= AI_SPECIALISTS_COUNT ? "emp-ai-specialist-" + i : "emp-consultant-" + i);
            decisionPayload.put("overrideReason", "Client pressure requires immediate allocation");
            decisionPayload.put("skillMatch", 0.25 + random.nextDouble() * 0.15);
            decisionPayload.put("riskAcknowledged", true);
            decisionPayload.put("managerOverride", "emp-alice-johnson");

            decision.set("payload", decisionPayload);
            events.add(decision);
        }

        // Generate allocation requests for strategic projects (use class variable)
        for (int i = 1; i <= strategicAllocationsCount; i++) {
            String projectId = "proj-strategic-" + i;
            String consultantId = "emp-senior-cohort-" + i;

            // Allocation request
            ObjectNode request = createBaseEvent("AllocationRequestReceived", "req-" + projectId, "AllocationRequest");
            request.put("timestamp", addDaysToTimestamp("2023-03-01T09:00:00Z", i * 5));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("projectId", projectId);
            payload.set("requiredSkills", createArrayNode("Java", "System Architecture", "Client Management"));
            payload.put("headcount", 2);
            payload.put("urgency", "MEDIUM");
            payload.put("clientId", "client-strategic-" + ((i % Math.min(4, config.getClients())) + 1));

            request.set("payload", payload);
            events.add(request);

            // Successful allocation
            ObjectNode allocation = createBaseEvent("AllocationDecisionMade", "req-" + projectId, "AllocationRequest");
            allocation.put("timestamp", addDaysToTimestamp("2023-03-01T09:00:00Z", i * 5 + 2));

            ObjectNode allocationPayload = objectMapper.createObjectNode();
            allocationPayload.put("decisionType", "FOLLOW_RECOMMENDATION");
            allocationPayload.put("allocatedConsultant", consultantId);
            allocationPayload.put("algorithmRecommendation", consultantId);
            allocationPayload.put("skillMatch", 0.75 + random.nextDouble() * 0.2);
            allocationPayload.put("clientApproved", true);
            allocationPayload.put("riskLevel", "LOW");

            allocation.set("payload", allocationPayload);
            events.add(allocation);
        }

        // Generate allocation requests for operational projects (use class variable)
        for (int i = 0; i < operationalAllocationsCount; i++) {
            int consultantIndex = strategicAllocationsCount + i + 1;
            String projectId = "proj-operational-" + ((i % 10) + 1);
            String consultantId = "emp-senior-cohort-" + consultantIndex;

            // Allocation request
            ObjectNode request = createBaseEvent("AllocationRequestReceived", "req-op-" + consultantIndex, "AllocationRequest");
            request.put("timestamp", addDaysToTimestamp("2023-03-10T09:00:00Z", consultantIndex * 4));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("projectId", projectId);
            payload.set("requiredSkills", createArrayNode("Java", "Database Management"));
            payload.put("headcount", 1);
            payload.put("urgency", "LOW");
            payload.put("clientId", "client-internal");

            request.set("payload", payload);
            events.add(request);

            // Operational allocation
            ObjectNode allocation = createBaseEvent("AllocationDecisionMade", "req-op-" + consultantIndex, "AllocationRequest");
            allocation.put("timestamp", addDaysToTimestamp("2023-03-10T09:00:00Z", consultantIndex * 4 + 1));

            ObjectNode allocationPayload = objectMapper.createObjectNode();
            allocationPayload.put("decisionType", "STANDARD_ALLOCATION");
            allocationPayload.put("allocatedConsultant", consultantId);
            allocationPayload.put("skillMatch", 0.8 + random.nextDouble() * 0.15);
            allocationPayload.put("clientApproved", true);
            allocationPayload.put("projectTier", "OPERATIONAL");
            allocationPayload.put("careerImpact", "MAINTENANCE");

            allocation.set("payload", allocationPayload);
            events.add(allocation);
        }
    }

    private void generateAllocationPerformanceTracking(ArrayNode events) {
        // Track poor performance of AI project mismatched allocations
        for (int i = 1; i <= AI_PROJECTS_COUNT; i++) {
            ObjectNode performance = createBaseEvent("AllocationPerformanceTracked", "perf-ai-" + i, "AllocationPerformance");
            performance.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 90 + i * 15));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("projectId", "proj-ai-" + i);
            payload.put("consultantId", "emp-consultant-" + i);
            payload.put("allocationId", "alloc-ai-" + i);

            ObjectNode metrics = objectMapper.createObjectNode();
            metrics.put("taskCompletion", 0.3 + random.nextDouble() * 0.3);
            metrics.put("codeQuality", 0.2 + random.nextDouble() * 0.3);
            metrics.put("clientSatisfaction", 2.0 + random.nextDouble());
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

        // Track good performance of strategic project allocations (now using class variable)
        int strategicPerformanceCount = Math.min(10, strategicAllocationsCount);

        for (int i = 1; i <= strategicPerformanceCount; i++) {
            ObjectNode performance = createBaseEvent("AllocationPerformanceTracked", "perf-strategic-" + i, "AllocationPerformance");
            performance.put("timestamp", addDaysToTimestamp("2023-06-01T14:00:00Z", i * 20));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("projectId", "proj-strategic-" + i);
            payload.put("consultantId", "emp-senior-cohort-" + i);
            payload.put("allocationId", "alloc-strategic-" + i);

            ObjectNode metrics = objectMapper.createObjectNode();
            metrics.put("taskCompletion", 0.85 + random.nextDouble() * 0.15);
            metrics.put("codeQuality", 0.8 + random.nextDouble() * 0.2);
            metrics.put("clientSatisfaction", 4.0 + random.nextDouble());
            metrics.put("leadershipSkills", 0.7 + random.nextDouble() * 0.3);
            payload.set("performanceMetrics", metrics);

            ArrayNode strengths = objectMapper.createArrayNode();
            strengths.add("Excellent client relationship management");
            strengths.add("Strong technical leadership");
            strengths.add("Proactive problem solving");
            payload.set("strengths", strengths);

            ObjectNode businessImpact = objectMapper.createObjectNode();
            businessImpact.put("clientSatisfactionIncrease", 0.3 + random.nextDouble() * 0.4);
            businessImpact.put("projectAcceleration", "1-2 weeks ahead of schedule");

            // Scale additional opportunities with organization size
            int baseOpportunity = 200000;
            double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int additionalOpportunity = (int)(baseOpportunity * organizationMultiplier) + random.nextInt(500000);
            businessImpact.put("additionalOpportunities", "$" + additionalOpportunity + " follow-on work identified");
            payload.set("businessImpact", businessImpact);

            payload.put("promotionReadiness", "HIGH");

            performance.set("payload", payload);
            events.add(performance);
        }

        // Generate allocation corrections for failed AI allocations
        for (int i = 1; i <= AI_PROJECTS_COUNT; i++) {
            ObjectNode correction = createBaseEvent("AllocationCorrectionInitiated", "correction-ai-" + i, "AllocationCorrection");
            correction.put("timestamp", addDaysToTimestamp(AI_INITIATIVE_START, 120 + i * 10));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("originalAllocation", "emp-consultant-" + i);

            // Correct allocation to AI specialist if available
            String correctedAllocation = i <= AI_SPECIALISTS_COUNT ? "emp-ai-specialist-" + i : "contractor-ai-" + i;
            payload.put("correctedAllocation", correctedAllocation);
            payload.put("correctionReason", "Performance issues due to skill mismatch");

            ObjectNode transitionPlan = objectMapper.createObjectNode();
            transitionPlan.put("knowledgeTransfer", "3 days");
            transitionPlan.put("clientReintroduction", "1 day");
            transitionPlan.put("projectReset", "2 days");
            payload.set("transitionPlan", transitionPlan);

            ObjectNode expectedRecovery = objectMapper.createObjectNode();
            expectedRecovery.put("timeToProductivity", "1 week");
            expectedRecovery.put("projectAcceleration", "4-6 weeks");
            expectedRecovery.put("clientSatisfactionRecovery", 2.5 + random.nextDouble());
            payload.set("expectedRecovery", expectedRecovery);

            ArrayNode preventionMeasures = objectMapper.createArrayNode();
            preventionMeasures.add("Mandatory skill validation >70% for client projects");
            preventionMeasures.add("AI allocation approval workflow");
            preventionMeasures.add("Manager AI literacy training");
            payload.set("preventionMeasures", preventionMeasures);

            correction.set("payload", payload);
            events.add(correction);
        }
    }

    private void generateUtilizationReports(ArrayNode events) {
        // Monthly utilization reports showing AI specialist bench time (12 months)
        String[] reportMonths = {
                "2024-01-31", "2024-02-29", "2024-03-31", "2024-04-30", "2024-05-31", "2024-06-30",
                "2024-07-31", "2024-08-31", "2024-09-30", "2024-10-31", "2024-11-30", "2024-12-31"
        };

        for (String month : reportMonths) {
            // Overall company utilization
            ObjectNode companyReport = createBaseEvent("ResourceUtilizationAnalysis", "util-company-" + month, "UtilizationReport");
            companyReport.put("timestamp", month + "T23:59:00Z");

            ObjectNode companyPayload = objectMapper.createObjectNode();
            companyPayload.put("reportPeriod", month.substring(0, 7));
            companyPayload.put("scope", "COMPANY_WIDE");
            companyPayload.put("totalConsultants", TOTAL_CONSULTANTS);
            companyPayload.put("utilizationRate", 0.75 + random.nextDouble() * 0.2);

            // Scale bench consultants with organization size
            int baseBenchConsultants = 5;
            double benchMultiplier = Math.max(1.0, TOTAL_CONSULTANTS / 150.0);
            int benchConsultants = (int)(baseBenchConsultants * benchMultiplier) + random.nextInt(15);
            companyPayload.put("benchConsultants", benchConsultants);

            companyPayload.put("revenueTarget", 0.85);
            companyPayload.put("actualRevenue", 0.82 + random.nextDouble() * 0.1);

            companyReport.set("payload", companyPayload);
            events.add(companyReport);

            // AI practice utilization (shows the bench time problem)
            ObjectNode aiReport = createBaseEvent("ResourceUtilizationAnalysis", "util-ai-" + month, "UtilizationReport");
            aiReport.put("timestamp", month + "T23:59:00Z");

            ObjectNode aiPayload = objectMapper.createObjectNode();
            aiPayload.put("reportPeriod", month.substring(0, 7));
            aiPayload.put("practice", "AI_ML");

            ArrayNode consultants = objectMapper.createArrayNode();
            boolean isEarlyMonth = month.compareTo("2024-10-31") <= 0;

            for (int i = 1; i <= AI_SPECIALISTS_COUNT; i++) {
                ObjectNode consultant = objectMapper.createObjectNode();
                consultant.put("consultantId", "emp-ai-specialist-" + i);

                if (isEarlyMonth) {
                    // Before corrections - high bench time
                    consultant.put("utilization", 0.0);
                    consultant.put("status", "BENCH");
                    consultant.put("daysBench", 25 + random.nextInt(20));

                    // Scale cost impact with organization size (higher rates for larger orgs)
                    int baseCostImpact = 20000;
                    double costMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
                    int costImpact = (int)(baseCostImpact * costMultiplier) + random.nextInt(15000);
                    consultant.put("costImpact", costImpact);
                } else {
                    // After corrections - better utilization
                    consultant.put("utilization", 0.6 + random.nextDouble() * 0.3);
                    consultant.put("status", random.nextDouble() > 0.3 ? "ALLOCATED" : "BENCH");
                    consultant.put("daysBench", random.nextInt(10));
                    consultant.put("costImpact", random.nextInt(5000));
                }

                consultants.add(consultant);
            }
            aiPayload.set("consultants", consultants);

            ObjectNode summary = objectMapper.createObjectNode();
            summary.put("totalConsultants", AI_SPECIALISTS_COUNT);
            summary.put("utilizationRate", isEarlyMonth ? 0.0 : (0.6 + random.nextDouble() * 0.25));

            // Scale bench cost with organization size
            int baseBenchCost = 160000;
            double costMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int benchCost = isEarlyMonth ?
                    (int)(baseBenchCost * costMultiplier) :
                    (20000 + random.nextInt((int)(80000 * costMultiplier)));
            summary.put("benchCost", benchCost);

            summary.put("targetUtilization", 0.85);
            summary.put("utilizationGap", isEarlyMonth ? -0.85 : (-0.25 + random.nextDouble() * 0.2));
            aiPayload.set("practiceSummary", summary);

            ObjectNode businessImpact = objectMapper.createObjectNode();

            // Scale business impact with organization size
            int baseRevenueOpportunity = 425000;
            int baseProfitImpact = 160000;
            double impactMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);

            int revenueOpportunity = isEarlyMonth ?
                    (int)(baseRevenueOpportunity * impactMultiplier) :
                    (100000 + random.nextInt((int)(200000 * impactMultiplier)));
            businessImpact.put("revenueOpportunity", revenueOpportunity);

            int profitImpact = isEarlyMonth ?
                    -(int)(baseProfitImpact * impactMultiplier) :
                    (-50000 + random.nextInt((int)(100000 * impactMultiplier)));
            businessImpact.put("profitImpact", profitImpact);

            businessImpact.put("efficiency", isEarlyMonth ? "CRITICAL" : "IMPROVING");
            aiPayload.set("businessImpact", businessImpact);

            aiReport.set("payload", aiPayload);
            events.add(aiReport);

            // Strategic practice utilization (for comparison) - uses class variable
            ObjectNode strategicReport = createBaseEvent("ResourceUtilizationAnalysis", "util-strategic-" + month, "UtilizationReport");
            strategicReport.put("timestamp", month + "T23:59:00Z");

            ObjectNode strategicPayload = objectMapper.createObjectNode();
            strategicPayload.put("reportPeriod", month.substring(0, 7));
            strategicPayload.put("practice", "STRATEGIC_CONSULTING");
            strategicPayload.put("totalConsultants", strategicAllocationsCount); // Now uses class variable
            strategicPayload.put("utilizationRate", 0.85 + random.nextDouble() * 0.1);
            strategicPayload.put("benchConsultants", random.nextInt(Math.max(1, strategicAllocationsCount / 5)));
            strategicPayload.put("avgClientSatisfaction", 4.2 + random.nextDouble() * 0.6);

            // Scale revenue per consultant with organization size (larger orgs command higher rates)
            int baseRevenuePerConsultant = 180000;
            double revenueMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
            int revenuePerConsultant = (int)(baseRevenuePerConsultant * revenueMultiplier) + random.nextInt(50000);
            strategicPayload.put("revenuePerConsultant", revenuePerConsultant);

            strategicReport.set("payload", strategicPayload);
            events.add(strategicReport);
        }
    }
}