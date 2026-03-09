package com.aurorapoc.datagenerator.reports;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ExecutiveSummaryGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ExecutiveSummaryGenerator.class);
    private final ObjectMapper objectMapper;

    public ExecutiveSummaryGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode generateExecutiveSummary(OrganizationConfiguration config,
                                               Map<String, JsonNode> eventStreams,
                                               int totalEvents) {
        logger.info("📋 Generating Executive Summary Report...");

        ObjectNode summary = objectMapper.createObjectNode();

        // Executive Header
        summary.put("title", "Business Intelligence Simulation - Executive Summary");
        summary.put("organization", config.getOrganizationName());
        summary.put("reportDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        summary.put("reportingPeriod", "2023-2024 Simulation");

        // Company Overview
        summary.set("companyOverview", generateCompanyOverview(config));

        // Simulation Scope
        summary.set("simulationScope", generateSimulationScope(config, eventStreams, totalEvents));

        // Key Business Scenarios
        summary.set("businessScenarios", generateBusinessScenarios(config, eventStreams));

        // System Capabilities Demonstration
        summary.set("systemCapabilities", generateSystemCapabilities());

        // Key Findings & Root Cause Analysis
        summary.set("keyFindings", generateKeyFindings(config, eventStreams));

        // Business Recommendations
        summary.set("businessRecommendations", generateBusinessRecommendations(config));

        // ROI & Value Proposition
        summary.set("valueProposition", generateValueProposition(config));

        return summary;
    }

    private ObjectNode generateCompanyOverview(OrganizationConfiguration config) {
        ObjectNode overview = objectMapper.createObjectNode();
        overview.put("organizationName", config.getOrganizationName());
        overview.put("industry", "Professional Consulting Services");
        overview.put("totalEmployees", config.getTotalConsultants());
        overview.put("organizationType", determineOrganizationType(config));

        ObjectNode structure = objectMapper.createObjectNode();
        structure.put("aiSpecialists", config.getAiSpecialists());
        structure.put("seniorConsultants", config.getSeniorCohort());
        structure.put("activeProjects", config.getTotalProjects());
        structure.put("aiProjects", config.getAiProjects());
        structure.put("clientBase", config.getClients());
        overview.set("organizationalStructure", structure);

        ObjectNode businessContext = objectMapper.createObjectNode();
        businessContext.put("marketVolatility", config.getMarketVolatility());
        businessContext.put("turnoverRate", config.getTurnoverRate());
        businessContext.put("primaryChallenge", "Rapid AI transformation while managing talent retention");
        businessContext.put("businessObjective", "Scale AI capabilities while maintaining operational excellence");
        overview.set("businessContext", businessContext);

        return overview;
    }

    private ObjectNode generateSimulationScope(OrganizationConfiguration config,
                                               Map<String, JsonNode> eventStreams,
                                               int totalEvents) {
        ObjectNode scope = objectMapper.createObjectNode();
        scope.put("simulationPurpose", "Demonstrate temporal analytics and root cause analysis capabilities");
        scope.put("timeframe", "January 2023 - December 2024");
        scope.put("totalEventsGenerated", totalEvents);

        // System Coverage
        ArrayNode systems = objectMapper.createArrayNode();

        ObjectNode hrSystem = objectMapper.createObjectNode();
        hrSystem.put("system", "Human Resources Management");
        hrSystem.put("events", eventStreams.get("hr.employment").get("events").size());
        hrSystem.put("coverage", "Employee lifecycle, skills, performance, retention");
        hrSystem.put("keyMetrics", "Hiring, promotions, departures, skill development");
        systems.add(hrSystem);

        ObjectNode projectSystem = objectMapper.createObjectNode();
        projectSystem.put("system", "Project Management");
        projectSystem.put("events", eventStreams.get("project.management").get("events").size());
        projectSystem.put("coverage", "Project lifecycle, resource allocation, deliverables");
        projectSystem.put("keyMetrics", "Budget variance, timeline adherence, client satisfaction");
        systems.add(projectSystem);

        ObjectNode clientSystem = objectMapper.createObjectNode();
        clientSystem.put("system", "Client Relationship Management");
        clientSystem.put("events", eventStreams.get("client.relationship").get("events").size());
        clientSystem.put("coverage", "Client satisfaction, feedback, escalations");
        clientSystem.put("keyMetrics", "Satisfaction scores, renewal risk, service quality");
        systems.add(clientSystem);

        ObjectNode skillsSystem = objectMapper.createObjectNode();
        skillsSystem.put("system", "Skills & Certification Management");
        skillsSystem.put("events", eventStreams.get("skills.certification").get("events").size());
        skillsSystem.put("coverage", "Market intelligence, certifications, skill gaps");
        skillsSystem.put("keyMetrics", "Certification completion, skill demand, market trends");
        systems.add(skillsSystem);

        ObjectNode allocationSystem = objectMapper.createObjectNode();
        allocationSystem.put("system", "Resource Allocation & Utilization");
        allocationSystem.put("events", eventStreams.get("resource.allocation").get("events").size());
        allocationSystem.put("coverage", "Resource allocation decisions, utilization tracking");
        allocationSystem.put("keyMetrics", "Allocation success, utilization rates, bench costs");
        systems.add(allocationSystem);

        scope.set("systemsCovered", systems);

        return scope;
    }

    private ObjectNode generateBusinessScenarios(OrganizationConfiguration config,
                                                 Map<String, JsonNode> eventStreams) {
        ObjectNode scenarios = objectMapper.createObjectNode();

        // AI Talent Paradox Scenario
        ObjectNode aiParadox = objectMapper.createObjectNode();
        aiParadox.put("scenario", "The AI Talent Paradox");
        aiParadox.put("description", "Despite hiring AI specialists, projects remain delayed due to poor allocation decisions");
        aiParadox.put("timeframe", "July 2024 - December 2024");

        ObjectNode aiMetrics = objectMapper.createObjectNode();
        aiMetrics.put("specialistsHired", config.getAiSpecialists());
        aiMetrics.put("projectsAffected", config.getAiProjects());
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        aiMetrics.put("estimatedBenchCost", (int)(160000 * organizationMultiplier));
        aiMetrics.put("averageProjectDelay", "4-6 weeks");
        aiParadox.set("impactMetrics", aiMetrics);

        ArrayNode aiRootCauses = objectMapper.createArrayNode();
        aiRootCauses.add("Inexperienced managers overriding algorithmic allocation recommendations");
        aiRootCauses.add("Client pressure leading to suboptimal resource allocation decisions");
        aiRootCauses.add("Mismatch between AI specialist skills and urgent project needs");
        aiRootCauses.add("Lack of AI expertise in management layer for proper resource assessment");
        aiParadox.set("rootCauses", aiRootCauses);
        scenarios.set("aiTalentParadox", aiParadox);

        // Senior Retention Crisis Scenario
        ObjectNode retentionCrisis = objectMapper.createObjectNode();
        retentionCrisis.put("scenario", "Senior Talent Retention Crisis");
        retentionCrisis.put("description", "Two-tier system emerges where operational track seniors leave while strategic track seniors are retained");
        retentionCrisis.put("timeframe", "January 2023 - December 2024");

        ObjectNode retentionMetrics = objectMapper.createObjectNode();
        retentionMetrics.put("seniorCohortSize", config.getSeniorCohort());
        retentionMetrics.put("strategicTrackSize", (int)(config.getSeniorCohort() * 0.6));
        retentionMetrics.put("operationalTrackSize", (int)(config.getSeniorCohort() * 0.4));
        retentionMetrics.put("estimatedDepartureRate", "80% from operational track");
        retentionMetrics.put("avgTenure", "20.2 months");
        retentionCrisis.set("impactMetrics", retentionMetrics);

        ArrayNode retentionRootCauses = objectMapper.createArrayNode();
        retentionRootCauses.add("Systematic assignment of operational track to less visible projects");
        retentionRootCauses.add("Frequent manager changes disrupting career development relationships");
        retentionRootCauses.add("No clear promotion pathway from operational to strategic projects");
        retentionRootCauses.add("Limited client exposure reducing career advancement opportunities");
        retentionCrisis.set("rootCauses", retentionRootCauses);
        scenarios.set("seniorRetentionCrisis", retentionCrisis);

        // Project Performance Correlation Scenario
        ObjectNode projectPerformance = objectMapper.createObjectNode();
        projectPerformance.put("scenario", "Project Performance Correlation Analysis");
        projectPerformance.put("description", "Clear correlation between team composition and project outcomes across multiple dimensions");
        projectPerformance.put("timeframe", "January 2022 - December 2024");

        ObjectNode performanceMetrics = objectMapper.createObjectNode();
        performanceMetrics.put("projectsAnalyzed", Math.max(10, config.getTotalProjects() / 2));
        performanceMetrics.put("strategicProjectSatisfaction", "4.2/5.0 average");
        performanceMetrics.put("operationalProjectSatisfaction", "3.1/5.0 average");
        performanceMetrics.put("budgetVarianceRange", "±30%");
        performanceMetrics.put("timelineVarianceRange", "±6 weeks");
        projectPerformance.set("impactMetrics", performanceMetrics);

        ArrayNode performancePatterns = objectMapper.createArrayNode();
        performancePatterns.add("Strategic projects with senior consultants show 35% better client satisfaction");
        performancePatterns.add("Projects with skill mismatches experience 4-6 week delays consistently");
        performancePatterns.add("Client satisfaction correlates directly with consultant experience levels");
        performancePatterns.add("Budget overruns primarily driven by resource constraint corrections");
        projectPerformance.set("discoveredPatterns", performancePatterns);
        scenarios.set("projectPerformanceCorrelation", projectPerformance);

        return scenarios;
    }

    private ObjectNode generateSystemCapabilities() {
        ObjectNode capabilities = objectMapper.createObjectNode();
        capabilities.put("demonstrationPurpose", "Showcase temporal analytics and root cause analysis capabilities");

        ArrayNode coreCapabilities = objectMapper.createArrayNode();

        ObjectNode temporalAnalysis = objectMapper.createObjectNode();
        temporalAnalysis.put("capability", "Temporal Event Correlation");
        temporalAnalysis.put("description", "Track related events across multiple systems and time periods");
        temporalAnalysis.put("businessValue", "Identify cause-and-effect relationships across organizational silos");
        temporalAnalysis.put("example", "Link hiring decisions to project delays through resource allocation patterns");
        coreCapabilities.add(temporalAnalysis);

        ObjectNode rootCauseAnalysis = objectMapper.createObjectNode();
        rootCauseAnalysis.put("capability", "Multi-System Root Cause Analysis");
        rootCauseAnalysis.put("description", "Traverse event relationships to identify underlying causes");
        rootCauseAnalysis.put("businessValue", "Move beyond symptoms to address fundamental business issues");
        rootCauseAnalysis.put("example", "Discover that client escalations trace back to manager inexperience in AI allocation decisions");
        coreCapabilities.add(rootCauseAnalysis);

        ObjectNode patternDetection = objectMapper.createObjectNode();
        patternDetection.put("capability", "Business Pattern Detection");
        patternDetection.put("description", "Identify recurring patterns and correlation across business dimensions");
        patternDetection.put("businessValue", "Enable proactive decision-making based on historical patterns");
        patternDetection.put("example", "Predict project success based on team composition and experience patterns");
        coreCapabilities.add(patternDetection);

        ObjectNode crossSystemInsights = objectMapper.createObjectNode();
        crossSystemInsights.put("capability", "Cross-System Business Intelligence");
        crossSystemInsights.put("description", "Generate insights by combining data across traditionally separate systems");
        crossSystemInsights.put("businessValue", "Break down data silos to reveal enterprise-wide insights");
        crossSystemInsights.put("example", "Connect HR performance reviews to client satisfaction scores through project allocation history");
        coreCapabilities.add(crossSystemInsights);

        capabilities.set("demonstratedCapabilities", coreCapabilities);

        return capabilities;
    }

    private ObjectNode generateKeyFindings(OrganizationConfiguration config,
                                           Map<String, JsonNode> eventStreams) {
        ObjectNode findings = objectMapper.createObjectNode();
        findings.put("analysisDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        findings.put("methodology", "Cross-system temporal event analysis with correlation tracking");

        ArrayNode criticalFindings = objectMapper.createArrayNode();

        // Finding 1: AI Talent Paradox Root Cause
        ObjectNode finding1 = objectMapper.createObjectNode();
        finding1.put("finding", "AI Talent Investment Paradox");
        finding1.put("severity", "HIGH");
        finding1.put("confidence", "95%");
        finding1.put("description", "Despite hiring " + config.getAiSpecialists() + " AI specialists at premium salaries, " +
                config.getAiProjects() + " AI projects remain resource-constrained due to poor allocation decisions");

        ArrayNode finding1Evidence = objectMapper.createArrayNode();
        finding1Evidence.add("17 instances of management overriding algorithmic allocation recommendations");
        finding1Evidence.add("Average skill match scores of 0.25 for emergency allocations vs 0.85 for recommended matches");
        finding1Evidence.add("4-6 week project delays correlating with allocation decision override events");
        finding1Evidence.add("Client escalations following emergency allocation decisions by inexperienced managers");
        finding1.set("supportingEvidence", finding1Evidence);

        ArrayNode finding1Impact = objectMapper.createArrayNode();
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        finding1Impact.add("Estimated $" + (int)(160000 * organizationMultiplier) + " in bench costs for underutilized AI specialists");
        finding1Impact.add("Average 4-6 week delay on critical AI projects");
        finding1Impact.add("Client satisfaction degradation from 4.2 to 2.8 on affected projects");
        finding1Impact.add("Emergency contractor costs at 2.5x planned salary rates");
        finding1.set("businessImpact", finding1Impact);

        criticalFindings.add(finding1);

        // Finding 2: Two-Tier Retention System
        ObjectNode finding2 = objectMapper.createObjectNode();
        finding2.put("finding", "Systematic Career Path Bifurcation");
        finding2.put("severity", "HIGH");
        finding2.put("confidence", "92%");
        finding2.put("description", "Operational track seniors experience 80% departure rate while strategic track shows 0% departures, " +
                "indicating systematic career development disparities");

        ArrayNode finding2Evidence = objectMapper.createArrayNode();
        finding2Evidence.add("8 departures from operational track (cohort members 16-25) vs 0 from strategic track (1-15)");
        finding2Evidence.add("174 manager changes affecting operational track vs stable management for strategic track");
        finding2Evidence.add("Average 20.2 months tenure with citations of 'limited advancement opportunities'");
        finding2Evidence.add("Transfer requests to strategic projects systematically declined with 6-8 month wait times");
        finding2.set("supportingEvidence", finding2Evidence);

        ArrayNode finding2Impact = objectMapper.createArrayNode();
        finding2Impact.add("Loss of 8 senior consultants with 20+ months of company investment");
        finding2Impact.add("Knowledge drain from operational projects affecting service quality");
        finding2Impact.add("Increased recruitment costs and ramp-up time for replacements");
        finding2Impact.add("Potential reputation damage affecting future hiring in operational roles");
        finding2.set("businessImpact", finding2Impact);

        criticalFindings.add(finding2);

        // Finding 3: Performance Correlation Patterns
        ObjectNode finding3 = objectMapper.createObjectNode();
        finding3.put("finding", "Team Composition Directly Predicts Project Success");
        finding3.put("severity", "MEDIUM");
        finding3.put("confidence", "98%");
        finding3.put("description", "Strong correlation between consultant experience levels and project outcomes across satisfaction, budget, and timeline dimensions");

        ArrayNode finding3Evidence = objectMapper.createArrayNode();
        finding3Evidence.add("Strategic projects (senior consultants): 4.2/5.0 satisfaction, 95% on-time delivery");
        finding3Evidence.add("Operational projects: 3.1/5.0 satisfaction, 60% on-time delivery");
        finding3Evidence.add("Budget variance directly correlates with skill mismatch frequency");
        finding3Evidence.add("Client renewal risk increases 3x with operational project exposure");
        finding3.set("supportingEvidence", finding3Evidence);

        findings.set("criticalFindings", criticalFindings);

        return findings;
    }

    private ObjectNode generateBusinessRecommendations(OrganizationConfiguration config) {
        ObjectNode recommendations = objectMapper.createObjectNode();
        recommendations.put("priority", "IMMEDIATE ACTION REQUIRED");
        recommendations.put("implementationTimeframe", "Next 90 days");

        ArrayNode immediateActions = objectMapper.createArrayNode();

        ObjectNode rec1 = objectMapper.createObjectNode();
        rec1.put("recommendation", "Implement AI-Aware Allocation Decision Framework");
        rec1.put("priority", "CRITICAL");
        rec1.put("timeframe", "30 days");
        rec1.put("description", "Establish mandatory skill validation (>70% match) for AI project allocations with management override approval process");
        rec1.put("expectedROI", "Prevent $" + (int)(160000 * Math.max(1.0, config.getTotalConsultants() / 150.0)) + " annual bench waste");
        rec1.put("owner", "Resource Allocation Team + AI Practice Lead");
        immediateActions.add(rec1);

        ObjectNode rec2 = objectMapper.createObjectNode();
        rec2.put("recommendation", "Create Unified Career Development Framework");
        rec2.put("priority", "HIGH");
        rec2.put("timeframe", "60 days");
        rec2.put("description", "Eliminate operational/strategic track distinction and create rotation-based senior consultant development");
        rec2.put("expectedROI", "Retain " + (int)(config.getSeniorCohort() * 0.4) + " operational track seniors, saving $500K+ in replacement costs");
        rec2.put("owner", "HR Leadership + Practice Managers");
        immediateActions.add(rec2);

        ObjectNode rec3 = objectMapper.createObjectNode();
        rec3.put("recommendation", "Establish AI Management Competency Program");
        rec3.put("priority", "HIGH");
        rec3.put("timeframe", "45 days");
        rec3.put("description", "Mandatory AI literacy training for managers making allocation decisions on AI projects");
        rec3.put("expectedROI", "Improve AI project success rate by 60%, reduce client escalations");
        rec3.put("owner", "Learning & Development + AI Practice");
        immediateActions.add(rec3);

        recommendations.set("immediateActions", immediateActions);

        ArrayNode strategicInitiatives = objectMapper.createArrayNode();

        ObjectNode strategy1 = objectMapper.createObjectNode();
        strategy1.put("initiative", "Predictive Resource Allocation System");
        strategy1.put("timeframe", "6 months");
        strategy1.put("description", "Implement ML-driven allocation system using historical correlation patterns to predict project success");
        strategy1.put("businessValue", "Increase project success rate by 40%, reduce allocation conflicts");
        strategicInitiatives.add(strategy1);

        ObjectNode strategy2 = objectMapper.createObjectNode();
        strategy2.put("initiative", "Cross-System Analytics Platform");
        strategy2.put("timeframe", "12 months");
        strategy2.put("description", "Deploy temporal analytics platform for real-time root cause analysis across HR, Projects, and Client systems");
        strategy2.put("businessValue", "Enable proactive issue identification and resolution, improve decision-making speed by 75%");
        strategicInitiatives.add(strategy2);

        recommendations.set("strategicInitiatives", strategicInitiatives);

        return recommendations;
    }

    private ObjectNode generateValueProposition(OrganizationConfiguration config) {
        ObjectNode value = objectMapper.createObjectNode();
        value.put("demonstrationObjective", "Showcase temporal analytics ROI for enterprise decision-making");

        // Quantified Business Impact
        ObjectNode quantifiedImpact = objectMapper.createObjectNode();
        double organizationMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);

        quantifiedImpact.put("annualBenchCostAvoidance", (int)(160000 * organizationMultiplier));
        quantifiedImpact.put("retentionCostAvoidance", (int)(500000 * organizationMultiplier));
        quantifiedImpact.put("projectSuccessImprovement", "40%");
        quantifiedImpact.put("clientSatisfactionImprovement", "25%");
        quantifiedImpact.put("decisionMakingSpeedImprovement", "75%");
        value.set("quantifiedBusinessImpact", quantifiedImpact);

        // Technology Capabilities Demonstrated
        ArrayNode capabilities = objectMapper.createArrayNode();
        capabilities.add("Real-time cross-system event correlation and analysis");
        capabilities.add("Multi-dimensional root cause analysis across organizational silos");
        capabilities.add("Predictive pattern recognition for proactive decision-making");
        capabilities.add("Temporal data relationships revealing hidden business insights");
        capabilities.add("Enterprise-scale event processing and correlation tracking");
        value.set("technologyCapabilities", capabilities);

        // Competitive Advantages
        ArrayNode advantages = objectMapper.createArrayNode();
        advantages.add("Break down data silos to reveal enterprise-wide insights");
        advantages.add("Move from reactive to proactive organizational management");
        advantages.add("Enable data-driven decision making across all business functions");
        advantages.add("Reduce time-to-insight from weeks to minutes");
        advantages.add("Transform historical data into predictive business intelligence");
        value.set("competitiveAdvantages", advantages);

        ObjectNode implementation = objectMapper.createObjectNode();
        implementation.put("implementationComplexity", "MEDIUM");
        implementation.put("timeToValue", "30-90 days");
        implementation.put("scalabilityFactor", "LINEAR");
        implementation.put("integrationRequirement", "Standard APIs and event streaming");
        value.set("implementationProfile", implementation);

        return value;
    }

    private String determineOrganizationType(OrganizationConfiguration config) {
        int consultants = config.getTotalConsultants();
        if (consultants < 50) return "STARTUP";
        else if (consultants < 150) return "SMALL_BUSINESS";
        else if (consultants < 300) return "STANDARD";
        else if (consultants < 1000) return "ENTERPRISE";
        else return "MEGA_CORP";
    }
}