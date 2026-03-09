package com.aurorapoc.datagenerator.generators;

import com.aurorapoc.datagenerator.config.OrganizationConfiguration;
import com.aurorapoc.datagenerator.generators.hr.RetentionCrisisGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class HREmploymentEventGenerator extends BaseEventGenerator {
    private static final Logger logger = LoggerFactory.getLogger(HREmploymentEventGenerator.class);

    // Use configuration instead of constants
    private int AI_SPECIALISTS_COUNT;
    private int SENIOR_COHORT_COUNT;
    private int TOTAL_CONSULTANTS;
    private int BASELINE_WORKFORCE; // Existing workforce at start of period

    // Timeline Constants
    private static final String AI_INITIATIVE_START = "2024-07-15T10:00:00Z";
    private static final String COHORT_PROMOTION_DATE = "2023-01-15T09:00:00Z";
    private  RetentionCrisisGenerator retentionCrisisGenerator;

    // Business Constants
    private static final int AVERAGE_PLANNED_SALARY = 95000;
    private static final int AVERAGE_ACTUAL_SALARY = 125000;

    public HREmploymentEventGenerator(ObjectMapper objectMapper, OrganizationConfiguration config) {
        super(objectMapper, config);
        this.AI_SPECIALISTS_COUNT = config.getAiSpecialists();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.TOTAL_CONSULTANTS = config.getTotalConsultants();
        this.BASELINE_WORKFORCE = calculateBaselineWorkforce();
        this.retentionCrisisGenerator = new RetentionCrisisGenerator(objectMapper, config);

    }

    public HREmploymentEventGenerator(ObjectMapper objectMapper, int startingEventId, OrganizationConfiguration config) {
        super(objectMapper, startingEventId, config);
        this.AI_SPECIALISTS_COUNT = config.getAiSpecialists();
        this.SENIOR_COHORT_COUNT = config.getSeniorCohort();
        this.TOTAL_CONSULTANTS = config.getTotalConsultants();
        this.BASELINE_WORKFORCE = calculateBaselineWorkforce();
        this.retentionCrisisGenerator = new RetentionCrisisGenerator(objectMapper, config);

    }

    // Organization Size Categories
    private enum OrganizationSizeCategory {
        STARTUP,      // < 50
        SMALL,        // 50-149
        STANDARD,     // 150-299
        ENTERPRISE,   // 300-999
        MEGA_CORP     // 1000+
    }

    // Realistic HR calculation results
    private static class RealisticHRCalculations {
        final int totalDepartures;
        final int totalNewHires;
        final int netGrowth;
        final double turnoverRate;
        final double growthRate;

        RealisticHRCalculations(int totalDepartures, int totalNewHires, int netGrowth,
                                double turnoverRate, double growthRate) {
            this.totalDepartures = totalDepartures;
            this.totalNewHires = totalNewHires;
            this.netGrowth = netGrowth;
            this.turnoverRate = turnoverRate;
            this.growthRate = growthRate;
        }
    }
    // Data Templates
    private static final List<String> CONSULTANT_NAMES = Arrays.asList(
            "Sarah Chen", "Marcus Rodriguez", "David Kim", "Jennifer Walsh",
            "Michael Chang", "Lisa Park", "Tom Wilson", "Anna Zhang",
            "Carlos Martinez", "Emily Johnson", "Raja Patel", "Sophie Miller",
            "Alex Thompson", "Maria Garcia", "James Liu", "Nina Patel",
            "Robert Taylor", "Priya Sharma", "Kevin O'Connor", "Zara Ahmed",
            "Jason Williams", "Amanda Foster", "Ryan Chen", "Isabella Martinez",
            "Daniel Anderson", "Grace Kim", "Samuel Torres", "Maya Patel",
            "Lucas Brown", "Aria Singh", "Ethan Davis", "Zoe Wang",
            "Oliver Martinez", "Luna Zhang", "Liam Johnson", "Nova Chen"
    );

    private static final List<String> AI_SKILLS = Arrays.asList(
            "Machine Learning", "Deep Learning", "Computer Vision", "Natural Language Processing",
            "TensorFlow", "PyTorch", "Python", "R", "AWS SageMaker", "Azure ML",
            "Scikit-learn", "Keras", "Apache Spark", "Data Engineering", "MLOps"
    );

    private static final List<ManagerProfile> MANAGER_PROFILES = Arrays.asList(
            new ManagerProfile("Alice Johnson", 0, 0.78, "Task-focused", 3, 0),
            new ManagerProfile("Bob Martinez", 3, 0.45, "Results-focused", 8, 5),
            new ManagerProfile("Carol Stevens", 1, 0.32, "Development-focused", 5, 2),
            new ManagerProfile("David Kim", 0, 0.67, "Process-focused", 4, 0),
            new ManagerProfile("Lisa Senior", 5, 0.21, "Strategic-focused", 6, 8),
            new ManagerProfile("Tom Rodriguez", 2, 0.55, "People-focused", 7, 1),
            new ManagerProfile("Sarah Wilson", 1, 0.43, "Quality-focused", 5, 3),
            new ManagerProfile("Mike Chen", 4, 0.35, "Innovation-focused", 4, 6)
    );

    // Helper Classes
    static class ManagerProfile {
        final String name;
        final int aiExperience;
        final double riskAversion;
        final String managementStyle;
        final int currentWorkload;
        final int principalPromotions;

        ManagerProfile(String name, int aiExperience, double riskAversion,
                       String managementStyle, int currentWorkload, int principalPromotions) {
            this.name = name;
            this.aiExperience = aiExperience;
            this.riskAversion = riskAversion;
            this.managementStyle = managementStyle;
            this.currentWorkload = currentWorkload;
            this.principalPromotions = principalPromotions;
        }
    }

    @Override
    public String getStreamName() {
        return "hr.employment";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
    @Override
    public void generateEvents(ArrayNode events) {
        OrganizationSizeCategory sizeCategory = determineOrganizationSize();

        logger.info("📝 Generating realistic HR events for {}-person {} organization...",
                TOTAL_CONSULTANTS, sizeCategory);

        // Calculate realistic hiring/departure numbers
        RealisticHRCalculations calculations = calculateRealisticHRNumbers();

        logger.info("📊 Realistic HR Numbers:");
        logger.info("   - Organization type: {}", sizeCategory);
        logger.info("   - Baseline workforce: {} consultants", BASELINE_WORKFORCE);
        logger.info("   - Annual turnover rate: {:.1f}%", calculations.turnoverRate * 100);
        logger.info("   - Growth rate (24 months): {:.1f}%", calculations.growthRate * 100);
        logger.info("   - Departures: {} over 24 months", calculations.totalDepartures);
        logger.info("   - New hires: {} over 24 months", calculations.totalNewHires);
        logger.info("   - Net growth: {} consultants", calculations.netGrowth);

        // Generate person entities for current workforce
        generateCurrentWorkforcePersons(events);

        // Generate realistic departure events
        generateRealisticDepartures(events, calculations.totalDepartures, sizeCategory);

        // Generate realistic new hire events
        generateRealisticNewHires(events, calculations.totalNewHires, sizeCategory);

        // Generate senior cohort promotions (subset of existing workforce)
        generateSeniorCohortPromotions(events);

        // Generate AI specialist hiring (new strategic hires)
        generateAISpecialistHiring(events);

        // Generate skills and certifications
        generateSkillsProgression(events);

        // Generate manager changes
        generateManagerChanges(events);

        // Generate performance reviews
        generatePerformanceReviews(events);

        // Generate career progression
        generateCareerProgressionEvents(events);

        // Generate departures (retention scenario)
        generateDepartureEvents(events);

        // Generate market-related events
        generateMarketRelatedEvents(events);

        // DEBUG: Check if retention generator exists
        logger.info("🔍 DEBUG: About to call retention generator. Generator exists: {}", retentionCrisisGenerator != null);

// Generate retention crisis patterns
        retentionCrisisGenerator.generateRetentionPatterns(events);
    }
    // Calculate baseline workforce based on organization size
    private int calculateBaselineWorkforce() {
        OrganizationSizeCategory category = determineOrganizationSize();

        switch (category) {
            case STARTUP:
                // Startups may have grown 100% in 24 months, so 50% baseline
                return Math.max(5, (int)(TOTAL_CONSULTANTS * 0.5));
            case SMALL:
                // Small companies growing fast, ~70% baseline
                return (int)(TOTAL_CONSULTANTS * 0.7);
            case STANDARD:
                // Standard growth, ~85% baseline
                return (int)(TOTAL_CONSULTANTS * 0.85);
            case ENTERPRISE:
                // More stable, ~90% baseline
                return (int)(TOTAL_CONSULTANTS * 0.9);
            case MEGA_CORP:
                // Very stable, ~95% baseline
                return (int)(TOTAL_CONSULTANTS * 0.95);
            default:
                return (int)(TOTAL_CONSULTANTS * 0.85);
        }
    }

    // Determine organization size category
    private OrganizationSizeCategory determineOrganizationSize() {
        if (TOTAL_CONSULTANTS < 50) return OrganizationSizeCategory.STARTUP;
        if (TOTAL_CONSULTANTS < 150) return OrganizationSizeCategory.SMALL;
        if (TOTAL_CONSULTANTS < 300) return OrganizationSizeCategory.STANDARD;
        if (TOTAL_CONSULTANTS < 1000) return OrganizationSizeCategory.ENTERPRISE;
        return OrganizationSizeCategory.MEGA_CORP;
    }

    // Get size-appropriate annual turnover rate
    private double getAnnualTurnoverRate(OrganizationSizeCategory category) {
        switch (category) {
            case STARTUP:
                return 0.25; // 25% - High turnover, rapid growth/changes
            case SMALL:
                return 0.18; // 18% - Above average, still growing
            case STANDARD:
                return 0.12; // 12% - Industry standard
            case ENTERPRISE:
                return 0.08; // 8% - Lower, more stable
            case MEGA_CORP:
                return 0.06; // 6% - Very stable, good retention
            default:
                return 0.12;
        }
    }

    // Get size-appropriate growth rate
    private double getGrowthRate(OrganizationSizeCategory category) {
        switch (category) {
            case STARTUP:
                return 0.50; // 50% growth over 24 months - aggressive startup growth
            case SMALL:
                return 0.30; // 30% growth - strong growth phase
            case STANDARD:
                return 0.15; // 15% growth - steady expansion
            case ENTERPRISE:
                return 0.08; // 8% growth - measured expansion
            case MEGA_CORP:
                return 0.05; // 5% growth - conservative, mature growth
            default:
                return 0.15;
        }
    }

    // Calculate realistic HR numbers based on organization size and type
    private RealisticHRCalculations calculateRealisticHRNumbers() {
        OrganizationSizeCategory sizeCategory = determineOrganizationSize();

        // Size-appropriate turnover and growth rates
        double annualTurnoverRate = getAnnualTurnoverRate(sizeCategory);
        double growthRate24Months = getGrowthRate(sizeCategory);

        // Calculate departures over 24 months
        int totalDepartures = (int)(BASELINE_WORKFORCE * annualTurnoverRate * 2.0);

        // Calculate actual growth needed to reach target
        int actualGrowthNeeded = TOTAL_CONSULTANTS - BASELINE_WORKFORCE;
        int growthHires = Math.max(0, actualGrowthNeeded);

        // Calculate replacement hires
        int replacementHires = totalDepartures;

        // Total new hires = growth + replacements
        int totalNewHires = growthHires + replacementHires;

        // Ensure minimums for very small orgs
        if (sizeCategory == OrganizationSizeCategory.STARTUP && totalNewHires < 3) {
            totalNewHires = Math.max(3, TOTAL_CONSULTANTS / 10); // At least some movement
            totalDepartures = Math.max(1, totalNewHires - actualGrowthNeeded);
        }

        return new RealisticHRCalculations(totalDepartures, totalNewHires, actualGrowthNeeded,
                annualTurnoverRate, growthRate24Months);
    }
    // Generate person entities for current workforce
    private void generateCurrentWorkforcePersons(ArrayNode events) {
        for (int i = 0; i < TOTAL_CONSULTANTS; i++) {
            String personName = CONSULTANT_NAMES.get(i % CONSULTANT_NAMES.size()) +
                    (i >= CONSULTANT_NAMES.size() ? " " + (i / CONSULTANT_NAMES.size() + 1) : "");

            ObjectNode event = createBaseEvent("PersonCreated", "person-" + (i + 1), "Person");

            // Most were created before the analysis period
            if (i < BASELINE_WORKFORCE) {
                event.put("timestamp", generateTimestamp("2020-01-01T09:00:00Z", 1095)); // Historical hires
            } else {
                event.put("timestamp", generateTimestamp("2023-01-01T09:00:00Z", 730)); // New hires in period
            }

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("displayName", personName);
            payload.put("firstName", personName.split(" ")[0]);
            payload.put("lastName", personName.split(" ")[1]);
            payload.put("email", personName.toLowerCase().replace(" ", ".") + "@techcorp.com");
            payload.put("nationality", "US");

            // Add education background
            ArrayNode education = objectMapper.createArrayNode();
            ObjectNode degree = objectMapper.createObjectNode();
            degree.put("degree", getRandomDegree());
            degree.put("institution", getRandomUniversity());
            degree.put("graduationYear", 2015 + random.nextInt(8));
            education.add(degree);
            payload.set("education", education);

            event.set("payload", payload);
            events.add(event);
        }
    }

    // Generate realistic departure events
    private void generateRealisticDepartures(ArrayNode events, int totalDepartures, OrganizationSizeCategory sizeCategory) {
        if (totalDepartures == 0) return;

        String[] departureMonths = {"2023-03-15", "2023-06-22", "2023-09-30", "2023-12-22",
                "2024-02-15", "2024-05-22", "2024-08-30", "2024-11-15"};

        // Size-appropriate departure reasons
        String[] startupReasons = {"Better equity opportunity", "Startup acquisition", "Founding own company", "Career pivot"};
        String[] enterpriseReasons = {"Career advancement", "Relocation", "Work-life balance", "Return to school"};
        String[] standardReasons = {"Better compensation", "Career growth", "Company culture", "New challenges"};

        String[] reasons = sizeCategory == OrganizationSizeCategory.STARTUP ? startupReasons :
                sizeCategory == OrganizationSizeCategory.ENTERPRISE || sizeCategory == OrganizationSizeCategory.MEGA_CORP ? enterpriseReasons :
                        standardReasons;

        // Generate departures spread over time
        for (int i = 0; i < totalDepartures; i++) {
            String consultantId = "emp-departed-" + (i + 1);
            String personId = "person-departed-" + (i + 1);
            String departureDate = departureMonths[i % departureMonths.length] + "T17:00:00Z";

            // Employment status change to DEPARTED
            ObjectNode departure = createBaseEvent("EmploymentStatusChanged", consultantId, "Employment");
            departure.put("timestamp", addDaysToTimestamp(departureDate, i * 3)); // Spread departures

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("previousStatus", "ACTIVE");
            payload.put("newStatus", "DEPARTED");
            payload.put("departureDate", departureDate.split("T")[0]);
            payload.put("departureReason", reasons[random.nextInt(reasons.length)]);

            // Size-appropriate tenure
            int minTenure = sizeCategory == OrganizationSizeCategory.STARTUP ? 6 : 12;
            int maxTenure = sizeCategory == OrganizationSizeCategory.MEGA_CORP ? 72 : 36;
            payload.put("tenure", (minTenure + random.nextInt(maxTenure - minTenure)) + " months");

            payload.put("finalLevel", i % 5 == 0 ? "Senior Consultant" : "Consultant");
            payload.put("monthsSinceLastPromotion", 6 + random.nextInt(30));
            payload.put("noticePeriod", sizeCategory == OrganizationSizeCategory.STARTUP ? "1 week" : "2 weeks");
            payload.put("exitInterviewCompleted", sizeCategory != OrganizationSizeCategory.STARTUP || random.nextBoolean());

            departure.set("payload", payload);
            events.add(departure);
        }
    }

    // Generate realistic new hire events
    private void generateRealisticNewHires(ArrayNode events, int totalNewHires, OrganizationSizeCategory sizeCategory) {
        if (totalNewHires == 0) return;

        // Generate new hires spread over 24 months
        for (int i = 0; i < totalNewHires; i++) {
            String consultantId = "emp-new-hire-" + (i + 1);
            String personId = "person-" + (BASELINE_WORKFORCE + i + 1);

            ObjectNode employmentEvent = createBaseEvent("EmploymentCreated", consultantId, "Employment");
            employmentEvent.put("timestamp", generateTimestamp("2023-01-01T09:00:00Z", 730)); // Spread over 24 months

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("personId", personId);
            payload.put("employeeId", "E-" + (2023000 + i));
            payload.put("level", getRandomConsultantLevel());
            payload.put("hiringDate", generateTimestamp("2023-01-01T09:00:00Z", 730).split("T")[0]);            payload.put("hiringDate", generateTimestamp("2023-01-01T09:00:00Z", 730).split("T")[0]);          payload.put("hiringDate", generateTimestamp("2023-01-01T09:00:00Z", 730).split("T")[0]);            payload.put("department", getRandomDepartment());
            payload.put("managerId", "emp-" + getRandomManager().name.toLowerCase().replace(" ", "-"));
            payload.put("status", "ACTIVE");
            payload.put("contractType", "PERMANENT");
            payload.put("location", getRandomLocation());

            // Size-appropriate salary ranges
            int baseSalary = getSizeAppropriateBaseSalary(sizeCategory);
            payload.put("startingSalary", baseSalary + random.nextInt(30000));

            employmentEvent.set("payload", payload);
            events.add(employmentEvent);
        }
    }

    // Get size-appropriate base salary
    private int getSizeAppropriateBaseSalary(OrganizationSizeCategory category) {
        switch (category) {
            case STARTUP:
                return 65000; // Lower base, equity compensation
            case SMALL:
                return 70000; // Competitive for growth
            case STANDARD:
                return 75000; // Market rate
            case ENTERPRISE:
                return 80000; // Above market
            case MEGA_CORP:
                return 90000; // Premium rates
            default:
                return 75000;
        }
    }
    private void generateSeniorCohortPromotions(ArrayNode events) {
        String promotionDate = COHORT_PROMOTION_DATE;

        for (int i = 0; i < SENIOR_COHORT_COUNT; i++) {
            String consultantId = "emp-senior-cohort-" + (i + 1);
            String personId = "person-" + (i + 1);

            // Employment creation (promotion event)
            ObjectNode employmentEvent = createBaseEvent("LevelChanged", consultantId, "Employment");
            employmentEvent.put("timestamp", addMinutesToTimestamp(promotionDate, i * 5)); // Spread over morning

            // Add correlation to cohort
            ObjectNode metadata = (ObjectNode) employmentEvent.get("metadata");
            metadata.put("correlationId", "senior-promotion-cohort-q1-2023");
            metadata.put("reason", "Promotion to Senior Consultant - Q1 2023 cohort");

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("personId", personId);
            payload.put("employeeId", "E-" + (2021000 + i));
            payload.put("previousLevel", "Consultant");
            payload.put("newLevel", "Senior Consultant");
            payload.put("promotionDate", promotionDate.split("T")[0]);
            payload.put("hiringDate", generatePastDate("2021-06-01", 540)); // Hired 18 months earlier
            payload.put("tenure", "18 months");
            payload.put("department", i % 3 == 0 ? "Digital Transformation" : (i % 3 == 1 ? "Enterprise Solutions" : "Cloud Architecture"));

            // Assign managers - strategic vs operational split affects manager assignment
            ManagerProfile manager = getManagerForCohortMember(i);
            payload.put("managerId", "emp-" + manager.name.toLowerCase().replace(" ", "-"));
            payload.put("status", "ACTIVE");
            payload.put("contractType", "PERMANENT");
            payload.put("location", getRandomLocation());

            employmentEvent.set("payload", payload);
            events.add(employmentEvent);
        }
    }

    private void generateAISpecialistHiring(ArrayNode events) {
        if (AI_SPECIALISTS_COUNT == 0) return; // No AI specialists to hire

        // Calculate hiring timeline and distribution based on organization size
        HiringStrategy strategy = calculateAIHiringStrategy();

        logger.info("📊 AI Hiring Strategy for {} specialists: {} over {} months",
                AI_SPECIALISTS_COUNT, strategy.approach, strategy.hiringMonths.length);

        int specialistIndex = 0;
        for (int month = 0; month < strategy.hiringMonths.length; month++) {
            for (int hire = 0; hire < strategy.monthlyHiring[month]; hire++) {
                if (specialistIndex >= AI_SPECIALISTS_COUNT) break;

                String consultantId = "emp-ai-specialist-" + (specialistIndex + 1);
                String personId = "person-" + (SENIOR_COHORT_COUNT + specialistIndex + 1);

                // Employment creation event
                ObjectNode employmentEvent = createBaseEvent("EmploymentCreated", consultantId, "Employment");
                String hiringDate = addDaysToTimestamp(strategy.hiringMonths[month] + "T09:00:00Z", hire * 3);
                employmentEvent.put("timestamp", hiringDate);

                ObjectNode metadata = (ObjectNode) employmentEvent.get("metadata");
                metadata.put("correlationId", "ai-initiative-2024");
                metadata.put("reason", "AI practice strategic hiring");
                metadata.put("hiringBatch", "Month-" + (month + 1));

                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("personId", personId);
                payload.put("employeeId", "E-2024-" + String.format("%04d", 89 + specialistIndex));
                payload.put("level", getAISpecialistLevel());
                payload.put("hiringDate", hiringDate.split("T")[0]);
                payload.put("department", "AI Practice");

                // Manager assignment - mix of experienced and inexperienced
                ManagerProfile manager = getAIManager(specialistIndex);
                payload.put("managerId", "emp-" + manager.name.toLowerCase().replace(" ", "-"));
                payload.put("status", "ACTIVE");
                payload.put("contractType", "PERMANENT");
                payload.put("location", getRandomLocation());

                // Salary variance (budget vs actual) - key to AI paradox scenario
                int budgetedSalary = AVERAGE_PLANNED_SALARY;
                int actualSalary = AVERAGE_ACTUAL_SALARY + random.nextInt(30000); // Market inflation
                payload.put("budgetedSalary", budgetedSalary);
                payload.put("startingSalary", actualSalary);
                payload.put("salaryVariance", (double)(actualSalary - budgetedSalary) / budgetedSalary);

                employmentEvent.set("payload", payload);
                events.add(employmentEvent);

                specialistIndex++;
            }
        }

        logger.info("✅ Generated {} AI specialist hiring events", specialistIndex);
    }

    // Add these new methods to the class
    private HiringStrategy calculateAIHiringStrategy() {
        OrganizationSizeCategory sizeCategory = determineOrganizationSize();

        HiringStrategy strategy = new HiringStrategy();

        switch (sizeCategory) {
            case STARTUP:
                // Quick, concentrated hiring over 2-3 months
                strategy.hiringMonths = new String[]{"2024-07-15", "2024-08-15", "2024-09-15"};
                strategy.monthlyHiring = distributeHiring(AI_SPECIALISTS_COUNT, 3, HiringPattern.FRONT_LOADED);
                strategy.approach = "Rapid scaling";
                break;

            case SMALL:
                // Steady hiring over 4 months
                strategy.hiringMonths = new String[]{"2024-07-15", "2024-08-15", "2024-09-15", "2024-10-15"};
                strategy.monthlyHiring = distributeHiring(AI_SPECIALISTS_COUNT, 4, HiringPattern.EVEN);
                strategy.approach = "Steady expansion";
                break;

            case STANDARD:
                // Standard 5-month rollout
                strategy.hiringMonths = new String[]{"2024-07-15", "2024-08-15", "2024-09-15", "2024-10-15", "2024-11-15"};
                strategy.monthlyHiring = distributeHiring(AI_SPECIALISTS_COUNT, 5, HiringPattern.EVEN);
                strategy.approach = "Phased rollout";
                break;

            case ENTERPRISE:
                // Extended 6-month hiring with ramp-up
                strategy.hiringMonths = new String[]{"2024-06-15", "2024-07-15", "2024-08-15", "2024-09-15", "2024-10-15", "2024-11-15"};
                strategy.monthlyHiring = distributeHiring(AI_SPECIALISTS_COUNT, 6, HiringPattern.RAMP_UP);
                strategy.approach = "Strategic ramp-up";
                break;

            case MEGA_CORP:
                // Extended 8-month hiring with controlled scaling
                strategy.hiringMonths = new String[]{"2024-05-15", "2024-06-15", "2024-07-15", "2024-08-15",
                        "2024-09-15", "2024-10-15", "2024-11-15", "2024-12-15"};
                strategy.monthlyHiring = distributeHiring(AI_SPECIALISTS_COUNT, 8, HiringPattern.CONTROLLED_SCALE);
                strategy.approach = "Enterprise-scale transformation";
                break;

            default:
                // Fallback to standard approach
                strategy.hiringMonths = new String[]{"2024-07-15", "2024-08-15", "2024-09-15", "2024-10-15", "2024-11-15"};
                strategy.monthlyHiring = distributeHiring(AI_SPECIALISTS_COUNT, 5, HiringPattern.EVEN);
                strategy.approach = "Standard rollout";
                break;
        }

        return strategy;
    }

    private int[] distributeHiring(int totalHires, int months, HiringPattern pattern) {
        int[] distribution = new int[months];

        switch (pattern) {
            case FRONT_LOADED:
                // Hire more in the first few months
                int frontLoad = (int)(totalHires * 0.6);
                distribution[0] = frontLoad;
                int remaining = totalHires - frontLoad;
                for (int i = 1; i < months; i++) {
                    distribution[i] = remaining / (months - 1);
                }
                // Adjust for remainder
                if (months > 1) {
                    distribution[1] += remaining % (months - 1);
                }
                break;

            case RAMP_UP:
                // Start slow, increase each month
                double sum = months * (months + 1) / 2.0;
                int assigned = 0;
                for (int i = 0; i < months - 1; i++) {
                    distribution[i] = (int)((double)totalHires * (i + 1) / sum);
                    assigned += distribution[i];
                }
                distribution[months - 1] = totalHires - assigned; // Remainder in last month
                break;

            case CONTROLLED_SCALE:
                // Conservative start, peak in middle, taper off
                int peak = months / 2;
                int aassigned = 0;
                for (int i = 0; i < months - 1; i++) {
                    if (i <= peak) {
                        distribution[i] = (int)((double)totalHires * (i + 1) / (months * 1.2));
                    } else {
                        distribution[i] = (int)((double)totalHires * (months - i) / (months * 1.2));
                    }
                    aassigned += distribution[i];
                }
                distribution[months - 1] = totalHires - aassigned; // Remainder in last month
                break;

            case EVEN:
            default:
                // Even distribution with remainder handling
                int basePerMonth = totalHires / months;
                int remainder = totalHires % months;

                for (int i = 0; i < months; i++) {
                    distribution[i] = basePerMonth + (i < remainder ? 1 : 0);
                }
                break;
        }

        return distribution;
    }

    // Add these helper classes at the end of the HREmploymentEventGenerator class
    private static class HiringStrategy {
        String[] hiringMonths;
        int[] monthlyHiring;
        String approach;
    }

    private enum HiringPattern {
        EVEN,           // Equal distribution across months
        FRONT_LOADED,   // Most hiring in first few months
        RAMP_UP,        // Gradual increase each month
        CONTROLLED_SCALE // Peak in middle, conservative start/end
    }

    private void generateAISpecialistHiring2(ArrayNode events) {
        String[] hiringMonths = {"2024-07-15", "2024-08-15", "2024-09-15", "2024-10-15", "2024-11-15"};
        int[] monthlyHiring = {2, 3, 2, 3, 2}; // Spread hiring across 5 months

        int specialistIndex = 0;
        for (int month = 0; month < hiringMonths.length; month++) {
            for (int hire = 0; hire < monthlyHiring[month]; hire++) {
                if (specialistIndex >= AI_SPECIALISTS_COUNT) break;

                String consultantId = "emp-ai-specialist-" + (specialistIndex + 1);
                String personId = "person-" + (SENIOR_COHORT_COUNT + specialistIndex + 1);

                // Employment creation event
                ObjectNode employmentEvent = createBaseEvent("EmploymentCreated", consultantId, "Employment");
                String hiringDate = addDaysToTimestamp(hiringMonths[month] + "T09:00:00Z", hire * 3);
                employmentEvent.put("timestamp", hiringDate);

                ObjectNode metadata = (ObjectNode) employmentEvent.get("metadata");
                metadata.put("correlationId", "ai-initiative-2024");
                metadata.put("reason", "AI practice strategic hiring");

                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("personId", personId);
                payload.put("employeeId", "E-2024-" + String.format("%04d", 89 + specialistIndex));
                payload.put("level", getAISpecialistLevel());
                payload.put("hiringDate", hiringDate.split("T")[0]);
                payload.put("department", "AI Practice");

                // Manager assignment - mix of experienced and inexperienced
                ManagerProfile manager = getAIManager(specialistIndex);
                payload.put("managerId", "emp-" + manager.name.toLowerCase().replace(" ", "-"));
                payload.put("status", "ACTIVE");
                payload.put("contractType", "PERMANENT");
                payload.put("location", getRandomLocation());

                // Salary variance (budget vs actual) - key to AI paradox scenario
                int budgetedSalary = AVERAGE_PLANNED_SALARY;
                int actualSalary = AVERAGE_ACTUAL_SALARY + random.nextInt(30000); // Market inflation
                payload.put("budgetedSalary", budgetedSalary);
                payload.put("startingSalary", actualSalary);
                payload.put("salaryVariance", (double)(actualSalary - budgetedSalary) / budgetedSalary);

                employmentEvent.set("payload", payload);
                events.add(employmentEvent);

                specialistIndex++;
            }
        }
    }
    // Generate skills progression for AI specialists
    private void generateSkillsProgression(ArrayNode events) {
        // Generate AI skills for specialists
        for (int i = 1; i <= AI_SPECIALISTS_COUNT; i++) {
            String consultantId = "emp-ai-specialist-" + i;
            generateAISkillsForConsultant(events, consultantId, i);
        }

        // Generate general skills for other consultants
        for (int i = 1; i <= SENIOR_COHORT_COUNT; i++) {
            String consultantId = "emp-senior-cohort-" + i;
            generateGeneralSkillsForConsultant(events, consultantId);
        }
    }

    private void generateAISkillsForConsultant(ArrayNode events, String consultantId, int specialistIndex) {
        // Each AI specialist gets 3-5 AI skills
        List<String> selectedSkills = getRandomAISkills(3 + random.nextInt(3));
        String baseDate = "2024-10-01T09:00:00Z";

        for (int skillIndex = 0; skillIndex < selectedSkills.size(); skillIndex++) {
            String skill = selectedSkills.get(skillIndex);

            // Skill certification started event
            ObjectNode startEvent = createBaseEvent("SkillCertificationStarted",
                    "cert-" + consultantId + "-" + skillIndex, "Certification");
            startEvent.put("timestamp", addDaysToTimestamp(baseDate, skillIndex * 7));

            ObjectNode startPayload = objectMapper.createObjectNode();
            startPayload.put("consultantId", consultantId);
            startPayload.put("skillId", "skill-" + skill.toLowerCase().replace(" ", "-"));
            startPayload.put("skillName", skill);
            startPayload.put("category", "AI_ML");
            startPayload.put("targetLevel", getRandomSkillLevel());
            startPayload.put("certificationProgram", "Internal " + skill + " Certification");
            startPayload.put("estimatedDuration", 14 + random.nextInt(14) + " days");
            startEvent.set("payload", startPayload);
            events.add(startEvent);

            // Skill certification completed event (some have delays)
            int actualDuration = 10 + random.nextInt(20); // 10-30 days
            ObjectNode completeEvent = createBaseEvent("SkillCertificationCompleted",
                    "cert-" + consultantId + "-" + skillIndex, "Certification");
            completeEvent.put("timestamp", addDaysToTimestamp(baseDate, skillIndex * 7 + actualDuration));

            ObjectNode completePayload = objectMapper.createObjectNode();
            completePayload.put("consultantId", consultantId);
            completePayload.put("skillId", "skill-" + skill.toLowerCase().replace(" ", "-"));
            completePayload.put("actualDuration", actualDuration + " days");
            completePayload.put("score", 75 + random.nextInt(25));
            completePayload.put("passingScore", 80);
            completePayload.put("certifiedLevel", startPayload.get("targetLevel"));
            completeEvent.set("payload", completePayload);
            events.add(completeEvent);

            // Add the skill to employment record
            ObjectNode skillAddedEvent = createBaseEvent("SkillAdded", consultantId, "Employment");
            skillAddedEvent.put("timestamp", completeEvent.get("timestamp"));

            ObjectNode skillPayload = objectMapper.createObjectNode();
            skillPayload.put("skillId", "skill-" + skill.toLowerCase().replace(" ", "-"));
            skillPayload.put("skillName", skill);
            skillPayload.put("category", "AI_ML");
            skillPayload.put("level", completePayload.get("certifiedLevel"));
            skillPayload.put("certifiedDate", completeEvent.get("timestamp").asText().split("T")[0]);
            skillPayload.put("yearsOfExperience", 2 + random.nextInt(6));
            skillAddedEvent.set("payload", skillPayload);
            events.add(skillAddedEvent);
        }
    }

    private void generateGeneralSkillsForConsultant(ArrayNode events, String consultantId) {
        // Generate 2-4 general skills per consultant
        List<String> generalSkills = Arrays.asList(
                "Java", "JavaScript", "Python", "React", "Angular", "Node.js", "Spring Boot",
                "PostgreSQL", "MongoDB", "AWS", "Azure", "Docker", "Kubernetes", "Git"
        );

        int skillCount = 2 + random.nextInt(3);
        List<String> selectedSkills = new java.util.ArrayList<>(generalSkills);
        java.util.Collections.shuffle(selectedSkills, random);

        String baseDate = "2023-02-01T09:00:00Z";

        for (int i = 0; i < skillCount; i++) {
            String skill = selectedSkills.get(i);

            ObjectNode skillEvent = createBaseEvent("SkillAdded", consultantId, "Employment");
            skillEvent.put("timestamp", addDaysToTimestamp(baseDate, i * 14 + random.nextInt(30)));

            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("skillId", "skill-" + skill.toLowerCase().replace(" ", "-"));
            payload.put("skillName", skill);
            payload.put("category", getSkillCategory(skill));
            payload.put("level", getRandomSkillLevel());
            payload.put("certifiedDate", addDaysToTimestamp(baseDate, i * 14).split("T")[0]);
            payload.put("yearsOfExperience", 1 + random.nextInt(8));

            skillEvent.set("payload", payload);
            events.add(skillEvent);
        }
    }
    private void generateManagerChanges(ArrayNode events) {
        // Generate manager changes for retention scenario (key factor in departures)

        // Operational track seniors experience more manager churn (retention scenario)
        for (int i = 16; i <= SENIOR_COHORT_COUNT; i++) { // Operational track
            String consultantId = "emp-senior-cohort-" + i;

            // First manager change (July 2023)
            ObjectNode change1 = createBaseEvent("ManagerChanged", consultantId, "Employment");
            change1.put("timestamp", "2023-07-01T09:00:00Z");

            ObjectNode payload1 = objectMapper.createObjectNode();
            payload1.put("previousManagerId", "emp-alice-johnson");
            payload1.put("newManagerId", "emp-bob-martinez");
            payload1.put("reason", "Organizational restructuring");
            payload1.put("transitionPeriod", "2 weeks");
            change1.set("payload", payload1);
            events.add(change1);

            // Second manager change (February 2024) - high churn for operational track
            ObjectNode change2 = createBaseEvent("ManagerChanged", consultantId, "Employment");
            change2.put("timestamp", "2024-02-15T09:00:00Z");

            ObjectNode payload2 = objectMapper.createObjectNode();
            payload2.put("previousManagerId", "emp-bob-martinez");
            payload2.put("newManagerId", "emp-carol-stevens");
            payload2.put("reason", "Manager departure");
            payload2.put("transitionPeriod", "1 week");
            change2.set("payload", payload2);
            events.add(change2);

            // Third manager change (September 2024) - excessive churn
            if (i > 20) { // Only for some operational track members
                ObjectNode change3 = createBaseEvent("ManagerChanged", consultantId, "Employment");
                change3.put("timestamp", "2024-09-01T09:00:00Z");

                ObjectNode payload3 = objectMapper.createObjectNode();
                payload3.put("previousManagerId", "emp-carol-stevens");
                payload3.put("newManagerId", "emp-david-kim");
                payload3.put("reason", "Team reorganization");
                payload3.put("transitionPeriod", "3 days");
                change3.set("payload", payload3);
                events.add(change3);
            }
        }

        // AI specialists get experienced managers initially, then some changes
        for (int i = 1; i <= AI_SPECIALISTS_COUNT; i++) {
            String consultantId = "emp-ai-specialist-" + i;

            // Some AI specialists get manager changes when inexperienced managers take over
            if (i > 6) { // Second half of AI specialists
                ObjectNode change = createBaseEvent("ManagerChanged", consultantId, "Employment");
                change.put("timestamp", "2024-10-01T09:00:00Z");

                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("previousManagerId", "emp-lisa-senior"); // Experienced AI manager
                payload.put("newManagerId", "emp-alice-johnson");    // Less experienced with AI
                payload.put("reason", "Capacity rebalancing");
                payload.put("transitionPeriod", "2 weeks");
                payload.put("notes", "Manager has limited AI/ML experience");
                change.set("payload", payload);
                events.add(change);
            }
        }
    }

    private void generatePerformanceReviews(ArrayNode events) {
        // Generate performance reviews for Q2 2023 and Q4 2023
        String[] reviewPeriods = {"2023-06-30T17:00:00Z", "2023-12-31T17:00:00Z"};
        String[] reviewQuarters = {"Q2-2023", "Q4-2023"};

        for (int period = 0; period < reviewPeriods.length; period++) {
            String reviewDate = reviewPeriods[period];
            String quarter = reviewQuarters[period];

            // Senior cohort reviews
            for (int i = 1; i <= SENIOR_COHORT_COUNT; i++) {
                String consultantId = "emp-senior-cohort-" + i;

                ObjectNode reviewEvent = createBaseEvent("PerformanceReviewCompleted",
                        "review-" + quarter + "-" + consultantId, "PerformanceReview");
                reviewEvent.put("timestamp", addDaysToTimestamp(reviewDate, i));

                ObjectNode payload = objectMapper.createObjectNode();
                payload.put("consultantId", consultantId);
                payload.put("reviewPeriod", quarter);
                payload.put("reviewDate", reviewDate.split("T")[0]);

                // Strategic vs operational track performance differences
                boolean isStrategicTrack = i <= (SENIOR_COHORT_COUNT * 0.6);

                if (isStrategicTrack) {
                    // Strategic track gets better reviews
                    payload.put("overallRating", 4.1 + random.nextDouble() * 0.8); // 4.1-4.9
                    payload.put("clientSatisfaction", 4.2 + random.nextDouble() * 0.7);
                    payload.put("technicalSkills", 4.0 + random.nextDouble() * 0.9);
                    payload.put("projectDelivery", 4.3 + random.nextDouble() * 0.6);
                    payload.put("careerTrajectory", "Senior Consultant → Principal track");
                    payload.put("developmentGoals", "Technical leadership and client relationship growth");
                } else {
                    // Operational track gets lower reviews
                    payload.put("overallRating", 3.2 + random.nextDouble() * 0.7); // 3.2-3.9
                    payload.put("clientSatisfaction", 3.0 + random.nextDouble() * 0.8);
                    payload.put("technicalSkills", 3.5 + random.nextDouble() * 0.8);
                    payload.put("projectDelivery", 3.1 + random.nextDouble() * 0.7);
                    payload.put("careerTrajectory", "Operational excellence focus");
                    payload.put("developmentGoals", "Process improvement and efficiency gains");
                }

                payload.put("managerFeedback", isStrategicTrack ?
                        "Strong contributor with client-facing potential" :
                        "Solid operational performer, focus on internal efficiency");

                payload.put("promotionReadiness", isStrategicTrack ? "Ready within 6 months" : "Needs 12-18 months development");

                reviewEvent.set("payload", payload);
                events.add(reviewEvent);
            }

            // AI specialist reviews (only for Q4 2023 after they were hired)
            if (period == 1) { // Q4 2023 only
                for (int i = 1; i <= AI_SPECIALISTS_COUNT; i++) {
                    String consultantId = "emp-ai-specialist-" + i;

                    ObjectNode reviewEvent = createBaseEvent("PerformanceReviewCompleted",
                            "review-" + quarter + "-" + consultantId, "PerformanceReview");
                    reviewEvent.put("timestamp", addDaysToTimestamp(reviewDate, i + SENIOR_COHORT_COUNT));

                    ObjectNode payload = objectMapper.createObjectNode();
                    payload.put("consultantId", consultantId);
                    payload.put("reviewPeriod", quarter);
                    payload.put("reviewDate", reviewDate.split("T")[0]);
                    payload.put("reviewType", "6-month new hire review");

                    // AI specialists get high ratings but allocation challenges noted
                    payload.put("overallRating", 4.2 + random.nextDouble() * 0.7); // 4.2-4.9
                    payload.put("technicalSkills", 4.5 + random.nextDouble() * 0.4);
                    payload.put("adaptability", 4.3 + random.nextDouble() * 0.6);
                    payload.put("teamwork", 4.0 + random.nextDouble() * 0.8);

                    // Manager experience affects review quality
                    boolean hasExperiencedManager = i <= 6;
                    if (hasExperiencedManager) {
                        payload.put("managerFeedback", "Excellent AI/ML skills, well-utilized on appropriate projects");
                        payload.put("projectAlignment", "Strong - matched to AI/ML projects");
                    } else {
                        payload.put("managerFeedback", "Strong technical skills, could benefit from more challenging assignments");
                        payload.put("projectAlignment", "Moderate - some skills underutilized");
                    }

                    payload.put("careerTrajectory", "Senior AI Specialist → AI Architect track");
                    payload.put("developmentGoals", "Deepen AI expertise and begin thought leadership");

                    reviewEvent.set("payload", payload);
                    events.add(reviewEvent);
                }
            }
        }
    }

    private void generateCareerProgressionEvents(ArrayNode events) {
        // Generate career development events for different tracks

        // Strategic track career progression
        for (int i = 1; i <= (int)(SENIOR_COHORT_COUNT * 0.6); i++) {
            String consultantId = "emp-senior-cohort-" + i;

            // Career development plan created
            ObjectNode careerPlan = createBaseEvent("CareerDevelopmentPlanCreated",
                    "plan-" + consultantId, "CareerDevelopment");
            careerPlan.put("timestamp", "2023-08-15T10:00:00Z");

            ObjectNode planPayload = objectMapper.createObjectNode();
            planPayload.put("consultantId", consultantId);
            planPayload.put("careerTrack", "Strategic Consultant → Principal");
            planPayload.put("targetPromotion", "Q2 2024");
            planPayload.put("requiredSkills", "Client relationship management, technical leadership, business development");
            planPayload.put("mentor", "emp-lisa-senior");
            planPayload.put("clientExposureTarget", "4+ strategic client interactions");
            careerPlan.set("payload", planPayload);
            events.add(careerPlan);

            // Client exposure opportunity
            ObjectNode clientExposure = createBaseEvent("ClientExposureOpportunityAssigned",
                    "exposure-" + consultantId, "Development");
            clientExposure.put("timestamp", "2023-10-01T09:00:00Z");

            ObjectNode exposurePayload = objectMapper.createObjectNode();
            exposurePayload.put("consultantId", consultantId);
            exposurePayload.put("clientId", "client-strategic-" + (i % 3 + 1));
            exposurePayload.put("role", "Technical Lead");
            exposurePayload.put("exposureType", "Direct client interaction");
            exposurePayload.put("expectedDuration", "6 months");
            clientExposure.set("payload", exposurePayload);
            events.add(clientExposure);
        }

        // Operational track - limited career progression
        for (int i = (int)(SENIOR_COHORT_COUNT * 0.6) + 1; i <= SENIOR_COHORT_COUNT; i++) {
            String consultantId = "emp-senior-cohort-" + i;

            // Basic development plan
            ObjectNode careerPlan = createBaseEvent("CareerDevelopmentPlanCreated",
                    "plan-" + consultantId, "CareerDevelopment");
            careerPlan.put("timestamp", "2023-09-15T10:00:00Z");

            ObjectNode planPayload = objectMapper.createObjectNode();
            planPayload.put("consultantId", consultantId);
            planPayload.put("careerTrack", "Operational Excellence");
            planPayload.put("targetPromotion", "TBD - performance dependent");
            planPayload.put("requiredSkills", "Process improvement, operational efficiency, team collaboration");
            planPayload.put("mentor", "TBA");
            planPayload.put("clientExposureTarget", "Internal stakeholders");
            careerPlan.set("payload", planPayload);
            events.add(careerPlan);

            // Internal project assignment (not client-facing)
            ObjectNode internalAssignment = createBaseEvent("InternalProjectAssigned",
                    "internal-" + consultantId, "Development");
            internalAssignment.put("timestamp", "2023-11-01T09:00:00Z");

            ObjectNode assignmentPayload = objectMapper.createObjectNode();
            assignmentPayload.put("consultantId", consultantId);
            assignmentPayload.put("projectType", "Internal operational improvement");
            assignmentPayload.put("role", "Process Specialist");
            assignmentPayload.put("clientFacing", false);
            assignmentPayload.put("expectedDuration", "9 months");
            assignmentPayload.put("visibilityLevel", "Department");
            internalAssignment.set("payload", assignmentPayload);
            events.add(internalAssignment);
        }

        // AI specialists career progression
        for (int i = 1; i <= AI_SPECIALISTS_COUNT; i++) {
            String consultantId = "emp-ai-specialist-" + i;

            // AI career track plan
            ObjectNode aiCareerPlan = createBaseEvent("CareerDevelopmentPlanCreated",
                    "plan-" + consultantId, "CareerDevelopment");
            aiCareerPlan.put("timestamp", "2024-09-01T10:00:00Z");

            ObjectNode aiPayload = objectMapper.createObjectNode();
            aiPayload.put("consultantId", consultantId);
            aiPayload.put("careerTrack", "AI Specialist → AI Architect");
            aiPayload.put("targetPromotion", "Q4 2024");
            aiPayload.put("requiredSkills", "Advanced ML algorithms, AI strategy, technical mentoring");
            aiPayload.put("mentor", "emp-lisa-senior");

            // Manager experience affects career planning quality
            boolean hasExperiencedManager = i <= 6;
            if (hasExperiencedManager) {
                aiPayload.put("aiProjectExposure", "Lead AI architect on 2+ strategic AI projects");
                aiPayload.put("thoughtLeadership", "Technical blog, conference speaking");
            } else {
                aiPayload.put("aiProjectExposure", "Support role on various projects");
                aiPayload.put("thoughtLeadership", "Internal knowledge sharing");
            }

            aiCareerPlan.set("payload", aiPayload);
            events.add(aiCareerPlan);
        }
    }
    private void generateDepartureEvents(ArrayNode events) {
        // Generate additional departure events specifically for retention scenario

        // Operational track departures (retention crisis)
        int operationalTrackDepartures = (int)(SENIOR_COHORT_COUNT * 0.4 * 0.8); // 80% of operational track

        for (int i = 0; i < operationalTrackDepartures; i++) {
            int consultantIndex = (int)(SENIOR_COHORT_COUNT * 0.6) + i + 1; // Operational track
            String consultantId = "emp-senior-cohort-" + consultantIndex;

            // Resignation submission
            ObjectNode resignation = createBaseEvent("ResignationSubmitted",
                    "resignation-" + consultantId, "Employment");

            // Stagger departures over time
            String[] departureDates = {"2024-01-15", "2024-03-22", "2024-05-10", "2024-07-08", "2024-09-16", "2024-11-12"};
            String departureDate = departureDates[i % departureDates.length] + "T16:00:00Z";
            resignation.put("timestamp", departureDate);

            ObjectNode resignationPayload = objectMapper.createObjectNode();
            resignationPayload.put("consultantId", consultantId);
            resignationPayload.put("reason", "Limited growth opportunities");
            resignationPayload.put("noticePeriod", "2 weeks");
            resignationPayload.put("counteroffer", false);
            resignationPayload.put("exitInterviewRequested", true);

            // Track patterns in operational track departures
            resignationPayload.put("track", "operational");
            resignationPayload.put("yearsOfService", 2.5 + random.nextDouble() * 2); // 2.5-4.5 years
            resignationPayload.put("lastPromotion", "18+ months ago");
            resignationPayload.put("managerChanges", 2 + random.nextInt(2)); // 2-3 manager changes
            resignationPayload.put("clientExposure", "Limited");

            resignation.set("payload", resignationPayload);
            events.add(resignation);

            // Exit interview
            ObjectNode exitInterview = createBaseEvent("ExitInterviewCompleted",
                    "exit-" + consultantId, "HR");
            exitInterview.put("timestamp", addDaysToTimestamp(departureDate, 7)); // Week later

            ObjectNode exitPayload = objectMapper.createObjectNode();
            exitPayload.put("consultantId", consultantId);
            exitPayload.put("primaryReason", "Career advancement limitations");
            exitPayload.put("secondaryReason", "Lack of client-facing opportunities");
            exitPayload.put("managerRating", 2.5 + random.nextDouble() * 1.5); // 2.5-4.0
            exitPayload.put("careerSupportRating", 2.0 + random.nextDouble() * 1.0); // 2.0-3.0
            exitPayload.put("recommendCompany", false);
            exitPayload.put("newRoleLevel", "Senior Consultant+ at competitor");
            exitPayload.put("salaryIncrease", "25-40%");

            exitPayload.put("feedback", "Limited opportunities for strategic project work and client interaction");

            exitInterview.set("payload", exitPayload);
            events.add(exitInterview);
        }
    }

    private void generateMarketRelatedEvents(ArrayNode events) {
        // Generate market condition events that affect hiring and retention

        // Market salary inflation event
        ObjectNode salaryInflation = createBaseEvent("MarketSalaryInflationDetected",
                "market-salary-2024", "MarketAnalysis");
        salaryInflation.put("timestamp", "2024-06-01T10:00:00Z");

        ObjectNode salaryPayload = objectMapper.createObjectNode();
        salaryPayload.put("skillCategory", "AI_ML");
        salaryPayload.put("inflationRate", 0.25); // 25% increase
        salaryPayload.put("averageMarketSalary", 125000);
        salaryPayload.put("ourAverageSalary", 95000);
        salaryPayload.put("competitiveGap", -0.24); // 24% below market
        salaryPayload.put("riskAssessment", "HIGH");
        salaryPayload.put("recommendedAction", "Immediate salary review for AI specialists");
        salaryInflation.set("payload", salaryPayload);
        events.add(salaryInflation);

        // Competitor hiring activity
        ObjectNode competitorHiring = createBaseEvent("CompetitorHiringActivityDetected",
                "competitor-analysis-2024", "MarketAnalysis");
        competitorHiring.put("timestamp", "2024-08-15T14:00:00Z");

        ObjectNode competitorPayload = objectMapper.createObjectNode();
        competitorPayload.put("competitorName", "TechSolutions Inc");
        competitorPayload.put("targetSkills", "AI, Machine Learning, Data Science");
        competitorPayload.put("hiringVolume", "50+ positions");
        competitorPayload.put("salaryPremium", 0.30); // 30% above market
        competitorPayload.put("targetCompanies", "Mid-market consulting firms");
        competitorPayload.put("riskToOurTalent", "VERY_HIGH");
        competitorPayload.put("employeesApproached", 8); // Our employees contacted
        competitorHiring.set("payload", competitorPayload);
        events.add(competitorHiring);

        // Economic downturn impact
        ObjectNode economicImpact = createBaseEvent("EconomicConditionsChanged",
                "economic-impact-2024", "MarketAnalysis");
        economicImpact.put("timestamp", "2024-03-01T12:00:00Z");

        ObjectNode economicPayload = objectMapper.createObjectNode();
        economicPayload.put("condition", "MILD_RECESSION");
        economicPayload.put("clientBudgetImpact", -0.15); // 15% budget reduction
        economicPayload.put("projectDelayIncrease", 0.20); // 20% more delays
        economicPayload.put("talentRetentionImpact", "POSITIVE"); // People less likely to leave
        economicPayload.put("newHiringImpact", "REDUCED"); // Slower hiring
        economicPayload.put("salaryPressure", "DOWNWARD");
        economicImpact.set("payload", economicPayload);
        events.add(economicImpact);

        // Industry AI boom event
        ObjectNode aiBoom = createBaseEvent("IndustryTrendIdentified",
                "ai-boom-2024", "MarketAnalysis");
        aiBoom.put("timestamp", "2024-05-01T10:00:00Z");

        ObjectNode boomPayload = objectMapper.createObjectNode();
        boomPayload.put("trend", "AI_MARKET_EXPANSION");
        boomPayload.put("growthRate", 1.45); // 145% year-over-year growth
        boomPayload.put("skillDemandIncrease", 2.3); // 230% increase in demand
        boomPayload.put("supplyGrowth", 0.15); // Only 15% increase in supply
        boomPayload.put("skillScarcity", "CRITICAL");
        boomPayload.put("impactOnRecruitment", "Extreme difficulty finding qualified candidates");
        boomPayload.put("impactOnRetention", "High risk of competitive poaching");
        boomPayload.put("strategicImplication", "AI talent becomes primary business constraint");
        aiBoom.set("payload", boomPayload);
        events.add(aiBoom);
    }
    // Helper methods for manager assignment and skill generation
    private ManagerProfile getRandomManager() {
        return MANAGER_PROFILES.get(random.nextInt(MANAGER_PROFILES.size()));
    }

    private ManagerProfile getManagerForCohortMember(int index) {
        // First 60% (strategic track) get better managers
        if (index < SENIOR_COHORT_COUNT * 0.6) {
            // Strategic track gets experienced managers with high principal promotions
            return MANAGER_PROFILES.stream()
                    .filter(m -> m.principalPromotions > 2)
                    .skip(index % 3)
                    .findFirst()
                    .orElse(MANAGER_PROFILES.get(0));
        } else {
            // Operational track gets less experienced managers
            return MANAGER_PROFILES.stream()
                    .filter(m -> m.principalPromotions <= 2)
                    .skip((index - (int)(SENIOR_COHORT_COUNT * 0.6)) % 3)
                    .findFirst()
                    .orElse(MANAGER_PROFILES.get(0));
        }
    }

    private ManagerProfile getAIManager(int specialistIndex) {
        // First 6 AI specialists get experienced AI managers
        if (specialistIndex < 6) {
            return MANAGER_PROFILES.stream()
                    .filter(m -> m.aiExperience > 0)
                    .skip(specialistIndex % 3)
                    .findFirst()
                    .orElse(MANAGER_PROFILES.get(0));
        } else {
            // Later AI specialists get managers without AI experience (allocation paradox)
            return MANAGER_PROFILES.stream()
                    .filter(m -> m.aiExperience == 0)
                    .skip((specialistIndex - 6) % 2)
                    .findFirst()
                    .orElse(MANAGER_PROFILES.get(0));
        }
    }

    private String getAISpecialistLevel() {
        String[] levels = {"Senior Data Scientist", "Senior AI Engineer", "Principal Data Scientist", "AI Architect"};
        return levels[random.nextInt(levels.length)];
    }

    private String getRandomConsultantLevel() {
        String[] levels = {"Junior Consultant", "Consultant", "Senior Consultant"};
        return levels[random.nextInt(levels.length)];
    }

    private String getRandomDepartment() {
        String[] departments = {"Digital Transformation", "Enterprise Solutions", "Cloud Architecture", "Data Analytics", "Cybersecurity"};
        return departments[random.nextInt(departments.length)];
    }



    protected String getRandomUniversity() {
        String[] universities = {"MIT", "Stanford", "UC Berkeley", "Carnegie Mellon", "University of Texas", "Georgia Tech"};
        return universities[random.nextInt(universities.length)];
    }

    private List<String> getRandomAISkills(int count) {
        List<String> shuffled = new java.util.ArrayList<>(AI_SKILLS);
        java.util.Collections.shuffle(shuffled, random);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    private String getRandomSkillLevel() {
        String[] levels = {"BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT"};
        return levels[random.nextInt(levels.length)];
    }

    private String getSkillCategory(String skill) {
        if (Arrays.asList("Java", "JavaScript", "Python", "Git").contains(skill)) {
            return "PROGRAMMING";
        } else if (Arrays.asList("React", "Angular").contains(skill)) {
            return "FRONTEND";
        } else if (Arrays.asList("Node.js", "Spring Boot").contains(skill)) {
            return "BACKEND";
        } else if (Arrays.asList("PostgreSQL", "MongoDB").contains(skill)) {
            return "DATA";
        } else if (Arrays.asList("AWS", "Azure", "Docker", "Kubernetes").contains(skill)) {
            return "CLOUD";
        } else {
            return "GENERAL";
        }
    }


}