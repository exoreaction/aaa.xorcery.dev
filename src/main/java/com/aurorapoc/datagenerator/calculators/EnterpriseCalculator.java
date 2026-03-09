package com.aurorapoc.datagenerator.config.calculators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.config.OrganizationExpectations;

public class EnterpriseCalculator extends com.aurorapoc.datagenerator.config.calculators.OrganizationCalculator {

    @Override
    public OrganizationExpectations calculateExpectations(OrganizationConfiguration config) {
        // Enterprise - complex operations, more constraints
        return new OrganizationExpectations.Builder()
                .expectedStrategicAllocations(Math.max(8, (int)(config.getStrategicProjects() * 0.75)))
                .expectedOperationalAllocations(Math.max(4, (int)(config.getOperationalProjects() * 0.35)))
                .expectedAIProjectConstraints(Math.max(3, (int)(config.getAiProjects() * 0.55)))
                .expectedPoorSkillMatches(Math.max(5, (int)(config.getAiSpecialists() * 0.4)))
                .expectedClientExpectationUpdates(Math.max(2, config.getAiProjects() / 6))
                .expectedOperationalDepartures(Math.max(0, config.getSeniorCohort() / 10)) // Reduce from /6 to /10 - more lenient
                .build();
    }
}