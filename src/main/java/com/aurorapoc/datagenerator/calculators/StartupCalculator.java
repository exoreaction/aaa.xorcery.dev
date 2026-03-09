package com.aurorapoc.datagenerator.config.calculators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.config.OrganizationExpectations;

public class StartupCalculator extends com.aurorapoc.datagenerator.config.calculators.OrganizationCalculator {

    @Override
    public OrganizationExpectations calculateExpectations(OrganizationConfiguration config) {
        // Startup-specific calculations - aggressive but small scale
        return new OrganizationExpectations.Builder()
                .expectedStrategicAllocations(Math.max(2, config.getStrategicProjects() / 2))
                .expectedOperationalAllocations(Math.max(1, config.getOperationalProjects() / 4))
                .expectedAIProjectConstraints(Math.max(1, config.getAiProjects() / 2))
                .expectedPoorSkillMatches(Math.max(1, config.getAiSpecialists() / 4))
                .expectedClientExpectationUpdates(Math.max(1, config.getAiProjects() / 8))
                .expectedOperationalDepartures(0) // Startups rarely have retention crisis patterns
                .build();
    }
}