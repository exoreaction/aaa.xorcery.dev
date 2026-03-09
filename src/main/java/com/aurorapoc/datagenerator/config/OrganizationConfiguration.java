package com.aurorapoc.datagenerator.config;

import com.aurorapoc.datagenerator.calculators.OrganizationCalculator;

/**
 * Configuration for organization size and event generation parameters.
 * Uses builder pattern for flexible configuration with storytelling context.
 */
public class OrganizationConfiguration {

    // Core organizational structure
    private final int totalConsultants;
    private final int aiSpecialists;
    private final int seniorCohort;
    private final int managers;

    // Projects and clients
    private final int totalProjects;
    private final int aiProjects;
    private final int strategicProjects;
    private final int operationalProjects;
    private final int clients;

    // Event generation targets
    private final int targetHREvents;
    private final int targetProjectEvents;
    private final int targetClientEvents;
    private final int targetSkillsEvents;
    private final int targetAllocationEvents;

    // Business parameters
    private final String tenantId;
    private final String organizationName;
    private final double marketVolatility;
    private final double turnoverRate;

    // Storytelling context
    private final String organizationScale;
    private final String industryFocus;
    private final String geographicScope;
    private final String competitivePosition;
    private final int officeLocations;
    private final String revenueRange;

    // Persona information
    private final PersonaProfile aiLeaderProfile;
    private final PersonaProfile talentLeaderProfile;
    private final PersonaProfile operationsLeaderProfile;

    // Calculated expectations
    private final OrganizationExpectations expectations;

    // Private constructor - use builder
    private OrganizationConfiguration(Builder builder) {
        this.totalConsultants = builder.totalConsultants;
        this.aiSpecialists = builder.aiSpecialists;
        this.seniorCohort = builder.seniorCohort;
        this.managers = builder.managers;
        this.totalProjects = builder.totalProjects;
        this.aiProjects = builder.aiProjects;
        this.strategicProjects = builder.strategicProjects;
        this.operationalProjects = builder.operationalProjects;
        this.clients = builder.clients;
        this.targetHREvents = builder.targetHREvents;
        this.targetProjectEvents = builder.targetProjectEvents;
        this.targetClientEvents = builder.targetClientEvents;
        this.targetSkillsEvents = builder.targetSkillsEvents;
        this.targetAllocationEvents = builder.targetAllocationEvents;
        this.tenantId = builder.tenantId;
        this.organizationName = builder.organizationName;
        this.marketVolatility = builder.marketVolatility;
        this.turnoverRate = builder.turnoverRate;

        // Storytelling context
        this.organizationScale = builder.organizationScale;
        this.industryFocus = builder.industryFocus;
        this.geographicScope = builder.geographicScope;
        this.competitivePosition = builder.competitivePosition;
        this.officeLocations = builder.officeLocations;
        this.revenueRange = builder.revenueRange;

        // Personas
        this.aiLeaderProfile = builder.aiLeaderProfile;
        this.talentLeaderProfile = builder.talentLeaderProfile;
        this.operationsLeaderProfile = builder.operationsLeaderProfile;

        // Calculate expectations using appropriate calculator
        OrganizationCalculator calculator = OrganizationCalculator.forOrganizationSize(this.totalConsultants);
        this.expectations = calculator.calculateExpectations(this);
    }

    // All getters
    public int getTotalConsultants() { return totalConsultants; }
    public int getAiSpecialists() { return aiSpecialists; }
    public int getSeniorCohort() { return seniorCohort; }
    public int getManagers() { return managers; }
    public int getTotalProjects() { return totalProjects; }
    public int getAiProjects() { return aiProjects; }
    public int getStrategicProjects() { return strategicProjects; }
    public int getOperationalProjects() { return operationalProjects; }
    public int getClients() { return clients; }
    public int getTargetHREvents() { return targetHREvents; }
    public int getTargetProjectEvents() { return targetProjectEvents; }
    public int getTargetClientEvents() { return targetClientEvents; }
    public int getTargetSkillsEvents() { return targetSkillsEvents; }
    public int getTargetAllocationEvents() { return targetAllocationEvents; }
    public String getTenantId() { return tenantId; }
    public String getOrganizationName() { return organizationName; }
    public double getMarketVolatility() { return marketVolatility; }
    public double getTurnoverRate() { return turnoverRate; }

    // Storytelling getters
    public String getOrganizationScale() { return organizationScale; }
    public String getIndustryFocus() { return industryFocus; }
    public String getGeographicScope() { return geographicScope; }
    public String getCompetitivePosition() { return competitivePosition; }
    public int getOfficeLocations() { return officeLocations; }
    public String getRevenueRange() { return revenueRange; }
    public PersonaProfile getAiLeaderProfile() { return aiLeaderProfile; }
    public PersonaProfile getTalentLeaderProfile() { return talentLeaderProfile; }
    public PersonaProfile getOperationsLeaderProfile() { return operationsLeaderProfile; }

    // NEW: Expectations getter
    public OrganizationExpectations getExpectations() { return expectations; }

    // Calculated properties
    public int getAdditionalConsultants() {
        return totalConsultants - seniorCohort - aiSpecialists;
    }

    public int getTotalExpectedEvents() {
        return targetHREvents + targetProjectEvents + targetClientEvents +
                targetSkillsEvents + targetAllocationEvents;
    }

    // New storytelling helper methods
    public String getContextDescription() {
        return String.format("%s - %s %s firm with %d consultants across %d offices",
                organizationName, organizationScale, industryFocus, totalConsultants, officeLocations);
    }

    public String getInvestmentContext() {
        if (totalConsultants < 100) {
            return String.format("$%s investment in AI capabilities and %d specialist hires",
                    getScaledInvestmentAmount(), aiSpecialists);
        } else if (totalConsultants < 1000) {
            return String.format("$%s transformation initiative with %d AI specialists and %d active projects",
                    getScaledInvestmentAmount(), aiSpecialists, aiProjects);
        } else {
            return String.format("$%s enterprise AI strategy with %d-person specialist team managing %d concurrent projects",
                    getScaledInvestmentAmount(), aiSpecialists, aiProjects);
        }
    }

    private String getScaledInvestmentAmount() {
        if (totalConsultants < 100) return "2.5M";
        else if (totalConsultants < 500) return "8M";
        else if (totalConsultants < 1500) return "25M";
        else return "75M+";
    }

    // Persona Profile inner class
    public static class PersonaProfile {
        private final String name;
        private final String title;
        private final String background;
        private final String primaryConcern;
        private final int yearsWithCompany;
        private final String previousRole;

        public PersonaProfile(String name, String title, String background, String primaryConcern,
                              int yearsWithCompany, String previousRole) {
            this.name = name;
            this.title = title;
            this.background = background;
            this.primaryConcern = primaryConcern;
            this.yearsWithCompany = yearsWithCompany;
            this.previousRole = previousRole;
        }

        public String getName() { return name; }
        public String getTitle() { return title; }
        public String getBackground() { return background; }
        public String getPrimaryConcern() { return primaryConcern; }
        public int getYearsWithCompany() { return yearsWithCompany; }
        public String getPreviousRole() { return previousRole; }

        public String getIntroduction() {
            return String.format("%s, %s, brings %s to her role. With %d years at the company, " +
                            "she's primarily focused on %s.",
                    name, title, background, yearsWithCompany, primaryConcern);
        }
    }

    // Builder class with persona support
    public static class Builder {
        // Required parameters
        private int totalConsultants;

        // Optional parameters with defaults
        private int aiSpecialists;
        private int seniorCohort;
        private int managers;
        private int totalProjects;
        private int aiProjects;
        private int strategicProjects;
        private int operationalProjects;
        private int clients;
        private int targetHREvents;
        private int targetProjectEvents;
        private int targetClientEvents;
        private int targetSkillsEvents;
        private int targetAllocationEvents;
        private String tenantId = "techcorp-consulting";
        private String organizationName = "TechCorp Consulting";
        private double marketVolatility = 0.15;
        private double turnoverRate = 0.12;

        // Storytelling fields
        private String organizationScale;
        private String industryFocus = "technology consulting";
        private String geographicScope;
        private String competitivePosition;
        private int officeLocations;
        private String revenueRange;
        private PersonaProfile aiLeaderProfile;
        private PersonaProfile talentLeaderProfile;
        private PersonaProfile operationsLeaderProfile;

        public Builder(int totalConsultants) {
            this.totalConsultants = totalConsultants;
            calculateDefaults();
            generateStorytellingDefaults();
            createPersonaProfiles();
        }

        private void calculateDefaults() {
            // Intelligent defaults based on organization size
            this.aiSpecialists = Math.max(5, totalConsultants / 10);
            this.seniorCohort = Math.max(10, totalConsultants / 5);
            this.managers = Math.max(3, totalConsultants / 15);
            this.totalProjects = Math.max(10, totalConsultants / 3);
            this.aiProjects = Math.max(3, aiSpecialists / 2);
            this.strategicProjects = (int) (totalProjects * 0.4);
            this.operationalProjects = totalProjects - strategicProjects - aiProjects;
            this.clients = Math.max(5, totalProjects / 4);

            // Event targets based on organizational complexity
            this.targetHREvents = totalConsultants * 8 + 100;
            this.targetProjectEvents = totalProjects * 6 + 50;
            this.targetClientEvents = clients * 8 + 20;
            this.targetSkillsEvents = totalConsultants * 3 + 30;
            this.targetAllocationEvents = totalProjects * 5 + 40;
        }

        private void generateStorytellingDefaults() {
            if (totalConsultants < 50) {
                this.organizationScale = "boutique";
                this.geographicScope = "regional";
                this.competitivePosition = "specialized niche player";
                this.officeLocations = 1;
                this.revenueRange = "$5M-15M";
            } else if (totalConsultants < 200) {
                this.organizationScale = "mid-market";
                this.geographicScope = "national";
                this.competitivePosition = "growing specialist firm";
                this.officeLocations = 3;
                this.revenueRange = "$25M-75M";
            } else if (totalConsultants < 1000) {
                this.organizationScale = "enterprise";
                this.geographicScope = "international";
                this.competitivePosition = "established market player";
                this.officeLocations = 8;
                this.revenueRange = "$150M-500M";
            } else {
                this.organizationScale = "global powerhouse";
                this.geographicScope = "worldwide";
                this.competitivePosition = "industry leader";
                this.officeLocations = 25;
                this.revenueRange = "$1B+";
            }
        }

        private void createPersonaProfiles() {
            if (totalConsultants < 100) {
                // Small company personas
                this.aiLeaderProfile = new PersonaProfile(
                        "Sarah Chen", "AI Practice Lead",
                        "deep technical expertise in machine learning and 8 years of consulting experience",
                        "proving ROI on our AI investment while scaling the practice", 3, "Senior AI Consultant"
                );
                this.talentLeaderProfile = new PersonaProfile(
                        "Marcus Rodriguez", "Head of People Operations",
                        "HR leadership experience from both startups and established firms",
                        "building a strong culture while managing rapid growth", 2, "HR Business Partner"
                );
                this.operationsLeaderProfile = new PersonaProfile(
                        "Jennifer Walsh", "Managing Partner",
                        "15 years of consulting experience and strong client relationships",
                        "ensuring operational excellence while driving profitable growth", 6, "Senior Partner"
                );
            } else if (totalConsultants < 1000) {
                // Medium/Enterprise company personas
                this.aiLeaderProfile = new PersonaProfile(
                        "Sarah Chen", "AI Practice Director",
                        "deep technical expertise and proven track record scaling AI teams",
                        "delivering measurable business value from our AI investments", 4, "Practice Lead"
                );
                this.talentLeaderProfile = new PersonaProfile(
                        "Marcus Rodriguez", "VP of Talent Strategy",
                        "strategic talent management across high-growth consulting environments",
                        "developing career pathways that retain our best people", 5, "Talent Director"
                );
                this.operationsLeaderProfile = new PersonaProfile(
                        "Jennifer Walsh", "Chief Operating Officer",
                        "operational excellence and process optimization in consulting firms",
                        "ensuring consistent delivery quality across all client engagements", 7, "VP Operations"
                );
            } else {
                // Large/Mega company personas
                this.aiLeaderProfile = new PersonaProfile(
                        "Sarah Chen", "Chief AI Officer",
                        "transformation leadership and enterprise AI strategy",
                        "positioning the firm as the market leader in AI-driven consulting", 6, "Global AI Director"
                );
                this.talentLeaderProfile = new PersonaProfile(
                        "Marcus Rodriguez", "Chief People Officer",
                        "global talent strategy and organizational development at scale",
                        "creating career experiences that attract and retain the world's best consultants", 8, "Global Talent VP"
                );
                this.operationsLeaderProfile = new PersonaProfile(
                        "Jennifer Walsh", "President & Chief Operating Officer",
                        "global operations and strategic execution in professional services",
                        "driving operational excellence across our worldwide practice", 12, "Global Operations VP"
                );
            }
        }

        // Fluent builder methods
        public Builder aiSpecialists(int aiSpecialists) {
            this.aiSpecialists = aiSpecialists;
            this.aiProjects = Math.max(3, aiSpecialists / 2); // Recalculate
            return this;
        }

        public Builder seniorCohort(int seniorCohort) {
            this.seniorCohort = seniorCohort;
            return this;
        }

        public Builder managers(int managers) {
            this.managers = managers;
            return this;
        }

        public Builder totalProjects(int totalProjects) {
            this.totalProjects = totalProjects;
            // Recalculate project distribution
            this.strategicProjects = (int) (totalProjects * 0.4);
            this.operationalProjects = totalProjects - strategicProjects - aiProjects;
            return this;
        }

        public Builder clients(int clients) {
            this.clients = clients;
            return this;
        }

        public Builder eventTargets(int hrEvents, int projectEvents, int clientEvents,
                                    int skillsEvents, int allocationEvents) {
            this.targetHREvents = hrEvents;
            this.targetProjectEvents = projectEvents;
            this.targetClientEvents = clientEvents;
            this.targetSkillsEvents = skillsEvents;
            this.targetAllocationEvents = allocationEvents;
            return this;
        }

        public Builder tenant(String tenantId, String organizationName) {
            this.tenantId = tenantId;
            this.organizationName = organizationName;
            return this;
        }

        public Builder marketConditions(double volatility, double turnoverRate) {
            this.marketVolatility = volatility;
            this.turnoverRate = turnoverRate;
            return this;
        }

        // Storytelling builder methods
        public Builder organizationProfile(String scale, String industry, String scope) {
            this.organizationScale = scale;
            this.industryFocus = industry;
            this.geographicScope = scope;
            return this;
        }

        public Builder customPersonas(PersonaProfile ai, PersonaProfile talent, PersonaProfile ops) {
            this.aiLeaderProfile = ai;
            this.talentLeaderProfile = talent;
            this.operationsLeaderProfile = ops;
            return this;
        }

        // Volume scaling
        public Builder scaleVolume(double multiplier) {
            this.targetHREvents = (int) (targetHREvents * multiplier);
            this.targetProjectEvents = (int) (targetProjectEvents * multiplier);
            this.targetClientEvents = (int) (targetClientEvents * multiplier);
            this.targetSkillsEvents = (int) (targetSkillsEvents * multiplier);
            this.targetAllocationEvents = (int) (targetAllocationEvents * multiplier);
            return this;
        }

        public OrganizationConfiguration build() {
            // Validation
            if (totalConsultants <= 0) {
                throw new IllegalArgumentException("Total consultants must be positive");
            }
            if (aiSpecialists + seniorCohort > totalConsultants) {
                throw new IllegalArgumentException("AI specialists + senior cohort cannot exceed total consultants");
            }
            if (aiProjects + strategicProjects + operationalProjects != totalProjects) {
                throw new IllegalArgumentException("Project counts don't add up");
            }

            return new OrganizationConfiguration(this);
        }

        // Preset configurations for common scenarios
        public static Builder startup() {
            return new Builder(25)
                    .aiSpecialists(2)
                    .seniorCohort(5)
                    .managers(2)
                    .totalProjects(8)
                    .clients(4)
                    .eventTargets(180, 60, 30, 25, 35)
                    .tenant("startup-consulting", "Startup Tech Solutions")
                    .organizationProfile("boutique startup", "technology consulting", "regional")
                    .marketConditions(0.25, 0.20);
        }

        public static Builder smallBusiness() {
            return new Builder(75)
                    .aiSpecialists(6)
                    .seniorCohort(12)
                    .managers(5)
                    .totalProjects(20)
                    .clients(8)
                    .eventTargets(400, 120, 50, 60, 70)
                    .tenant("smallbiz-consulting", "Small Business Consulting")
                    .organizationProfile("growing specialist", "business consulting", "national")
                    .marketConditions(0.18, 0.15);
        }

        public static Builder enterprise() {
            return new Builder(500)
                    .aiSpecialists(35)
                    .seniorCohort(80)
                    .managers(25)
                    .totalProjects(120)
                    .clients(25)
                    .eventTargets(2900, 580, 225, 310, 330)
                    .tenant("enterprise-consulting", "Global Enterprise Solutions")
                    .organizationProfile("established enterprise", "management consulting", "international")
                    .marketConditions(0.10, 0.08);
        }

        public static Builder megaCorp() {
            return new Builder(2000)
                    .aiSpecialists(150)
                    .seniorCohort(300)
                    .managers(60)
                    .totalProjects(400)
                    .clients(50)
                    .eventTargets(8000, 2000, 800, 1000, 1200)
                    .tenant("megacorp-consulting", "MegaCorp Global Consulting")
                    .organizationProfile("global powerhouse", "strategic consulting", "worldwide")
                    .marketConditions(0.08, 0.05);
        }
    }

    // Static factory methods
    public static OrganizationConfiguration standard() {
        return new Builder(150)
                .aiSpecialists(12)
                .seniorCohort(25)
                .totalProjects(40)
                .clients(12)
                .eventTargets(650, 230, 84, 90, 126)
                .tenant("techcorp-consulting", "TechCorp Consulting")
                .organizationProfile("mid-market leader", "technology consulting", "national")
                .marketConditions(0.15, 0.12)
                .build();
    }

    public static OrganizationConfiguration smallBusiness() {
        return Builder.smallBusiness().build();
    }

    public static OrganizationConfiguration enterprise() {
        return Builder.enterprise().build();
    }

    public static OrganizationConfiguration megaCorp() {
        return Builder.megaCorp().build();
    }

    public static OrganizationConfiguration startup() {
        return Builder.startup().build();
    }

    @Override
    public String toString() {
        return String.format("%s: %s with %d consultants across %d offices, %d projects, ~%d events, %s",
                organizationName, organizationScale, totalConsultants, officeLocations,
                totalProjects, getTotalExpectedEvents(), expectations);
    }
}