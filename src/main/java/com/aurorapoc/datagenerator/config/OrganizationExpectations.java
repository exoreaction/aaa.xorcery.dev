package com.aurorapoc.datagenerator.config;

public class OrganizationExpectations {
    private final int expectedStrategicAllocations;
    private final int expectedOperationalAllocations;
    private final int expectedAIProjectConstraints;
    private final int expectedPoorSkillMatches;
    private final int expectedClientExpectationUpdates;
    private final int expectedOperationalDepartures;

    private OrganizationExpectations(Builder builder) {
        this.expectedStrategicAllocations = builder.expectedStrategicAllocations;
        this.expectedOperationalAllocations = builder.expectedOperationalAllocations;
        this.expectedAIProjectConstraints = builder.expectedAIProjectConstraints;
        this.expectedPoorSkillMatches = builder.expectedPoorSkillMatches;
        this.expectedClientExpectationUpdates = builder.expectedClientExpectationUpdates;
        this.expectedOperationalDepartures = builder.expectedOperationalDepartures;
    }

    // Getters
    public int getExpectedStrategicAllocations() { return expectedStrategicAllocations; }
    public int getExpectedOperationalAllocations() { return expectedOperationalAllocations; }
    public int getExpectedAIProjectConstraints() { return expectedAIProjectConstraints; }
    public int getExpectedPoorSkillMatches() { return expectedPoorSkillMatches; }
    public int getExpectedClientExpectationUpdates() { return expectedClientExpectationUpdates; }
    public int getExpectedOperationalDepartures() { return expectedOperationalDepartures; }

    public static class Builder {
        private int expectedStrategicAllocations;
        private int expectedOperationalAllocations;
        private int expectedAIProjectConstraints;
        private int expectedPoorSkillMatches;
        private int expectedClientExpectationUpdates;
        private int expectedOperationalDepartures;

        public Builder expectedStrategicAllocations(int value) {
            this.expectedStrategicAllocations = value;
            return this;
        }

        public Builder expectedOperationalAllocations(int value) {
            this.expectedOperationalAllocations = value;
            return this;
        }

        public Builder expectedAIProjectConstraints(int value) {
            this.expectedAIProjectConstraints = value;
            return this;
        }

        public Builder expectedPoorSkillMatches(int value) {
            this.expectedPoorSkillMatches = value;
            return this;
        }

        public Builder expectedClientExpectationUpdates(int value) {
            this.expectedClientExpectationUpdates = value;
            return this;
        }

        public Builder expectedOperationalDepartures(int value) {
            this.expectedOperationalDepartures = value;
            return this;
        }

        public OrganizationExpectations build() {
            return new OrganizationExpectations(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Expectations[strategic=%d, operational=%d, ai-constraints=%d, poor-matches=%d, expectation-updates=%d, departures=%d]",
                expectedStrategicAllocations, expectedOperationalAllocations, expectedAIProjectConstraints,
                expectedPoorSkillMatches, expectedClientExpectationUpdates, expectedOperationalDepartures);
    }
}