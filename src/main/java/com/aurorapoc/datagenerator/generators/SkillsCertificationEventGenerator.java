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

public class SkillsCertificationEventGenerator extends BaseEventGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SkillsCertificationEventGenerator.class);

    // Configuration-driven values
    private final int AI_SPECIALISTS_COUNT;
    private final int SENIOR_COHORT_COUNT;
    private final int TOTAL_CONSULTANTS;
    private final int TOTAL_PROJECTS_COUNT;

    // Calculated values for scaling
    private final int marketIntelligenceEvents;
    private final int certificationEvents;
    private final int skillGapAnalysisEvents;

    // Skills Data
    private static final List<String> AI_SKILLS = Arrays.asList(
            "Machine Learning", "Deep Learning", "Computer Vision", "Natural Language Processing",
            "TensorFlow", "PyTorch", "Python", "R", "AWS SageMaker", "Azure ML",
            "Scikit-learn", "Keras", "Apache Spark", "Data Engineering", "MLOps"
    );

    private static final List<String> GENERAL_SKILLS = Arrays.asList(
            "Java", "JavaScript", "Python", "React", "Angular", "Node.js", "Spring Boot",
            "PostgreSQL", "MongoDB", "AWS", "Azure", "Docker", "Kubernetes", "Git"
    );

    // Extended skill lists for larger organizations
    private static final List<String> EXTENDED_AI_SKILLS = Arrays.asList(
            "Reinforcement Learning", "Generative AI", "Computer Vision", "Speech Recognition",
            "Robotics", "Quantum Computing", "Edge AI", "AutoML", "Model Optimization",
            "AI Ethics", "Federated Learning", "Neural Architecture Search"
    );

    private static final List<String> EXTENDED_GENERAL_SKILLS = Arrays.asList(
            "Microservices", "GraphQL", "TypeScript", "Vue.js", "Flutter", "Swift",
            "Kotlin", "Go", "Rust", "Blockchain", "DevOps", "Terraform", "Ansible"
    );

    // Skill Categories for market intelligence
    private static final String[] SKILL_CATEGORIES = {"AI_ML", "PROGRAMMING", "CLOUD", "FRONTEND", "BACKEND", "DATA"};

    // Department names that scale with organization size
    private static final String[] BASE_DEPARTMENTS = {
            "Digital Transformation", "Enterprise Solutions", "Cloud Architecture",
            "Data Analytics", "Cybersecurity"
    };

    private static final String[] EXTENDED_DEPARTMENTS = {
            "AI Practice", "Mobile Development", "DevOps Engineering", "Quality Assurance",
            "Business Intelligence", "Integration Services", "Platform Engineering"
    };

    private final List<String> availableSkills;
    private final List<String> departmentNames;

    public SkillsCertificationEventGenerator(ObjectMapper objectMapper, OrganizationConfiguration config) {
        super(objectMapper, config);
        this.AI_SPECIALISTS_COUNT = config.getAiSpecialists();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.TOTAL_CONSULTANTS = config.getTotalConsultants();
        this.TOTAL_PROJECTS_COUNT = config.getTotalProjects();

        // Calculate event scaling
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        this.marketIntelligenceEvents = (int)(80 * Math.min(2.0, organizationMultiplier)); // Cap at 2x
        this.certificationEvents = AI_SPECIALISTS_COUNT * 4 + SENIOR_COHORT_COUNT; // 4 certs per AI specialist + 1 per senior
        this.skillGapAnalysisEvents = Math.max(24, (int)(30 * organizationMultiplier)); // Monthly + quarterly reports

        // Create scaled skill and department lists
        this.availableSkills = createScaledSkillList(config);
        this.departmentNames = createScaledDepartmentList(config);
    }

    public SkillsCertificationEventGenerator(ObjectMapper objectMapper, int startingEventId, OrganizationConfiguration config) {
        super(objectMapper, startingEventId, config);
        this.AI_SPECIALISTS_COUNT = config.getAiSpecialists();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.TOTAL_CONSULTANTS = config.getTotalConsultants();
        this.TOTAL_PROJECTS_COUNT = config.getTotalProjects();

        // Calculate event scaling
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        this.marketIntelligenceEvents = (int)(80 * Math.min(2.0, organizationMultiplier)); // Cap at 2x
        this.certificationEvents = AI_SPECIALISTS_COUNT * 4 + SENIOR_COHORT_COUNT; // 4 certs per AI specialist + 1 per senior
        this.skillGapAnalysisEvents = Math.max(24, (int)(30 * organizationMultiplier)); // Monthly + quarterly reports

        // Create scaled skill and department lists
        this.availableSkills = createScaledSkillList(config);
        this.departmentNames = createScaledDepartmentList(config);
    }

    private List<String> createScaledSkillList(OrganizationConfiguration config) {
        List<String> skills = new ArrayList<>();
        skills.addAll(AI_SKILLS);
        skills.addAll(GENERAL_SKILLS);

        // Add extended skills for larger organizations
        if (config.getTotalConsultants() > 150) {
            skills.addAll(EXTENDED_AI_SKILLS);
            skills.addAll(EXTENDED_GENERAL_SKILLS);
        }

        return skills;
    }

    private List<String> createScaledDepartmentList(OrganizationConfiguration config) {
        List<String> departments = new ArrayList<>();
        departments.addAll(Arrays.asList(BASE_DEPARTMENTS));

        // Add extended departments for larger organizations
        if (config.getTotalConsultants() > 300) {
            departments.addAll(Arrays.asList(EXTENDED_DEPARTMENTS));
        }

        return departments;
    }

    @Override
    public String getStreamName() {
        return "skills.certification";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void generateEvents(ArrayNode events) {
        // Generate Market Intelligence on Skills
        logger.info("📈 Generating skill market intelligence ({} events)...", marketIntelligenceEvents);
        generateSkillMarketIntelligence(events);

        // Generate Certification Process Events
        logger.info("🎓 Generating certification process events ({} certification events)...", certificationEvents);
        generateCertificationProcessEvents(events);

        // Generate Skill Gap Analysis Events
        logger.info("🔍 Generating skill gap analysis events ({} analysis events)...", skillGapAnalysisEvents);
        generateSkillGapAnalysisEvents(events);
    }

    private void generateSkillMarketIntelligence(ArrayNode events) {
        // AI skill demand surge (key scenario driver) - scales with organization size
        ObjectNode aiTrend = createBaseEvent("SkillMarketDemandUpdated", "trend-ai-demand", "MarketTrend");
        aiTrend.put("timestamp", "2024-07-15T12:00:00Z");

        ObjectNode aiPayload = objectMapper.createObjectNode();
        aiPayload.put("skillCategory", "AI_ML");
        aiPayload.put("demandTrend", "INCREASING");
        aiPayload.put("demandGrowth", 3.40);

        // Scale salary expectations with organization size
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        int averageSalary = (int)(125000 * organizationMultiplier);
        aiPayload.put("averageSalary", averageSalary);
        aiPayload.put("salaryGrowth", 0.25);
        aiPayload.put("marketScarcity", "CRITICAL");
        aiPayload.put("timeToFill", "90+ days");

        ObjectNode projectDemand = objectMapper.createObjectNode();
        projectDemand.put("currentQuarter", (int)(23 * organizationMultiplier));
        projectDemand.put("nextQuarter", (int)(31 * organizationMultiplier));
        projectDemand.put("growth", 0.35);
        aiPayload.set("projectDemand", projectDemand);

        aiTrend.set("payload", aiPayload);
        events.add(aiTrend);

        // Individual AI skill trends (scales with available skills)
        int aiSkillTrends = Math.min(AI_SKILLS.size(), (int)(15 * Math.min(1.5, organizationMultiplier)));

        for (int i = 0; i < aiSkillTrends; i++) {
            String skill = AI_SKILLS.get(i % AI_SKILLS.size());

            ObjectNode skillTrend = createBaseEvent("SkillMarketDemandUpdated",
                    "trend-" + skill.toLowerCase().replace(" ", "-"),
                    "SkillTrend");
            skillTrend.put("timestamp", addDaysToTimestamp("2024-07-01T12:00:00Z", random.nextInt(180)));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("skillName", skill);
            payload.put("category", "AI_ML");
            payload.put("demandTrend", "INCREASING");
            payload.put("demandGrowth", 1.5 + random.nextDouble() * 2.0); // 150% - 350%

            // Scale salary with organization size
            int skillSalary = (int)((110000 + random.nextInt(40000)) * organizationMultiplier);
            payload.put("averageSalary", skillSalary);
            payload.put("salaryGrowth", 0.15 + random.nextDouble() * 0.15); // 15-30%
            payload.put("marketScarcity", random.nextDouble() > 0.3 ? "HIGH" : "CRITICAL");
            payload.put("competitorDemand", 0.8 + random.nextDouble() * 0.2);

            // Scale client request frequency with organization size
            int requestFrequency = (int)((15 + random.nextInt(25)) * Math.min(1.5, organizationMultiplier));
            payload.put("clientRequestFrequency", requestFrequency);

            skillTrend.set("payload", payload);
            events.add(skillTrend);
        }

        // General skill market updates (other categories)
        for (String category : SKILL_CATEGORIES) {
            if (category.equals("AI_ML")) continue; // Already handled above

            ObjectNode trend = createBaseEvent("SkillMarketDemandUpdated",
                    "trend-" + category.toLowerCase(),
                    "MarketTrend");
            trend.put("timestamp", addDaysToTimestamp("2024-01-15T12:00:00Z", random.nextInt(300)));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("skillCategory", category);
            payload.put("demandTrend", random.nextBoolean() ? "INCREASING" : "STABLE");
            payload.put("demandGrowth", 0.05 + random.nextDouble() * 0.15);

            // Scale salary with organization size
            int categorySalary = (int)((85000 + random.nextInt(40000)) * organizationMultiplier);
            payload.put("averageSalary", categorySalary);
            payload.put("salaryGrowth", random.nextDouble() * 0.1);
            payload.put("marketScarcity", random.nextDouble() > 0.6 ? "MEDIUM" : "LOW");
            payload.put("competitiveLevel", 0.4 + random.nextDouble() * 0.4);

            trend.set("payload", payload);
            events.add(trend);
        }

        // Quarterly salary surveys (scales with organization complexity)
        String[] quarters = {"2023-Q1", "2023-Q2", "2023-Q3", "2023-Q4",
                "2024-Q1", "2024-Q2", "2024-Q3", "2024-Q4"};

        for (String quarter : quarters) {
            ObjectNode survey = createBaseEvent("SalarySurveyCompleted", "salary-survey-" + quarter, "SalarySurvey");
            survey.put("timestamp", quarter.replace("Q1", "-03-31T17:00:00Z")
                    .replace("Q2", "-06-30T17:00:00Z")
                    .replace("Q3", "-09-30T17:00:00Z")
                    .replace("Q4", "-12-31T17:00:00Z"));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("surveyPeriod", quarter);
            payload.put("responseRate", 0.65 + random.nextDouble() * 0.25);

            // Scale market salaries with organization size
            int marketMedian = (int)((95000 + random.nextInt(30000)) * organizationMultiplier);
            int companyMedian = (int)((92000 + random.nextInt(25000)) * organizationMultiplier);
            payload.put("marketMedianSalary", marketMedian);
            payload.put("companyMedianSalary", companyMedian);
            payload.put("competitivePosition", companyMedian > marketMedian ? "ABOVE_MARKET" : "AT_MARKET");

            survey.set("payload", payload);
            events.add(survey);
        }
    }

    private void generateCertificationProcessEvents(ArrayNode events) {
        // Process optimization events (response to AI certification delays)
        ObjectNode optimization = createBaseEvent("CertificationProcessOptimized", "process-ai-cert-opt", "ProcessOptimization");
        optimization.put("timestamp", "2024-12-01T10:00:00Z");

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("processName", "AI Skills Certification");
        payload.put("previousDuration", "21-28 days");
        payload.put("newDuration", "12-14 days");
        payload.put("improvement", 0.50);
        payload.set("changes", createArrayNode(
                "Parallel theory and practical modules",
                "AI-assisted assessment",
                "Pre-work during onboarding"
        ));
        payload.put("expectedImpact", "50% faster time-to-productive allocation");

        // Scale investment with organization size
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        int investment = (int)(75000 * organizationMultiplier);
        payload.put("investmentRequired", investment);
        payload.put("expectedROI", 2.8);

        optimization.set("payload", payload);
        events.add(optimization);

        // Individual certification completions for all AI specialists
        for (int i = 1; i <= AI_SPECIALISTS_COUNT; i++) {
            String consultantId = "emp-ai-specialist-" + i;
            List<String> selectedSkills = getRandomAISkills(3 + random.nextInt(3));

            for (int skillIndex = 0; skillIndex < selectedSkills.size(); skillIndex++) {
                String skill = selectedSkills.get(skillIndex);

                // Certification completion event
                ObjectNode completion = createBaseEvent("SkillCertificationCompleted",
                        "cert-" + consultantId + "-" + skill.toLowerCase().replace(" ", "-"),
                        "SkillCertification");
                completion.put("timestamp", addDaysToTimestamp("2024-10-20T16:30:00Z", i * 7 + skillIndex * 3));

                ObjectNode completionPayload = objectMapper.createObjectNode();
                completionPayload.put("consultantId", consultantId);
                completionPayload.put("skillId", "skill-" + skill.toLowerCase().replace(" ", "-"));
                completionPayload.put("skillName", skill);
                completionPayload.put("category", "AI_ML");

                int actualDuration = 10 + random.nextInt(20); // 10-30 days
                completionPayload.put("actualDuration", actualDuration + " days");
                completionPayload.put("plannedDuration", "21 days");
                completionPayload.put("durationVariance", actualDuration - 21);
                completionPayload.put("score", 75 + random.nextInt(25));
                completionPayload.put("passingScore", 80);
                completionPayload.put("certifiedLevel", getRandomSkillLevel());
                completionPayload.put("validUntil", addDaysToTimestamp("2024-10-20T00:00:00Z", 365 + i * 7).split("T")[0]);

                // Assessment breakdown
                ObjectNode assessment = objectMapper.createObjectNode();
                assessment.put("theory", 80 + random.nextInt(20));
                assessment.put("practical", 75 + random.nextInt(25));
                assessment.put("caseStudy", 70 + random.nextInt(30));
                completionPayload.set("assessmentResults", assessment);

                completion.set("payload", completionPayload);
                events.add(completion);
            }
        }

        // General certifications for senior cohort (1 cert per senior)
        for (int i = 1; i <= SENIOR_COHORT_COUNT; i++) {
            String consultantId = "emp-senior-cohort-" + i;
            String skill = GENERAL_SKILLS.get(i % GENERAL_SKILLS.size());

            ObjectNode completion = createBaseEvent("SkillCertificationCompleted",
                    "cert-" + consultantId + "-" + skill.toLowerCase().replace(" ", "-"),
                    "SkillCertification");
            completion.put("timestamp", addDaysToTimestamp("2024-09-01T16:30:00Z", i * 5));

            ObjectNode completionPayload = objectMapper.createObjectNode();
            completionPayload.put("consultantId", consultantId);
            completionPayload.put("skillId", "skill-" + skill.toLowerCase().replace(" ", "-"));
            completionPayload.put("skillName", skill);
            completionPayload.put("category", getSkillCategory(skill));
            completionPayload.put("actualDuration", (14 + random.nextInt(14)) + " days");
            completionPayload.put("score", 80 + random.nextInt(20));
            completionPayload.put("certifiedLevel", getRandomSkillLevel());

            completion.set("payload", completionPayload);
            events.add(completion);
        }

        // Certification failures and retakes (adds realism) - scales with organization size
        int failureCount = Math.max(8, AI_SPECIALISTS_COUNT / 3); // About 1/3 have some failures

        for (int i = 1; i <= failureCount; i++) {
            ObjectNode failure = createBaseEvent("SkillCertificationFailed",
                    "cert-failure-" + i,
                    "SkillCertification");
            failure.put("timestamp", addDaysToTimestamp("2024-11-01T14:00:00Z", i * 5));

            ObjectNode failurePayload = objectMapper.createObjectNode();
            failurePayload.put("consultantId", "emp-consultant-" + (10 + i));
            failurePayload.put("skillName", AI_SKILLS.get(random.nextInt(AI_SKILLS.size())));
            failurePayload.put("score", 60 + random.nextInt(19)); // Below passing score of 80
            failurePayload.put("passingScore", 80);
            failurePayload.put("attempt", 1);
            failurePayload.put("retakeScheduled", true);
            failurePayload.put("retakeDate", addDaysToTimestamp("2024-11-01T00:00:00Z", 14 + i * 5).split("T")[0]);
            failurePayload.put("additionalTrainingRequired", "Advanced " + AI_SKILLS.get(random.nextInt(3)) + " concepts");

            failure.set("payload", failurePayload);
            events.add(failure);
        }
    }

    private void generateSkillGapAnalysisEvents(ArrayNode events) {
        // Comprehensive skill gap analysis for AI practice
        ObjectNode gapAnalysis = createBaseEvent("SkillGapIdentified", "gap-ai-practice", "SkillGapAnalysis");
        gapAnalysis.put("timestamp", "2024-10-01T14:00:00Z");

        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("analysisScope", "AI_PRACTICE");
        payload.put("analysisDate", "2024-10-01");
        payload.put("urgencyLevel", "CRITICAL");

        // Scale gap summary with organization size
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        int totalRequired = (int)(24 * organizationMultiplier); // 4 AI projects × 6 people avg, scaled
        int currentCapacity = AI_SPECIALISTS_COUNT;
        int gap = Math.max(0, totalRequired - currentCapacity);

        ObjectNode gapSummary = objectMapper.createObjectNode();
        gapSummary.put("totalRequiredCapacity", totalRequired);
        gapSummary.put("currentCapacity", currentCapacity);
        gapSummary.put("gap", gap);
        gapSummary.put("gapPercentage", gap > 0 ? (double)gap / totalRequired : 0.0);
        payload.set("overallGap", gapSummary);

        // Skill-specific gaps
        ArrayNode skillGaps = objectMapper.createArrayNode();
        for (String skill : Arrays.asList("Machine Learning", "Deep Learning", "Computer Vision", "NLP", "Python")) {
            ObjectNode skillGap = objectMapper.createObjectNode();
            skillGap.put("skillName", skill);

            int required = (int)((4 + random.nextInt(4)) * Math.min(2.0, organizationMultiplier));
            int available = random.nextInt(Math.max(1, AI_SPECIALISTS_COUNT / 3));

            skillGap.put("required", required);
            skillGap.put("available", available);
            skillGap.put("gap", Math.max(0, required - available));
            skillGap.put("priority", (required - available) > 2 ? "HIGH" : "MEDIUM");
            skillGaps.add(skillGap);
        }
        payload.set("skillSpecificGaps", skillGaps);

        // Scale affected projects
        ArrayNode affectedProjects = objectMapper.createArrayNode();
        int affectedCount = Math.min(6, config.getAiProjects());
        for (int i = 1; i <= affectedCount; i++) {
            affectedProjects.add("proj-ai-" + i);
        }
        payload.set("projectsAffected", affectedProjects);

        // Scale business impact
        int businessImpact = (int)(850000 * organizationMultiplier);
        payload.put("businessImpact", businessImpact);
        payload.put("timeToClose", gap > totalRequired * 0.5 ? "6-12 months" : "3-6 months");

        // Recommendations scaled to organization size
        ArrayNode recommendations = objectMapper.createArrayNode();
        recommendations.add("Fast-track AI certification for existing consultants");
        if (gap > 10) {
            recommendations.add("Hire " + Math.min(gap, 20) + " additional AI specialists");
        } else {
            recommendations.add("Hire " + Math.max(2, gap) + " additional AI specialists");
        }
        recommendations.add("Partner with external AI consulting firms");
        recommendations.add("Implement AI skills development program");
        if (organizationMultiplier > 2.0) {
            recommendations.add("Establish AI Center of Excellence");
            recommendations.add("Create AI mentorship program");
        }
        payload.set("recommendations", recommendations);

        gapAnalysis.set("payload", payload);
        events.add(gapAnalysis);

        // Department-specific skill gap analyses (scales with departments)
        for (int i = 0; i < departmentNames.size(); i++) {
            String department = departmentNames.get(i);

            ObjectNode deptGap = createBaseEvent("SkillGapIdentified",
                    "gap-" + department.toLowerCase().replace(" ", "-"),
                    "SkillGapAnalysis");
            deptGap.put("timestamp", addDaysToTimestamp("2024-09-01T14:00:00Z", i * 15));

            ObjectNode deptPayload = objectMapper.createObjectNode();
            deptPayload.put("department", department);
            deptPayload.put("analysisScope", "DEPARTMENT");

            // Scale skill needs with organization size
            int skillsNeeded = (int)((3 + random.nextInt(5)) * Math.min(1.5, organizationMultiplier));
            int skillsAvailable = random.nextInt(Math.max(1, skillsNeeded / 2));

            deptPayload.put("criticalSkillsNeeded", skillsNeeded);
            deptPayload.put("currentSkillsAvailable", skillsAvailable);
            deptPayload.put("skillGapSeverity", (skillsNeeded - skillsAvailable) > skillsNeeded * 0.6 ? "HIGH" : "MEDIUM");
            deptPayload.put("impactOnProjects", Math.max(1, (int)((2 + random.nextInt(4)) * Math.min(1.5, organizationMultiplier))));
            deptPayload.set("recommendedActions", createArrayNode(
                    "Upskill existing consultants",
                    "Strategic hiring for key skills",
                    "Cross-department skill sharing"
            ));

            deptGap.set("payload", deptPayload);
            events.add(deptGap);
        }

        // Quarterly skill demand forecasts (4 quarters)
        String[] quarters = {"Q4 2024", "Q1 2025", "Q2 2025", "Q3 2025"};
        for (int i = 0; i < quarters.length; i++) {
            ObjectNode forecast = createBaseEvent("SkillDemandForecast",
                    "forecast-" + quarters[i].toLowerCase().replace(" ", "-"),
                    "SkillForecast");
            forecast.put("timestamp", addDaysToTimestamp("2024-10-15T10:00:00Z", i * 90));

            ObjectNode forecastPayload = objectMapper.createObjectNode();
            forecastPayload.put("forecastPeriod", quarters[i]);
            forecastPayload.put("forecastConfidence", 0.75 + random.nextDouble() * 0.2);

            // Scale AI skills demand with organization size
            ObjectNode aiDemand = objectMapper.createObjectNode();
            int expectedProjects = (int)((8 + i * 2) * Math.min(2.0, organizationMultiplier));
            int requiredConsultants = (int)((15 + i * 3) * Math.min(2.0, organizationMultiplier));
            int currentSupply = AI_SPECIALISTS_COUNT + i;

            aiDemand.put("expectedProjects", expectedProjects);
            aiDemand.put("requiredConsultants", requiredConsultants);
            aiDemand.put("currentSupply", currentSupply);
            aiDemand.put("projectedGap", Math.max(0, requiredConsultants - currentSupply));
            forecastPayload.set("aiSkillsDemand", aiDemand);

            // General skills demand
            ArrayNode skillForecasts = objectMapper.createArrayNode();
            for (String category : Arrays.asList("CLOUD", "DATA", "FRONTEND", "BACKEND")) {
                ObjectNode categoryForecast = objectMapper.createObjectNode();
                categoryForecast.put("category", category);
                categoryForecast.put("expectedGrowth", 0.1 + random.nextDouble() * 0.2);
                categoryForecast.put("supplyRisk", random.nextDouble() > 0.6 ? "MEDIUM" : "LOW");
                skillForecasts.add(categoryForecast);
            }
            forecastPayload.set("generalSkillsForecasts", skillForecasts);

            forecast.set("payload", forecastPayload);
            events.add(forecast);
        }

        // Skill certification bottleneck analysis events (scales with organization complexity)
        String[] bottleneckTypes = {"ASSESSMENT_CAPACITY", "INSTRUCTOR_AVAILABILITY", "EQUIPMENT_SHORTAGE"};
        int bottleneckEvents = organizationMultiplier > 2.0 ? bottleneckTypes.length + 2 : bottleneckTypes.length;

        for (int i = 0; i < Math.min(bottleneckEvents, bottleneckTypes.length); i++) {
            ObjectNode bottleneck = createBaseEvent("CertificationBottleneckIdentified",
                    "bottleneck-" + bottleneckTypes[i].toLowerCase(),
                    "CertificationBottleneck");
            bottleneck.put("timestamp", addDaysToTimestamp("2024-11-15T11:00:00Z", i * 10));

            ObjectNode bottleneckPayload = objectMapper.createObjectNode();
            bottleneckPayload.put("bottleneckType", bottleneckTypes[i]);
            bottleneckPayload.put("severity", random.nextDouble() > 0.5 ? "HIGH" : "MEDIUM");

            // Scale impact with organization size
            int affectedCerts = (int)((5 + random.nextInt(10)) * Math.min(1.5, organizationMultiplier));
            int costImpact = (int)((25000 + random.nextInt(50000)) * organizationMultiplier);

            bottleneckPayload.put("affectedCertifications", affectedCerts);
            bottleneckPayload.put("delayImpact", (1 + random.nextInt(3)) + " weeks");
            bottleneckPayload.put("costImpact", costImpact);

            ArrayNode mitigation = objectMapper.createArrayNode();
            switch (bottleneckTypes[i]) {
                case "ASSESSMENT_CAPACITY":
                    mitigation.add("Hire additional assessors");
                    mitigation.add("Implement automated assessment tools");
                    if (organizationMultiplier > 1.5) {
                        mitigation.add("Establish regional assessment centers");
                    }
                    break;
                case "INSTRUCTOR_AVAILABILITY":
                    mitigation.add("Contract external AI training experts");
                    mitigation.add("Develop self-paced learning modules");
                    if (organizationMultiplier > 2.0) {
                        mitigation.add("Create internal AI training academy");
                    }
                    break;
                case "EQUIPMENT_SHORTAGE":
                    mitigation.add("Procure additional GPU resources");
                    mitigation.add("Leverage cloud-based training environments");
                    if (organizationMultiplier > 1.5) {
                        mitigation.add("Establish dedicated AI training infrastructure");
                    }
                    break;
            }
            bottleneckPayload.set("mitigationActions", mitigation);

            bottleneck.set("payload", bottleneckPayload);
            events.add(bottleneck);
        }

        // Additional bottleneck events for larger organizations
        if (organizationMultiplier > 2.0) {
            // Mentor availability bottleneck
            ObjectNode mentorBottleneck = createBaseEvent("CertificationBottleneckIdentified",
                    "bottleneck-mentor-availability",
                    "CertificationBottleneck");
            mentorBottleneck.put("timestamp", addDaysToTimestamp("2024-12-01T11:00:00Z", 5));

            ObjectNode mentorPayload = objectMapper.createObjectNode();
            mentorPayload.put("bottleneckType", "MENTOR_AVAILABILITY");
            mentorPayload.put("severity", "HIGH");
            mentorPayload.put("affectedCertifications", (int)(15 * organizationMultiplier));
            mentorPayload.put("delayImpact", "2-4 weeks");
            mentorPayload.put("costImpact", (int)(40000 * organizationMultiplier));
            mentorPayload.set("mitigationActions", createArrayNode(
                    "Establish senior consultant mentorship program",
                    "Create AI expertise sharing circles",
                    "Implement peer-to-peer learning sessions"
            ));

            mentorBottleneck.set("payload", mentorPayload);
            events.add(mentorBottleneck);

            // Curriculum scalability bottleneck
            ObjectNode curriculumBottleneck = createBaseEvent("CertificationBottleneckIdentified",
                    "bottleneck-curriculum-scalability",
                    "CertificationBottleneck");
            curriculumBottleneck.put("timestamp", addDaysToTimestamp("2024-12-05T11:00:00Z", 3));

            ObjectNode curriculumPayload = objectMapper.createObjectNode();
            curriculumPayload.put("bottleneckType", "CURRICULUM_SCALABILITY");
            curriculumPayload.put("severity", "MEDIUM");
            curriculumPayload.put("affectedCertifications", (int)(20 * organizationMultiplier));
            curriculumPayload.put("delayImpact", "1-2 weeks");
            curriculumPayload.put("costImpact", (int)(30000 * organizationMultiplier));
            curriculumPayload.set("mitigationActions", createArrayNode(
                    "Modularize certification curriculum",
                    "Develop adaptive learning paths",
                    "Implement AI-powered personalized training"
            ));

            curriculumBottleneck.set("payload", curriculumPayload);
            events.add(curriculumBottleneck);
        }
    }

    // Helper methods
    private String getRandomSkillLevel() {
        String[] levels = {"INTERMEDIATE", "ADVANCED", "EXPERT"};
        return levels[random.nextInt(levels.length)];
    }

    private List<String> getRandomAISkills(int count) {
        List<String> skills = new ArrayList<>(availableSkills.subList(0, Math.min(AI_SKILLS.size(), availableSkills.size())));
        Collections.shuffle(skills, random);
        return skills.subList(0, Math.min(count, skills.size()));
    }

    private String getSkillCategory(String skill) {
        if (Arrays.asList("Machine Learning", "Deep Learning", "TensorFlow", "PyTorch", "Python", "R").contains(skill)) {
            return "AI_ML";
        } else if (Arrays.asList("React", "Angular", "JavaScript", "TypeScript").contains(skill)) {
            return "FRONTEND";
        } else if (Arrays.asList("Java", "Node.js", "Spring Boot").contains(skill)) {
            return "BACKEND";
        } else if (Arrays.asList("AWS", "Azure", "Docker", "Kubernetes").contains(skill)) {
            return "CLOUD";
        } else if (Arrays.asList("PostgreSQL", "MongoDB").contains(skill)) {
            return "DATA";
        } else {
            return "PROGRAMMING";
        }
    }

    private double calculateTotalGapScore(ArrayNode skillGaps) {
        double total = 0;
        for (int i = 0; i < skillGaps.size(); i++) {
            ObjectNode gap = (ObjectNode) skillGaps.get(i);
            if (gap.has("gap")) {
                total += gap.get("gap").asDouble();
            }
        }
        return total;
    }
}