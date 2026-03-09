package com.aurorapoc.datagenerator.config.calculators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.config.OrganizationExpectations;

public class StandardCalculator extends com.aurorapoc.datagenerator.config.calculators.OrganizationCalculator {

    @Override
    public OrganizationExpectations calculateExpectations(OrganizationConfiguration config) {
        // Standard mid-market firm - more realistic expectations accounting for generation randomness
        return new OrganizationExpectations.Builder()
                .expectedStrategicAllocations(Math.max(5, (int)(config.getStrategicProjects() * 0.6))) // Reduced from 0.7
                .expectedOperationalAllocations(Math.max(2, (int)(config.getOperationalProjects() * 0.25))) // Reduced from 0.35
                .expectedAIProjectConstraints(Math.max(2, (int)(config.getAiProjects() * 0.4))) // Reduced from 0.5
                .expectedPoorSkillMatches(Math.max(1, config.getAiSpecialists() / 3)) // Reduced from /2
                .expectedClientExpectationUpdates(Math.max(1, config.getAiProjects() / 8)) // Reduced from /6
                .expectedOperationalDepartures(Math.max(0, config.getSeniorCohort() / 12)) // Reduced from /8, allow 0
                .build();
    }
}