package com.aurorapoc.datagenerator.projection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Aurora AI Talent Scaling Projection Tests")
class AuroraAITalentScalingProjectionTest {

    private AITalentAnalyzer analyzer;
    private AIScalingProjector projector;
    private List<JsonNode> hrEvents;
    private List<JsonNode> projectEvents;
    private List<JsonNode> skillsEvents;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws Exception {
        mapper = new ObjectMapper();
        loadHistoricalEvents();
        analyzer = new AITalentAnalyzer(hrEvents, projectEvents, skillsEvents);
        projector = new AIScalingProjector(analyzer);
    }

    @Test
    @DisplayName("Analyze Current AI Talent Acquisition Patterns")
    void analyzeCurrentAITalentPatterns() {
        // Analyze historical AI specialist hiring
        var aiSpecialists = analyzer.getAISpecialists();
        var hiringPattern = analyzer.getHiringPattern();
        var salaryTrends = analyzer.getSalaryInflationPattern();

        // Validate current state matches observed data
        assertEquals(6, aiSpecialists.size(), "Should have 6 AI specialists hired");

        // Validate salary inflation pattern
        double averageAISalary = salaryTrends.getAverageAISalary();
        double budgetVariance = (averageAISalary - 95000) / 95000 * 100;
        assertTrue(budgetVariance >= 35 && budgetVariance <= 55,
                "AI salaries should be 35-55% above budget");

        // Analyze hiring velocity
        var hiringVelocity = hiringPattern.getMonthlyHiringRate();
        assertEquals(2.0, hiringVelocity, 0.5, "Hiring 2 AI specialists per month on average");

        System.out.println("=== Current AI Talent State ===");
        System.out.println("AI Specialists: " + aiSpecialists.size());
        System.out.println("Average Salary: $" + String.format("%.0f", averageAISalary));
        System.out.println("Budget Variance: +" + String.format("%.1f", budgetVariance) + "%");
        System.out.println("Monthly Hiring Rate: " + hiringVelocity + " specialists/month");
    }

    @Test
    @DisplayName("Project AI Talent Demand Growth")
    void projectAITalentDemandGrowth() {
        var currentDemand = analyzer.getCurrentAIDemand();
        var demandGrowthRate = analyzer.getDemandGrowthRate(); // 1.5-3.5x annually

        // Project demand for next 18 months
        var projectedDemand = projector.projectDemandGrowth(18);

        // Current demand: 24 specialists needed, 6 available = 18 shortage
        assertEquals(24, currentDemand.getRequiredSpecialists());
        assertEquals(6, currentDemand.getAvailableSpecialists());
        assertEquals(18, currentDemand.getShortage());

        // Validate growth projections
        var demand6Months = projectedDemand.getDemandAtMonth(6);
        var demand12Months = projectedDemand.getDemandAtMonth(12);
        var demand18Months = projectedDemand.getDemandAtMonth(18);

        assertTrue(demand6Months >= 30, "6-month demand should be 30+ specialists");
        assertTrue(demand12Months >= 42, "12-month demand should be 42+ specialists");
        assertTrue(demand18Months >= 60, "18-month demand should be 60+ specialists");

        System.out.println("=== AI Talent Demand Projection ===");
        System.out.println("Current Required: " + currentDemand.getRequiredSpecialists());
        System.out.println("Current Available: " + currentDemand.getAvailableSpecialists());
        System.out.println("Current Shortage: " + currentDemand.getShortage());
        System.out.println("Projected Demand (6m): " + demand6Months);
        System.out.println("Projected Demand (12m): " + demand12Months);
        System.out.println("Projected Demand (18m): " + demand18Months);
    }

    @Test
    @DisplayName("Model Certification and Productivity Ramp-Up Cycles")
    void modelCertificationAndProductivityCycles() {
        var certificationAnalysis = analyzer.getCertificationPatterns();
        var productivityAnalysis = analyzer.getProductivityRampPatterns();

        // Analyze certification completion timeline
        var avgCertificationTime = certificationAnalysis.getAverageCertificationTime();
        var certificationSuccess = certificationAnalysis.getSuccessRate();
        var bottlenecks = certificationAnalysis.getBottlenecks();

        assertEquals(3, avgCertificationTime, 0.5, "Average 3 months for AI certification");
        assertTrue(certificationSuccess >= 0.8, "80%+ certification success rate");
        assertFalse(bottlenecks.isEmpty(), "Should identify certification bottlenecks");

        // Model productivity ramp-up
        var productivityCurve = productivityAnalysis.getProductivityCurve();
        assertEquals(0.2, productivityCurve.getProductivityAtMonth(1), 0.1, "20% productive in month 1");
        assertEquals(0.5, productivityCurve.getProductivityAtMonth(3), 0.1, "50% productive in month 3");
        assertEquals(0.8, productivityCurve.getProductivityAtMonth(6), 0.1, "80% productive in month 6");
        assertEquals(1.0, productivityCurve.getProductivityAtMonth(9), 0.1, "Full productivity at month 9");

        // Project certification pipeline
        var pipeline = projector.projectCertificationPipeline(12);

        System.out.println("=== Certification & Productivity Analysis ===");
        System.out.println("Avg Certification Time: " + avgCertificationTime + " months");
        System.out.println("Success Rate: " + String.format("%.1f", certificationSuccess * 100) + "%");
        System.out.println("Bottlenecks: " + bottlenecks);
        System.out.println("Productivity Ramp: Month 1=" + productivityCurve.getProductivityAtMonth(1) +
                ", Month 6=" + productivityCurve.getProductivityAtMonth(6));
        System.out.println("Certified Pipeline (12m): " + pipeline.getCertifiedCount());
    }

    @Test
    @DisplayName("Forecast Budget Impact and Salary Inflation")
    void forecastBudgetImpactAndSalaryInflation() {
        var currentBudget = analyzer.getCurrentAIBudgetAllocation();
        var salaryInflation = analyzer.getSalaryInflationTrends();

        // Project salary inflation based on market competition
        var salaryProjection = projector.projectSalaryInflation(18);

        // Current state: $95K budget, actual $130K-$148K (37-56% over budget)
        assertEquals(95000, currentBudget.getBudgetedSalary());
        assertTrue(currentBudget.getActualAverageSalary() >= 130000, "Actual salaries $130K+");

        // Validate inflation projections
        var salary6m = salaryProjection.getProjectedSalaryAtMonth(6);
        var salary12m = salaryProjection.getProjectedSalaryAtMonth(12);
        var salary18m = salaryProjection.getProjectedSalaryAtMonth(18);

        assertTrue(salary6m >= 140000, "6-month projected salary should be $140K+");
        assertTrue(salary12m >= 155000, "12-month projected salary should be $155K+");
        assertTrue(salary18m >= 170000, "18-month projected salary should be $170K+");

        // Calculate total budget impact
        var budgetImpact = projector.calculateTotalBudgetImpact(18);
        var demandProjection = projector.projectDemandGrowth(18);

        long totalHiringCost18m = salary18m * demandProjection.getDemandAtMonth(18);
        long budgetedCost18m = 95000 * demandProjection.getDemandAtMonth(18);
        double budgetOverrun = ((double)(totalHiringCost18m - budgetedCost18m) / budgetedCost18m) * 100;

        assertTrue(budgetOverrun >= 200, "Budget overrun should exceed 200%");

        System.out.println("=== Budget Impact Projection ===");
        System.out.println("Current Budget: $" + currentBudget.getBudgetedSalary());
        System.out.println("Current Actual Avg: $" + String.format("%.0f", currentBudget.getActualAverageSalary()));
        System.out.println("Projected Salary (6m): $" + String.format("%.0f", salary6m));
        System.out.println("Projected Salary (12m): $" + String.format("%.0f", salary12m));
        System.out.println("Projected Salary (18m): $" + String.format("%.0f", salary18m));
        System.out.println("Total Budget Overrun (18m): +" + String.format("%.0f", budgetOverrun) + "%");
    }

    @Test
    @DisplayName("Predict When AI Skill Gap Will Close")
    void predictWhenAISkillGapWillClose() {
        var currentGap = analyzer.getCurrentSkillGap();
        var hiringProjection = projector.projectHiringPlan(24); // 24 months
        var productivityProjection = projector.projectEffectiveCapacity(24);

        // Current gap: need 24, have 6 effective = 18 gap
        assertEquals(18, currentGap.getEffectiveGap());

        // Find when gap closes (considering ramp-up time)
        int monthsToCloseGap = projector.calculateMonthsToCloseGap();

        // Should be 15-18 months considering:
        // - Need to hire 18+ specialists
        // - 2 specialists/month hiring rate = 9 months to hire
        // - Plus 6-9 months productivity ramp = 15-18 months total
        assertTrue(monthsToCloseGap >= 15 && monthsToCloseGap <= 21,
                "Gap should close in 15-21 months");

        // Validate capacity at gap closure
        var capacityAtClosure = productivityProjection.getEffectiveCapacityAtMonth(monthsToCloseGap);
        var demandAtClosure = projector.projectDemandGrowth(monthsToCloseGap).getDemandAtMonth(monthsToCloseGap);

        assertTrue(capacityAtClosure >= demandAtClosure * 0.9,
                "Should achieve 90%+ demand coverage when gap closes");

        System.out.println("=== Skill Gap Closure Projection ===");
        System.out.println("Current Effective Gap: " + currentGap.getEffectiveGap());
        System.out.println("Months to Close Gap: " + monthsToCloseGap);
        System.out.println("Capacity at Closure: " + capacityAtClosure);
        System.out.println("Demand at Closure: " + demandAtClosure);
        System.out.println("Coverage Ratio: " + String.format("%.1f", (capacityAtClosure / demandAtClosure) * 100) + "%");
    }

    @Test
    @DisplayName("Model Impact on Project Delivery and Client Satisfaction")
    void modelImpactOnProjectDeliveryAndClientSatisfaction() {
        var projectImpact = analyzer.getAIProjectPerformancePatterns();
        var satisfactionProjection = projector.projectClientSatisfaction(18);

        // Current state: AI projects delayed 4-6 weeks, 2.8/5 satisfaction
        var currentDelays = projectImpact.getAverageDelayWeeks();
        var currentSatisfaction = projectImpact.getAverageSatisfactionScore();

        assertTrue(currentDelays >= 4 && currentDelays <= 6, "Current delays 4-6 weeks");
        assertEquals(2.8, currentSatisfaction, 0.2, "Current satisfaction ~2.8/5");

        // Project improvements as skill gap closes
        var satisfaction6m = satisfactionProjection.getSatisfactionAtMonth(6);
        var satisfaction12m = satisfactionProjection.getSatisfactionAtMonth(12);
        var satisfaction18m = satisfactionProjection.getSatisfactionAtMonth(18);

        // Satisfaction should improve gradually
        assertTrue(satisfaction6m >= 2.8 && satisfaction6m <= 3.2, "Slow improvement in 6 months");
        assertTrue(satisfaction12m >= 3.5 && satisfaction12m <= 4.0, "Significant improvement in 12 months");
        assertTrue(satisfaction18m >= 4.0, "Good satisfaction by 18 months");

        // Model project success probability
        var successProbability = projector.projectAIProjectSuccessProbability(18);
        var currentSuccessRate = projectImpact.getCurrentSuccessRate();

        assertEquals(0.4, currentSuccessRate, 0.1, "Current success rate ~40%");
        assertTrue(successProbability.getProbabilityAtMonth(18) >= 0.8,
                "Success rate should reach 80%+ by month 18");

        System.out.println("=== Project Impact Projection ===");
        System.out.println("Current Avg Delay: " + currentDelays + " weeks");
        System.out.println("Current Satisfaction: " + currentSatisfaction + "/5");
        System.out.println("Current Success Rate: " + String.format("%.1f", currentSuccessRate * 100) + "%");
        System.out.println("Projected Satisfaction (18m): " + String.format("%.1f", satisfaction18m) + "/5");
        System.out.println("Projected Success Rate (18m): " + String.format("%.1f", successProbability.getProbabilityAtMonth(18) * 100) + "%");
    }

    @Test
    @DisplayName("Generate AI Talent Scaling Executive Report")
    void generateAITalentScalingExecutiveReport() {
        var report = projector.generateExecutiveScalingReport(18);

        // Validate report structure
        assertNotNull(report.getCurrentState());
        assertNotNull(report.getProjections());
        assertNotNull(report.getRisks());
        assertNotNull(report.getRecommendations());
        assertNotNull(report.getInvestmentAnalysis());

        // Key metrics should be present
        assertTrue(report.getProjections().containsKey("monthsToCloseGap"));
        assertTrue(report.getProjections().containsKey("totalBudgetImpact"));
        assertTrue(report.getProjections().containsKey("clientSatisfactionTrend"));

        // Recommendations should address key challenges
        var recommendations = report.getRecommendations();
        assertTrue(recommendations.stream().anyMatch(r -> r.contains("certification")));
        assertTrue(recommendations.stream().anyMatch(r -> r.contains("salary")));
        assertTrue(recommendations.stream().anyMatch(r -> r.contains("hiring")));

        System.out.println("=== AI Talent Scaling Executive Report ===");
        System.out.println(report.toFormattedString());

        // Write report to file
        report.writeToFile("generated-events/ai-talent-scaling-projection-report.json");
    }

    private void loadHistoricalEvents() throws Exception {
        hrEvents = loadJsonArray("generated-events/hr-employment-events-expanded.json");
        projectEvents = loadJsonArray("generated-events/project-management-events-expanded.json");
        skillsEvents = loadJsonArray("generated-events/skills-certification-events-expanded.json");
    }

    private List<JsonNode> loadJsonArray(String filename) throws Exception {
        File file = new File(filename);
        JsonNode rootNode = mapper.readTree(file);
        List<JsonNode> events = new ArrayList<>();
        if (rootNode.isArray()) {
            rootNode.forEach(events::add);
        }
        return events;
    }
}