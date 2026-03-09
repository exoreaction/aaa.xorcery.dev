package com.aurorapoc.datagenerator.reports;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.reports.analysis.EventAnalysis;
import com.aurorapoc.datagenerator.reports.analysis.CompositeEventAnalyzer;
import com.aurorapoc.datagenerator.reports.analysis.WhyQuestionAnalyzer;
import com.aurorapoc.datagenerator.reports.analysis.WhyQuestionResult;
import com.aurorapoc.datagenerator.reports.analysis.AiTalentParadoxAnalyzer;
import com.aurorapoc.datagenerator.reports.analysis.SeniorRetentionCrisisAnalyzer;
import com.aurorapoc.datagenerator.reports.analysis.ProjectPerformanceCorrelationAnalyzer;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Enhanced Natural Language Executive Report Generator with company-level what-if framing
 * showcasing Aurora's WHY-based analytics capabilities through analyzer-driven content.
 */
public class NaturalLanguageExecutiveReportGenerator {

    private static final Logger logger = LoggerFactory.getLogger(NaturalLanguageExecutiveReportGenerator.class);

    private final CompositeEventAnalyzer eventAnalyzer;
    private final List<WhyQuestionAnalyzer> whyQuestionAnalyzers;

    public NaturalLanguageExecutiveReportGenerator() {
        this.eventAnalyzer = new CompositeEventAnalyzer();
        this.whyQuestionAnalyzers = Arrays.asList(
                new AiTalentParadoxAnalyzer(),
                new SeniorRetentionCrisisAnalyzer(),
                new ProjectPerformanceCorrelationAnalyzer()
        );
    }

    public String generateExecutiveReport(OrganizationConfiguration config,
                                          Map<String, JsonNode> eventStreams,
                                          int totalEvents) {
        logger.info("📝 Generating Aurora What-If Executive Report...");

        // Analyze actual event data using the composite analyzer
        EventAnalysis analysis = eventAnalyzer.analyzeEvents(eventStreams);

        // Generate Why Question analysis results
        JsonNode eventStreamsNode = createEventStreamsNode(eventStreams);
        List<WhyQuestionResult> whyResults = whyQuestionAnalyzers.stream()
                .map(analyzer -> analyzer.analyze(config, analysis, eventStreamsNode))
                .toList();

        StringBuilder report = new StringBuilder();

        // 1. Company-Level What-If Header
        appendCompanyLevelWhatIfIntro(report, config);

        // 2. The Analytics Gap Every Organization Faces
        appendAnalyticsGap(report, config, analysis, totalEvents);

        // 3. Three Leaders, Three Impossible Questions
        appendImpossibleQuestions(report, config, whyResults);

        // 4. The Aurora What-If Transformation
        appendAuroraTransformation(report, config, totalEvents);

        // 5. What-If Investigations (The 3 WHY scenarios)
        appendWhatIfInvestigations(report, config, analysis, whyResults);

        // 6. The Meta-Discovery: Connected Systems
        appendMetaDiscovery(report, config, analysis, whyResults);

        // 7. The What-If Business Case
        appendWhatIfBusinessCase(report, config, analysis, whyResults);

        // 8. The Aurora What-If Vision Realized
        appendWhatIfVisionRealized(report, config, totalEvents, whyResults);

        return report.toString();
    }

    private JsonNode createEventStreamsNode(Map<String, JsonNode> eventStreams) {
        // Convert Map to JsonNode for analyzer compatibility
        return eventStreams.values().iterator().hasNext() ?
                eventStreams.values().iterator().next() : null;
    }

    private void appendCompanyLevelWhatIfIntro(StringBuilder report, OrganizationConfiguration config) {
        report.append("# Aurora Business Intelligence: Executive Report\n\n");
        report.append("## The Enterprise What-If: Beyond Traditional Analytics\n\n");
        report.append("**What if your executives could finally get answers to the business questions that keep them awake at night?**\n\n");
        report.append("---\n\n");
    }

    private void appendAnalyticsGap(StringBuilder report, OrganizationConfiguration config,
                                    EventAnalysis analysis, int totalEvents) {
        report.append("## The Analytics Gap Every Organization Faces\n\n");

        // Add company context and scale
        report.append("### ").append(config.getOrganizationName()).append(" Context & Scale\n");
        report.append("**Organization Profile:** ").append(config.getContextDescription()).append("\n\n");

        report.append("**Enterprise Scale:**\n");
        report.append("- **").append(String.format("%,d", config.getTotalConsultants())).append(" consultants** across ")
                .append(config.getOfficeLocations()).append(" offices in ").append(config.getGeographicScope()).append(" markets\n");
        report.append("- **").append(config.getTotalProjects()).append(" active projects** including ")
                .append(config.getAiProjects()).append(" AI initiatives and ")
                .append(config.getStrategicProjects()).append(" strategic engagements\n");
        report.append("- **").append(config.getClients()).append(" client relationships** in ")
                .append(config.getRevenueRange()).append(" revenue segment\n");
        report.append("- **$").append(String.format("%,d", config.getAiSpecialists() * 200000))
                .append(" AI investment** across ").append(config.getAiSpecialists()).append(" specialists\n\n");

        // Add key events from the analysis period
        report.append("### Key Business Events (24-Month Analysis Period)\n");
        report.append("**").append(String.format("%,d", totalEvents)).append(" business events** tracked across five operational systems:\n\n");

        // HR/Talent Events
        report.append("**Talent & Human Capital:**\n");
        report.append("- ").append(analysis.getHiringEvents()).append(" new consultants hired (including ")
                .append(config.getAiSpecialists()).append(" AI specialists)\n");
        report.append("- ").append(analysis.getDepartureEvents()).append(" departures tracked (")
                .append(analysis.getSeniorDepartures()).append(" senior-level)\n");
        report.append("- ").append(analysis.getPromotionEvents()).append(" promotions and career advancement events\n");
        report.append("- ").append(analysis.getCertificationsCompleted()).append(" skill certifications completed\n\n");

        // Project & Delivery Events
        report.append("**Project & Delivery Operations:**\n");
        report.append("- ").append(analysis.getProjectsCreated()).append(" projects launched (")
                .append(analysis.getAiProjectsCreated()).append(" AI/ML focused)\n");
        report.append("- ").append(analysis.getProjectsCompleted()).append(" projects completed successfully\n");
        report.append("- ").append(analysis.getProjectDelays()).append(" delivery delays and timeline adjustments\n");
        report.append("- ").append(analysis.getBudgetVariances()).append(" budget variance events tracked\n\n");

        // Client & Business Events
        report.append("**Client & Business Performance:**\n");
        report.append("- ").append(analysis.getClientEscalations()).append(" client escalations requiring senior intervention\n");
        report.append("- ").append(analysis.getSatisfactionSurveys()).append(" client satisfaction assessments completed\n");
        if (analysis.getSatisfactionScoreCount() > 0) {
            report.append("- ").append(String.format("%.1f", analysis.getAverageSatisfactionScore()))
                    .append("/5.0 average client satisfaction score\n");
        }
        report.append("- ").append(analysis.getAllocationDecisions()).append(" resource allocation decisions made\n");
        report.append("- ").append(analysis.getAllocationOverrides()).append(" management overrides of systematic recommendations\n\n");

        // The traditional BI vs WHY gap
        report.append("### Traditional BI Excels at Reporting WHAT Happened\n");
        report.append("**Current Analytics Capabilities:**\n");
        report.append("- ✅ **Hiring Metrics:** ").append(analysis.getHiringEvents()).append(" new hires tracked across ")
                .append(config.getOfficeLocations()).append(" offices\n");
        report.append("- ✅ **Project Dashboards:** ").append(config.getTotalProjects()).append(" active projects with status, budget, timeline tracking\n");
        report.append("- ✅ **Client Satisfaction:** ");
        if (analysis.getSatisfactionScoreCount() > 0) {
            report.append(String.format("%.1f", analysis.getAverageSatisfactionScore())).append("/5.0 average measured across ")
                    .append(analysis.getSatisfactionSurveys()).append(" surveys\n");
        } else {
            report.append("Survey systems operational with regular feedback collection\n");
        }
        report.append("- ✅ **Financial Investment:** $").append(String.format("%,d", config.getAiSpecialists() * 200000))
                .append(" AI capability investment tracked and reported\n");
        report.append("- ✅ **Resource Utilization:** ").append(analysis.getAllocationDecisions())
                .append(" allocation decisions logged with utilization metrics\n\n");

        // The critical gap
        report.append("### But What About the WHY Questions That Drive Strategic Decisions?\n\n");

        report.append("**The Strategic Intelligence Gap:**\n");
        report.append("- Why do ").append(config.getAiProjects()).append(" AI projects struggle despite ")
                .append(config.getAiSpecialists()).append(" specialists?\n");
        report.append("- Why are ").append(analysis.getDepartureEvents()).append(" departures happening despite competitive compensation?\n");
        report.append("- Why do similar projects with ").append(analysis.getBudgetVariances())
                .append(" budget variances show different outcomes?\n");
        report.append("- Why do ").append(analysis.getClientEscalations()).append(" escalations keep recurring?\n");
        report.append("- Why do ").append(analysis.getAllocationOverrides())
                .append(" management overrides consistently override systematic recommendations?\n\n");

        report.append("**Traditional BI shows you the numbers. Aurora reveals the hidden patterns that explain them.**\n\n");
    }

    private void appendImpossibleQuestions(StringBuilder report, OrganizationConfiguration config,
                                           List<WhyQuestionResult> whyResults) {
        report.append("### Three Leaders, Three Impossible Questions\n\n");
        report.append("At ").append(config.getOrganizationName()).append(", three senior executives face strategic challenges that traditional analytics cannot solve:\n\n");

        for (int i = 0; i < whyResults.size(); i++) {
            WhyQuestionResult result = whyResults.get(i);
            WhyQuestionAnalyzer analyzer = whyQuestionAnalyzers.get(i);
            OrganizationConfiguration.PersonaProfile persona = analyzer.getPersona(config);

            report.append("**").append(persona.getName()).append(", ").append(persona.getTitle()).append(":**\n");
            report.append("*\"").append(result.getWhyQuestion()).append("\"*\n");
            report.append("- ").append(result.getTraditionalBILimitation()).append("\n\n");
        }
    }

    private void appendAuroraTransformation(StringBuilder report, OrganizationConfiguration config, int totalEvents) {
        report.append("## The Aurora What-If Transformation\n\n");
        report.append("**What if these impossible WHY questions became answerable through temporal event analysis?**\n\n");

        report.append("Aurora's breakthrough: Instead of analyzing data snapshots, analyze the story your data tells across time.\n\n");

        report.append("**").append(String.format("%,d", totalEvents)).append(" business events analyzed across 5 operational systems**\n");
        report.append("**24-month temporal correlation analysis**\n");
        report.append("**Cross-system pattern recognition**\n\n");

        report.append("### What if executives could trace the hidden connections between their biggest challenges?\n\n");
        report.append("---\n\n");
    }

    private void appendWhatIfInvestigations(StringBuilder report, OrganizationConfiguration config,
                                            EventAnalysis analysis, List<WhyQuestionResult> whyResults) {

        for (int i = 0; i < whyResults.size(); i++) {
            WhyQuestionResult result = whyResults.get(i);
            WhyQuestionAnalyzer analyzer = whyQuestionAnalyzers.get(i);
            appendWhatIfInvestigation(report, result, analyzer, config, analysis, i + 1);
        }
    }

    private void appendWhatIfInvestigation(StringBuilder report, WhyQuestionResult result,
                                           WhyQuestionAnalyzer analyzer, OrganizationConfiguration config,
                                           EventAnalysis analysis, int investigationNumber) {
        OrganizationConfiguration.PersonaProfile persona = analyzer.getPersona(config);

        report.append("## Investigation ").append(investigationNumber).append(": ")
                .append(result.getScenarioTitle()).append("\n\n");

        report.append("**").append(persona.getName()).append("'s WHY Question:**\n");
        report.append("*\"").append(result.getWhyQuestion()).append("\"*\n\n");

        // Use the investigative angle from the analyzer
        report.append("### ").append(result.getInvestigativeAngle()).append("\n\n");

        // Use the discovery process from the analyzer (includes what-if content)
        report.append(result.getDiscoveryProcess());

        // Show root cause explanation
        report.append("**Root Cause Revealed:** ").append(result.getRootCauseExplanation().replace("\"", "")).append("\n\n");

        // Use the what-if resolution from the analyzer
        report.append("### The What-If Resolution\n");
        report.append(result.getWhatIfResolution()).append("\n\n");

        report.append("---\n\n");
    }

    private void appendMetaDiscovery(StringBuilder report, OrganizationConfiguration config,
                                     EventAnalysis analysis, List<WhyQuestionResult> whyResults) {
        report.append("## The Meta-Discovery: What if all three WHYs are connected?\n\n");

        report.append("**Aurora's Systems Analysis Revealed:**\n\n");

        // Get the first names from each persona dynamically
        String sarah = whyResults.get(0) != null ?
                whyQuestionAnalyzers.get(0).getPersona(config).getName().split(" ")[0] : "Sarah";
        String marcus = whyResults.get(1) != null ?
                whyQuestionAnalyzers.get(1).getPersona(config).getName().split(" ")[0] : "Marcus";
        String jennifer = whyResults.get(2) != null ?
                whyQuestionAnalyzers.get(2).getPersona(config).getName().split(" ")[0] : "Jennifer";

        report.append("**What if ").append(sarah).append("'s allocation pressure creates ").append(marcus).append("'s retention problem?**\n");
        report.append("Emergency allocation → Operational assignments → Career limitations → Departure risk\n\n");

        report.append("**What if ").append(marcus).append("'s retention crisis affects ").append(jennifer).append("'s project performance?**\n");
        report.append("Senior departures → Knowledge loss → Degraded team composition → Poor outcomes\n\n");

        report.append("**What if ").append(jennifer).append("'s performance issues create ").append(sarah).append("'s client escalations?**\n");
        report.append("Project failures → Client escalations → Emergency allocation pressure → Cycle repeats\n\n");

        report.append("### The Enterprise What-If Moment\n");
        report.append("**What if ").append(config.getOrganizationName()).append(" doesn't have three separate problems, but one interconnected system?**\n\n");

        report.append("Aurora's temporal correlation reveals: Each WHY question is a symptom of allocation decision failures cascading across the organization.\n\n");
        report.append("---\n\n");
    }

    private void appendWhatIfBusinessCase(StringBuilder report, OrganizationConfiguration config,
                                          EventAnalysis analysis, List<WhyQuestionResult> whyResults) {
        report.append("## The What-If Business Case\n\n");

        report.append("### Financial Impact of WHY-Based Insights\n");
        report.append("| Mystery Solved | Annual Impact | Implementation Cost | ROI |\n");
        report.append("|----------------|---------------|-------------------|-----|\n");

        double totalAnnualImpact = 0;
        double totalImplementationCost = 0;

        for (WhyQuestionResult result : whyResults) {
            WhyQuestionResult.FinancialImpact impact = result.getFinancialImpact();
            double annualImpact = impact.getCurrentCost() + impact.getOpportunityCost();
            double implementationCost = impact.getImplementationCost();
            double roi = annualImpact / implementationCost;

            totalAnnualImpact += annualImpact;
            totalImplementationCost += implementationCost;

            report.append("| ").append(result.getScenarioTitle())
                    .append(" | $").append(String.format("%,d", (int)annualImpact))
                    .append(" | $").append(String.format("%,d", (int)implementationCost))
                    .append(" | ").append(String.format("%.1fx", roi)).append(" |\n");
        }

        double totalROI = totalAnnualImpact / totalImplementationCost;
        report.append("| **Total WHY Value** | **$").append(String.format("%,d", (int)totalAnnualImpact))
                .append("** | **$").append(String.format("%,d", (int)totalImplementationCost))
                .append("** | **").append(String.format("%.1fx", totalROI)).append("** |\n\n");

        report.append("### What if every organization could access WHY-based analytics?\n\n");

        report.append("**Competitive Advantage Through WHY Intelligence:**\n");
        report.append("- Move from reactive problem-solving to proactive pattern prevention\n");
        report.append("- Transform departmental silos into systems thinking\n");
        report.append("- Enable decision-making based on root causes, not symptoms\n");
        report.append("- Reduce time-to-insight from months to minutes\n\n");

        report.append("---\n\n");
    }

    private void appendWhatIfVisionRealized(StringBuilder report, OrganizationConfiguration config,
                                            int totalEvents, List<WhyQuestionResult> whyResults) {
        report.append("## The Aurora What-If Vision Realized\n\n");

        report.append("### Before Aurora: The Impossible Questions\n");
        report.append("- Executives with strategic challenges that data couldn't explain\n");
        report.append("- Traditional BI providing metrics without meaning\n");
        report.append("- Decision-making based on intuition and incomplete information\n");
        report.append("- Recurring problems without visible solutions\n\n");

        report.append("### After Aurora: The WHY-Powered Organization\n");

        // Get transformations dynamically from analyzer personas
        List<String> transformations = Arrays.asList(
                "From crisis management to talent optimization",
                "From retention defense to career architecture",
                "From performance variance to success engineering"
        );

        for (int i = 0; i < whyQuestionAnalyzers.size() && i < whyResults.size(); i++) {
            OrganizationConfiguration.PersonaProfile persona = whyQuestionAnalyzers.get(i).getPersona(config);
            String firstName = persona.getName().split(" ")[0];
            report.append("- **").append(firstName).append(":** ").append(transformations.get(i)).append("\n");
        }
        report.append("- **Organization:** From reactive management to predictive leadership\n\n");

        report.append("## The Ultimate What-If\n\n");
        report.append("**What if business intelligence could finally bridge the gap between data and decisions?**\n\n");
        report.append("**What if executives could ask their hardest questions and get actionable answers?**\n\n");
        report.append("**What if your organization could prevent problems by understanding their temporal patterns?**\n\n");

        report.append("Aurora's temporal analytics platform transforms the impossible into the inevitable - turning executive WHY questions into competitive advantages.\n\n");

        report.append("---\n\n");
        report.append("*This executive report demonstrates Aurora's temporal analytics capabilities through analysis of ")
                .append(String.format("%,d", totalEvents)).append(" simulated business events, revealing how WHY-based intelligence solves the strategic challenges that traditional analytics cannot address.*\n\n");

        report.append("*Report generated on ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")))
                .append(" by Aurora Business Intelligence Platform.*\n");
    }
}