package com.aurorapoc.datagenerator.reports.analysis;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.List;

/**
 * Analyzes the Project Outcome Mystery: Why do some projects consistently succeed
 * while others struggle, despite similar scope and budgets?
 */
public class ProjectPerformanceCorrelationAnalyzer implements WhyQuestionAnalyzer {

    @Override
    public WhyQuestionResult analyze(OrganizationConfiguration config, EventAnalysis eventAnalysis, JsonNode eventStreams) {

        double orgMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        int revenueRisk = (int)(200000 * orgMultiplier);
        int currentCost = (int)(300000 * orgMultiplier);
        int implementationCost = (int)(150000 * orgMultiplier);

        return new WhyQuestionResult.Builder()
                .whyQuestion(getWhyQuestion(config))
                .executiveQuote(generateExecutiveQuote(config))
                .discoveryProcess(generateWhatIfDiscoveryProcess(config, eventAnalysis))
                .rootCauseExplanation(generateRootCauseExplanation(config, eventAnalysis))
                .discoveryChain(generateDiscoveryChain(config, eventAnalysis))
                .keyMetrics(generateKeyMetrics(config, eventAnalysis, revenueRisk))
                .deeperDiscovery(generateDeeperDiscovery(config, eventAnalysis))
                .actionableInsights(generateActionableInsights(config))
                .temporalInsight("The outcome mystery reveals that project success follows mathematical patterns hidden in team composition data.")
                .scenarioTitle(getScenarioTitle())
                .scenarioNumber(getScenarioNumber())
                .whatIfResolution(generateWhatIfResolution(config, eventAnalysis))
                .investigativeAngle("What if project success is mathematically predictable?")
                .traditionalBILimitation("Traditional BI shows project timelines, budget variances, and resource allocation - but can't predict outcomes or reveal success determination patterns")
                .financialImpact(new WhyQuestionResult.FinancialImpact(currentCost, revenueRisk, implementationCost, (double)currentCost/implementationCost))
                .build();
    }

    @Override
    public String getWhyQuestion(OrganizationConfiguration config) {
        return "Why do some projects consistently succeed while others struggle, despite similar scope and budgets?";
    }

    @Override
    public OrganizationConfiguration.PersonaProfile getPersona(OrganizationConfiguration config) {
        return config.getOperationsLeaderProfile();
    }

    @Override
    public String getScenarioTitle() {
        return "The Project Outcome Mystery";
    }

    @Override
    public int getScenarioNumber() {
        return 3;
    }

    private String generateExecutiveQuote(OrganizationConfiguration config) {
        if (config.getTotalConsultants() < 100) {
            return "I notice that some of our project teams consistently deliver exceptional results while others with similar scope and budgets struggle—and I can't figure out why.";
        } else if (config.getTotalConsultants() < 1000) {
            return "We have projects with identical complexity and budgets, but the performance variance is dramatic—some teams are heroes while others need constant intervention.";
        } else {
            return "Across hundreds of projects, I see patterns that are puzzling—certain team compositions predict success with uncanny accuracy, while others seem destined to struggle regardless of resources.";
        }
    }

    private String generateWhatIfDiscoveryProcess(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        StringBuilder process = new StringBuilder();
        process.append("**Aurora's Investigative Journey:**\n\n");

        process.append("**What if we test traditional success factors?**\n");
        process.append("- Budget adequacy: ❌ Similar budgets don't predict success\n");
        process.append("- Scope complexity: ❌ Similar complexity shows different outcomes\n");
        process.append("- Project management: ❌ Same methodologies, different results\n");
        process.append("- *Discovery:* Traditional factors don't explain variance\n\n");

        process.append("**What if it's about team composition?**\n");
        process.append("- Strategic track teams: 4.2/5.0 client satisfaction\n");
        process.append("- Operational track teams: 3.1/5.0 client satisfaction\n");
        process.append("- Analyzed ").append(eventAnalysis.getProjectsCompleted()).append(" completed projects\n");
        process.append("- *Breakthrough:* 95% prediction accuracy from team composition alone\n\n");

        process.append("**What if success patterns are hidden in allocation history?**\n");
        process.append("- Projects with 60%+ strategic consultants: 4.5/5.0 satisfaction\n");
        process.append("- Projects with operational-heavy teams: 2.8/5.0 satisfaction\n");
        process.append("- Strategic consultants: 5+ years average experience\n");
        process.append("- Operational consultants: 2-3 years average experience\n");
        process.append("- *Root Cause Revealed:* Project outcomes are predetermined by team assembly decisions\n\n");

        return process.toString();
    }

    private String generateWhatIfResolution(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        String firstName = getPersona(config).getName().split(" ")[0];

        StringBuilder resolution = new StringBuilder();
        resolution.append("**What if ").append(firstName).append(" could predict project success before starting?**\n");
        resolution.append("- AI-driven team composition optimization implemented\n");
        resolution.append("- Project risk intervention protocols activated\n");
        resolution.append("- Client satisfaction: 3.1/5.0 → 4.2/5.0\n");
        resolution.append("- Budget variance: ±25% → ±5%\n\n");
        resolution.append("*").append(firstName).append("'s transformation: \"Now I engineer success, not manage surprises.\"*");

        return resolution.toString();
    }

    private String generateRootCauseExplanation(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return "\"Your project outcome mystery isn't about scope or budget—it's about team composition. Strategic track teams with direct client relationships and optimal skill matching achieve predictably better outcomes than operational teams with emergency allocations.\"";
    }

    private List<String> generateDiscoveryChain(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return Arrays.asList(
                "**Surface Problem:** Inconsistent project performance despite similar resources",
                "**Traditional Response:** Improve project management processes",
                "**Aurora's Discovery:** Team composition predicts success with 95% accuracy",
                "**Root Cause:** Strategic vs operational track assignment determines project outcomes",
                "**Actual Solution:** Optimize team composition through systematic allocation"
        );
    }

    private List<String> generateKeyMetrics(OrganizationConfiguration config, EventAnalysis eventAnalysis, int revenueRisk) {
        return Arrays.asList(
                String.format("**Client Satisfaction Gap:** Strategic projects achieve 4.2/5.0 vs operational 3.1/5.0 (35%% performance differential)"),
                String.format("**Delivery Success Rate:** Strategic track 95%% on-time vs operational track 60%% delivery success"),
                String.format("**Budget Variance Correlation:** %d variance events correlating with operational team assignments", eventAnalysis.getBudgetVariances()),
                String.format("**Quality Indicator:** Strategic projects show ±5%% budget variance vs ±25%% operational variance"),
                String.format("**Revenue Risk:** $%,d annually from client satisfaction degradation", revenueRisk)
        );
    }

    private String generateDeeperDiscovery(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return "*\"What specific team composition factors drive the highest success rates?\"*\n**Aurora Found:** Projects with 60%+ strategic track consultants and direct client relationship owners achieve 4.5/5.0 satisfaction vs 2.8/5.0 for operational-heavy teams.";
    }

    private List<String> generateActionableInsights(OrganizationConfiguration config) {
        if (config.getTotalConsultants() < 100) {
            return Arrays.asList(
                    "Deploy team composition analysis for all active projects with optimization recommendations",
                    "Implement skill-project matching validation before any new team assignments",
                    "Create project success probability model based on historical team performance data",
                    "Establish direct client relationship requirements for all senior project roles"
            );
        } else if (config.getTotalConsultants() < 1000) {
            return Arrays.asList(
                    "Deploy enterprise team composition optimization algorithm for all project assignments",
                    "Implement real-time project risk monitoring based on team composition analysis",
                    "Create automated escalation triggers for projects with high-risk team profiles",
                    "Establish client relationship development protocols for all project team members"
            );
        } else {
            return Arrays.asList(
                    "Deploy global project success probability modeling across all practice areas",
                    "Implement AI-driven team composition optimization with continuous learning capabilities",
                    "Create predictive project risk management system with automated intervention protocols",
                    "Establish worldwide client relationship excellence program for all consultant levels"
            );
        }
    }
}