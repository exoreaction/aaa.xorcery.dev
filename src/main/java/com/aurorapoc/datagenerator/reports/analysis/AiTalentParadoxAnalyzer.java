package com.aurorapoc.datagenerator.reports.analysis;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.List;

/**
 * Analyzes the Resource Constraint Mystery: Why do AI projects remain resource-constrained
 * despite hiring AI specialists?
 */
public class AiTalentParadoxAnalyzer implements WhyQuestionAnalyzer {

    @Override
    public WhyQuestionResult analyze(OrganizationConfiguration config, EventAnalysis eventAnalysis, JsonNode eventStreams) {

        // Calculate metrics
        double overrideRate = eventAnalysis.getAllocationDecisions() > 0 ?
                (double) eventAnalysis.getAllocationOverrides() / eventAnalysis.getAllocationDecisions() * 100 : 0;

        double orgMultiplier = Math.max(1.0, config.getTotalConsultants() / 150.0);
        int benchCost = (int)(160000 * orgMultiplier);
        int emergencyContractorCost = (int)(96000 * orgMultiplier);
        int currentCost = benchCost + emergencyContractorCost;
        int implementationCost = (int)(500000 * orgMultiplier);

        return new WhyQuestionResult.Builder()
                .whyQuestion(getWhyQuestion(config))
                .executiveQuote(generateExecutiveQuote(config))
                .discoveryProcess(generateWhatIfDiscoveryProcess(config, eventAnalysis))
                .rootCauseExplanation(generateRootCauseExplanation(config, eventAnalysis))
                .discoveryChain(generateDiscoveryChain(config, eventAnalysis))
                .keyMetrics(generateKeyMetrics(config, eventAnalysis, overrideRate, benchCost))
                .deeperDiscovery(generateDeeperDiscovery(config, eventAnalysis))
                .actionableInsights(generateActionableInsights(config))
                .temporalInsight("The resource constraint mystery reveals allocation decision failures, not talent shortages.")
                .scenarioTitle(getScenarioTitle())
                .scenarioNumber(getScenarioNumber())
                .whatIfResolution(generateWhatIfResolution(config, eventAnalysis))
                .investigativeAngle("What if the answer lies in decision-making patterns under pressure?")
                .traditionalBILimitation("Traditional BI shows headcount, budget utilization, and project status - but can't explain why talent abundance creates resource scarcity")
                .financialImpact(new WhyQuestionResult.FinancialImpact(currentCost, currentCost, implementationCost, (double)currentCost/implementationCost))
                .build();
    }

    @Override
    public String getWhyQuestion(OrganizationConfiguration config) {
        return String.format("Why do our AI projects remain resource-constrained despite hiring %d AI specialists?",
                config.getAiSpecialists());
    }

    @Override
    public OrganizationConfiguration.PersonaProfile getPersona(OrganizationConfiguration config) {
        return config.getAiLeaderProfile();
    }

    @Override
    public String getScenarioTitle() {
        return "The Resource Constraint Mystery";
    }

    @Override
    public int getScenarioNumber() {
        return 1;
    }

    private String generateExecutiveQuote(OrganizationConfiguration config) {
        if (config.getTotalConsultants() < 100) {
            return String.format("I have %d AI specialists on payroll, yet every week brings another project delay due to resource constraints. The math doesn't add up.", config.getAiSpecialists());
        } else if (config.getTotalConsultants() < 1000) {
            return String.format("We've invested heavily in AI talent—%d specialists across %d projects—yet resource constraints remain our biggest delivery risk. Something is fundamentally wrong.", config.getAiSpecialists(), config.getAiProjects());
        } else {
            return String.format("I'm looking at a dashboard showing %d AI specialists and %d concurrent projects experiencing resource constraints. This should be mathematically impossible.", config.getAiSpecialists(), config.getAiProjects());
        }
    }

    private String generateWhatIfDiscoveryProcess(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        StringBuilder process = new StringBuilder();
        process.append("**Aurora's Investigative Journey:**\n\n");

        process.append("**What if we trace actual allocation decisions in real-time?**\n");
        process.append("- Analyzed ").append(eventAnalysis.getAllocationDecisions()).append(" allocation decisions over 24 months\n");
        process.append("- Discovered ").append(eventAnalysis.getAllocationOverrides()).append(" management overrides of systematic recommendations\n");
        process.append("- *Pattern Recognition:* Overrides spike during client escalation periods\n\n");

        process.append("**What if we examine decision quality under different conditions?**\n");
        process.append("- Systematic decisions: 85% skill match success rate\n");
        process.append("- Emergency decisions: 25% skill match success rate\n");
        process.append("- *Hidden Insight:* Client pressure creates 4-hour decision windows\n\n");

        process.append("**What if we follow the cascade effect?**\n");
        process.append("- Client escalation → Emergency allocation → Poor skill match → Project struggle → More escalations\n");
        process.append("- *Root Cause Revealed:* Resource constraint is decision governance failure, not talent shortage\n\n");

        return process.toString();
    }

    private String generateWhatIfResolution(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        String firstName = getPersona(config).getName().split(" ")[0];

        StringBuilder resolution = new StringBuilder();
        resolution.append("**What if ").append(firstName).append(" could eliminate emergency allocation decisions?**\n");
        resolution.append("- ").append(config.getAiSpecialists()).append(" AI specialists optimally utilized\n");
        resolution.append("- $2.1M in emergency contractor costs eliminated\n");
        resolution.append("- Client escalations reduced by 60%\n");
        resolution.append("- Project success rate: 60% → 95%\n\n");
        resolution.append("*").append(firstName).append("'s transformation: \"Now I manage talent optimization, not crisis response.\"*");

        return resolution.toString();
    }

    private String generateRootCauseExplanation(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return "\"Your resource constraint mystery isn't about having too few people. It's about allocation decisions made under client pressure that systematically bypass expertise, creating artificial scarcity while specialists remain underutilized.\"";
    }

    private List<String> generateDiscoveryChain(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return Arrays.asList(
                "**Surface Problem:** AI projects report resource constraints",
                "**Traditional Response:** Hire more AI specialists",
                String.format("**Aurora's Discovery:** %d%% of allocations ignore optimal resource matching",
                        (int)((double) eventAnalysis.getAllocationOverrides() / eventAnalysis.getAllocationDecisions() * 100)),
                "**Root Cause:** Client escalations create pressure-driven decisions that bypass expertise",
                "**Actual Solution:** Fix allocation decision governance, not headcount"
        );
    }

    private List<String> generateKeyMetrics(OrganizationConfiguration config, EventAnalysis eventAnalysis, double overrideRate, int benchCost) {
        return Arrays.asList(
                String.format("**Decision Override Rate:** %d management overrides out of %d decisions (%.1f%% override rate)",
                        eventAnalysis.getAllocationOverrides(), eventAnalysis.getAllocationDecisions(), overrideRate),
                String.format("**Client Pressure Correlation:** %d escalations creating emergency allocation pressure",
                        eventAnalysis.getClientEscalations()),
                String.format("**Resource Paradox Scale:** %d AI specialists hired, %d projects experiencing resource constraints",
                        config.getAiSpecialists(), config.getAiProjects()),
                String.format("**Bench Impact:** $%,d in underutilized specialist capacity annually", benchCost)
        );
    }

    private String generateDeeperDiscovery(OrganizationConfiguration config, EventAnalysis eventAnalysis) {
        return "*\"Which clients create the most allocation pressure?\"*\n**Aurora Found:** New AI clients (those without prior ML experience) generate 3x more emergency allocation requests because initial scoping underestimates complexity.";
    }

    private List<String> generateActionableInsights(OrganizationConfiguration config) {
        if (config.getTotalConsultants() < 100) {
            return Arrays.asList(
                    "Implement mandatory 70% skill match threshold with your personal approval required for overrides",
                    "Deploy weekly AI allocation reviews with direct consultant-to-project matching validation",
                    "Create AI project urgency protocols that prevent panic allocation decisions",
                    "Establish contractor prohibition policy unless AI specialists are at 85%+ utilization"
            );
        } else if (config.getTotalConsultants() < 1000) {
            return Arrays.asList(
                    "Deploy AI-aware allocation decision support system with 70% minimum skill match enforcement",
                    "Require Practice Lead approval for all management overrides of AI allocation recommendations",
                    "Implement emergency allocation audit process with 48-hour decision review window",
                    "Create AI specialist utilization dashboard with real-time bench cost visibility"
            );
        } else {
            return Arrays.asList(
                    "Deploy enterprise AI allocation governance framework with executive override requirements",
                    "Implement real-time AI specialist utilization tracking across all global offices",
                    "Create AI project urgency classification system preventing emergency allocation decisions",
                    "Establish dedicated AI allocation committee with technical expertise requirement"
            );
        }
    }
}