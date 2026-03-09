package com.aurorapoc.datagenerator.config.calculators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.config.OrganizationExpectations;

public abstract class OrganizationCalculator {

    public abstract OrganizationExpectations calculateExpectations(OrganizationConfiguration config);

    public static OrganizationCalculator forOrganizationSize(int consultants) {
        if (consultants < 50) return new StartupCalculator();
        if (consultants < 150) return new SmallBusinessCalculator();
        if (consultants < 300) return new StandardCalculator();
        if (consultants < 1000) return new EnterpriseCalculator();
        return new MegaCorpCalculator();
    }

    protected String getOrganizationType(int consultants) {
        if (consultants < 50) return "STARTUP";
        if (consultants < 150) return "SMALL_BUSINESS";
        if (consultants < 300) return "STANDARD";
        if (consultants < 1000) return "ENTERPRISE";
        return "MEGA_CORP";
    }
}