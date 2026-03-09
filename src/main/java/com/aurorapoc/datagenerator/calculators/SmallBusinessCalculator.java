package com.aurorapoc.datagenerator.config.calculators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.config.OrganizationExpectations;

public class SmallBusinessCalculator extends com.aurorapoc.datagenerator.config.calculators.OrganizationCalculator {

    @Override
    public OrganizationExpectations calculateExpectations(OrganizationConfiguration config) {
        // Small business - growing but limited resources
        return new OrganizationExpectations.Builder()
                .expectedStrategicAllocations(Math.max(3, (int)(config.getStrategicProjects() * 0.6)))
                .expectedOperationalAllocations(Math.max(1, (int)(config.getOperationalProjects() * 0.3)))
                .expectedAIProjectConstraints(Math.max(1, (int)(config.getAiProjects() * 0.4)))
                .expectedPoorSkillMatches(Math.max(1, config.getAiSpecialists() / 3))
                .expectedClientExpectationUpdates(Math.max(1, config.getAiProjects() / 6))
                .expectedOperationalDepartures(Math.max(0, config.getSeniorCohort() / 12))
                .build();
    }
}