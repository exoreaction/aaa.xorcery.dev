package com.aurorapoc.datagenerator.reports.analysis;

import java.util.List;

/**
 * Contains the complete analysis result for a WHY question,
 * including the discovery process, actionable insights, and key events highlighting.
 */
public class WhyQuestionResult {

    private final String whyQuestion;
    private final String executiveQuote;
    private final String discoveryProcess;
    private final String rootCauseExplanation;
    private final List<String> discoveryChain;
    private final List<String> keyMetrics;
    private final String deeperDiscovery;
    private final List<String> actionableInsights;
    private final String temporalInsight;
    private final FinancialImpact financialImpact;
    private final String scenarioTitle;
    private final int scenarioNumber;
    private final String whatIfResolution;
    private final String investigativeAngle;
    private final String traditionalBILimitation;
    private final String keyEventsHighlight; // NEW

    public WhyQuestionResult(Builder builder) {
        this.whyQuestion = builder.whyQuestion;
        this.executiveQuote = builder.executiveQuote;
        this.discoveryProcess = builder.discoveryProcess;
        this.rootCauseExplanation = builder.rootCauseExplanation;
        this.discoveryChain = builder.discoveryChain;
        this.keyMetrics = builder.keyMetrics;
        this.deeperDiscovery = builder.deeperDiscovery;
        this.actionableInsights = builder.actionableInsights;
        this.temporalInsight = builder.temporalInsight;
        this.financialImpact = builder.financialImpact;
        this.scenarioTitle = builder.scenarioTitle;
        this.scenarioNumber = builder.scenarioNumber;
        this.whatIfResolution = builder.whatIfResolution;
        this.investigativeAngle = builder.investigativeAngle;
        this.traditionalBILimitation = builder.traditionalBILimitation;
        this.keyEventsHighlight = builder.keyEventsHighlight;
    }

    // Getters (including new one)
    public String getWhyQuestion() { return whyQuestion; }
    public String getExecutiveQuote() { return executiveQuote; }
    public String getDiscoveryProcess() { return discoveryProcess; }
    public String getRootCauseExplanation() { return rootCauseExplanation; }
    public List<String> getDiscoveryChain() { return discoveryChain; }
    public List<String> getKeyMetrics() { return keyMetrics; }
    public String getDeeperDiscovery() { return deeperDiscovery; }
    public List<String> getActionableInsights() { return actionableInsights; }
    public String getTemporalInsight() { return temporalInsight; }
    public FinancialImpact getFinancialImpact() { return financialImpact; }
    public String getScenarioTitle() { return scenarioTitle; }
    public int getScenarioNumber() { return scenarioNumber; }
    public String getWhatIfResolution() { return whatIfResolution; }
    public String getInvestigativeAngle() { return investigativeAngle; }
    public String getTraditionalBILimitation() { return traditionalBILimitation; }
    public String getKeyEventsHighlight() { return keyEventsHighlight; } // NEW

    // Builder pattern
    public static class Builder {
        private String whyQuestion;
        private String executiveQuote;
        private String discoveryProcess;
        private String rootCauseExplanation;
        private List<String> discoveryChain;
        private List<String> keyMetrics;
        private String deeperDiscovery;
        private List<String> actionableInsights;
        private String temporalInsight;
        private FinancialImpact financialImpact;
        private String scenarioTitle;
        private int scenarioNumber;
        private String whatIfResolution;
        private String investigativeAngle;
        private String traditionalBILimitation;
        private String keyEventsHighlight; // NEW

        public Builder whyQuestion(String whyQuestion) {
            this.whyQuestion = whyQuestion;
            return this;
        }

        public Builder executiveQuote(String quote) {
            this.executiveQuote = quote;
            return this;
        }

        public Builder discoveryProcess(String process) {
            this.discoveryProcess = process;
            return this;
        }

        public Builder rootCauseExplanation(String explanation) {
            this.rootCauseExplanation = explanation;
            return this;
        }

        public Builder discoveryChain(List<String> chain) {
            this.discoveryChain = chain;
            return this;
        }

        public Builder keyMetrics(List<String> metrics) {
            this.keyMetrics = metrics;
            return this;
        }

        public Builder deeperDiscovery(String discovery) {
            this.deeperDiscovery = discovery;
            return this;
        }

        public Builder actionableInsights(List<String> insights) {
            this.actionableInsights = insights;
            return this;
        }

        public Builder temporalInsight(String insight) {
            this.temporalInsight = insight;
            return this;
        }

        public Builder financialImpact(FinancialImpact impact) {
            this.financialImpact = impact;
            return this;
        }

        public Builder scenarioTitle(String title) {
            this.scenarioTitle = title;
            return this;
        }

        public Builder scenarioNumber(int number) {
            this.scenarioNumber = number;
            return this;
        }

        public Builder whatIfResolution(String resolution) {
            this.whatIfResolution = resolution;
            return this;
        }

        public Builder investigativeAngle(String angle) {
            this.investigativeAngle = angle;
            return this;
        }

        public Builder traditionalBILimitation(String limitation) {
            this.traditionalBILimitation = limitation;
            return this;
        }

        public Builder keyEventsHighlight(String keyEvents) { // NEW
            this.keyEventsHighlight = keyEvents;
            return this;
        }

        public WhyQuestionResult build() {
            return new WhyQuestionResult(this);
        }
    }

    // Financial Impact inner class (unchanged)
    public static class FinancialImpact {
        private final int currentCost;
        private final int preventableCost;
        private final int implementationCost;
        private final double roi;

        public FinancialImpact(int currentCost, int preventableCost, int implementationCost, double roi) {
            this.currentCost = currentCost;
            this.preventableCost = preventableCost;
            this.implementationCost = implementationCost;
            this.roi = roi;
        }

        public int getCurrentCost() { return currentCost; }
        public int getPreventableCost() { return preventableCost; }
        public int getOpportunityCost() { return preventableCost; }
        public int getImplementationCost() { return implementationCost; }
        public double getRoi() { return roi; }
    }
}