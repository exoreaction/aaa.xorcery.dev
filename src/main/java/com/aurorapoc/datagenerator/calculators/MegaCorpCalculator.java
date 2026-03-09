package com.aurorapoc.datagenerator.config.calculators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.config.OrganizationExpectations;

public class MegaCorpCalculator extends com.aurorapoc.datagenerator.config.calculators.OrganizationCalculator {

    @Override
    public OrganizationExpectations calculateExpectations(OrganizationConfiguration config) {
        // Mega corp - high complexity, significant constraints
        return new OrganizationExpectations.Builder()
                .expectedStrategicAllocations(Math.max(15, (int)(config.getStrategicProjects() * 0.8)))
                .expectedOperationalAllocations(Math.max(8, (int)(config.getOperationalProjects() * 0.4)))
                .expectedAIProjectConstraints(Math.max(10, (int)(config.getAiProjects() * 0.6)))
                .expectedPoorSkillMatches(Math.max(15, (int)(config.getAiSpecialists() * 0.5)))
                .expectedClientExpectationUpdates(Math.max(5, config.getAiProjects() / 6))
                .expectedOperationalDepartures(Math.max(5, config.getSeniorCohort() / 5))
                .build();
    }
}