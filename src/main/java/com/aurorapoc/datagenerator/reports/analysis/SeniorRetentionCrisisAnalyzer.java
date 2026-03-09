package com.aurorapoc.datagenerator.reports.analysis;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.List;

/**
 * Analyzes the Senior Departure Mystery: Why are senior consultants considering departure
 * despite competitive compensation and market-leading benefits?
 */
public class SeniorRetentionCrisisAnalyzer implements WhyQuestionAnalyzer {

    @Override
    public WhyQuestionResult analyze(OrganizationConfiguration config, EventAnalysis eventAnalysis, JsonNode eventStreams) {

        int operationalTrack = (int)(config.getSeniorCohort() * 0.4);
        int strategicTrack = (int)(config.getSeniorCohort() * 0.6);

        double orgMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        int replacementCost = operationalTrack * 50000;
        int currentCost = (int)(400000 * orgMultiplier);
        int implementationCost = (int)(200000 * orgMultiplier);

        return new WhyQuestionResult.Builder()
                .whyQuestion(getWhyQuestion(config))
                .executiveQuote(generateExecutiveQuote(config))
                .discoveryProcess(generateWhatIfDiscoveryProcess(config, eventAnalysis))
                .rootCauseExplanation(generateRootCauseExplanation(config, eventAnalysis))
                .discoveryChain(generateDiscoveryChain(config, eventAnalysis))
                .keyMetrics(generateKeyMetrics(config, eventAnalysis, operationalTrack, strategicTrack))
                .deeperDiscovery(generateDeeperDiscovery(config, eventAnalysis))
                .actionableInsights(generateActionableInsights(config))
                .temporalInsight("The departure mystery reveals invisible career systems that traditional HR metrics cannot detect.")
                .scenarioTitle(getScenarioTitle())
                .scenarioNumber(getScenarioNumber())
                .whatIfResolution(generateWhatIfResolution(config, eventAnalysis))
                .investigativeAngle("What if compensation isn't the real issue?")
                .traditionalBILimitation("Traditional BI shows compensation data, turnover rates, and engagement scores - but can't reveal invisible career barriers and systematic limitations")
                .financialImpact(new WhyQuestionResult.FinancialImpact(currentCost, replacementCost, implementationCost, (double)currentCost/implementationCost))
                .build();
    }

    @Override
    public String getWhyQuestion(OrganizationConfiguration config) {
        return "Why are senior consultants considering departure despite competitive compensation and market-leading benefits?";
    }

    @Override
    public OrganizationConfiguration.PersonaProfile getPersona(OrganizationConfiguration config) {
        return config.getTalentLeaderProfile();
    }

    @Override
    public String getScenarioTitle() {
        return "The Senior Departure Mystery";
    }

    @Override
    public int getScenarioNumber() {
        return 2;
    }

    private String generateExecutiveQuote(OrganizationConfiguration config) {
        if (config.getTotalConsultants() < 100) {
            return "Our senior consultants are our most valuable asset, but I'm seeing subtle signs that some of our best people are exploring options despite our competitive packages.";
        } else if (config.getTotalConsultants() < 1000) {
            return "We pride ourselves on talent development, but I'm tracking concerning patterns in our senior consultant engagement scores and career progression requests.";
        } else {
            return "Managing talent at this scale, I see trends that worry me—some of our most experienced consultants seem disconnected from growth opportunities despite our global career frameworks.";
        }
    }

    private String generateWhatIfDiscoveryProcess(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        StringBuilder process = new StringBuilder();
        process.append("**Aurora's Investigative Journey:**\n\n");

        process.append("**What if we analyze career progression patterns?**\n");
        process.append("- Tracked ").append(eventAnalysis.getPromotionEvents()).append(" promotion events over 24 months\n");
        process.append("- Analyzed ").append(eventAnalysis.getDepartureEvents()).append(" departure patterns by seniority level\n");
        process.append("- *Discovery:* Clear bifurcation in advancement trajectories\n\n");

        process.append("**What if there's an invisible two-tier system?**\n");
        process.append("- ").append((int)(config.getSeniorCohort() * 0.6)).append(" consultants on 'strategic track': 0% departure risk\n");
        process.append("- ").append((int)(config.getSeniorCohort() * 0.4)).append(" consultants on 'operational track': 80% departure risk\n");
        process.append("- *Hidden System Exposed:* Initial project assignment determines entire career path\n\n");

        process.append("**What if first 90 days predict the next 5 years?**\n");
        process.append("- 95% accuracy in predicting career track from initial assignment\n");
        process.append("- Strategic track: High-visibility client projects, stable management\n");
        process.append("- Operational track: Internal work, management instability (174 changes tracked)\n");
        process.append("- *Root Cause Revealed:* Early operational assignments create systematic career limitations\n\n");

        return process.toString();
    }

    private String generateWhatIfResolution(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        String firstName = getPersona(config).getName().split(" ")[0];
        int operationalTrack = (int)(config.getSeniorCohort() * 0.4);

        StringBuilder resolution = new StringBuilder();
        resolution.append("**What if ").append(firstName).append(" could eliminate career track discrimination?**\n");
        resolution.append("- ").append(operationalTrack).append(" at-risk seniors enrolled in strategic exposure program\n");
        resolution.append("- Career mobility barriers removed within 60 days\n");
        resolution.append("- $6M in replacement costs avoided\n");
        resolution.append("- Talent retention: 60% → 95%\n\n");
        resolution.append("*").append(firstName).append("'s transformation: \"Now I manage career optimization, not damage control.\"*");

        return resolution.toString();
    }

    private String generateRootCauseExplanation(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return "\"Your senior departure mystery isn't about compensation or benefits. It's about an invisible two-tier career system where operational track consultants face systematic barriers to strategic project assignments and client relationship development.\"";
    }

    private List<String> generateDiscoveryChain(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        int operationalTrack = (int)(config.getSeniorCohort() * 0.4);
        return Arrays.asList(
                "**Surface Problem:** Senior consultant engagement declining",
                "**Traditional Response:** Improve compensation and benefits",
                String.format("**Aurora's Discovery:** %d operational track seniors have 80%% departure risk", operationalTrack),
                "**Root Cause:** Invisible career bifurcation creates advancement inequality",
                "**Actual Solution:** Eliminate track-based assignment bias"
        );
    }

    private List<String> generateKeyMetrics(OrganizationConfiguration config, EventAnalysis eventAnalysis, int operationalTrack, int strategicTrack) {
        return Arrays.asList(
                String.format("**Career Track Distribution:** %d strategic track vs %d operational track senior consultants", strategicTrack, operationalTrack),
                String.format("**Departure Risk Differential:** 0%% strategic track vs 80%% operational track departure probability"),
                String.format("**Management Instability:** 174 documented manager changes affecting operational track consultants"),
                String.format("**Career Mobility Barriers:** 6-8 month average delays for strategic project transfer requests"),
                String.format("**Replacement Cost Exposure:** $%,d for potential departures from operational track", operationalTrack * 50000)
        );
    }

    private String generateDeeperDiscovery(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return "*\"Which factors predict which track a senior consultant will be assigned to?\"*\n**Aurora Found:** Initial project assignment within first 90 days determines track with 95% accuracy—early operational assignments create systematic career limitations.";
    }

    private List<String> generateActionableInsights(OrganizationConfiguration config) {
        int operationalTrack = (int)(config.getSeniorCohort() * 0.4);

        if (config.getTotalConsultants() < 100) {
            return Arrays.asList(
                    String.format("Conduct immediate career development discussions with all %d at-risk senior consultants", operationalTrack),
                    "Guarantee strategic project rotation within 60 days for operational track volunteers",
                    "Assign strategic track mentors to create cross-track knowledge transfer",
                    "Implement monthly client exposure requirements for all senior consultants"
            );
        } else if (config.getTotalConsultants() < 1000) {
            return Arrays.asList(
                    String.format("Deploy comprehensive career pathway audit for %d operational track seniors", operationalTrack),
                    "Eliminate track-based project assignment bias through rotation requirements",
                    "Create strategic project transfer fast-track program with 30-day placement guarantee",
                    "Establish client relationship development program for operational track consultants"
            );
        } else {
            return Arrays.asList(
                    String.format("Launch global career equity initiative addressing %d operational track senior consultants", operationalTrack),
                    "Implement enterprise-wide project assignment governance eliminating track bias",
                    "Create senior consultant development program with guaranteed strategic exposure",
                    "Deploy global mentorship network connecting operational and strategic track professionals"
            );
        }
    }
}