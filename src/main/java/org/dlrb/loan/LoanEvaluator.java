package org.dlrb.loan;
import java.math.BigDecimal;

public class LoanEvaluator {
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 65;
    private static final int MIN_SCORE_FOR_LOAN = 700;
    private static final int MIN_SCORE_WITHOUT_GUARANTOR = 1200;
    private static final BigDecimal MAX_DEBT_RATIO = new BigDecimal("0.50");

    private final InstallmentCalculator calculator;

    public LoanEvaluator(InstallmentCalculator calculator) {
        if (calculator == null) {
            throw new IllegalArgumentException("Calculator cannot be null");
        }
        this.calculator = calculator;
    }

    /**
     * Evaluates a loan application by the business rules R1-R4.
     * @param application the loan application to evaluate
     * @return APPROVED, APPROVED_WITH_GUARANTOR or REJECTED
     * @throws IllegalArgumentException if application is null
     */

    public Decision evaluate(LoanApplication application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        Applicant applicant = application.getApplicant();

        if (applicant.getAge() < MIN_AGE || applicant.getAge() > MAX_AGE) {
            return Decision.REJECTED;
        }
        if (applicant.hasOverdueDebt()) {
            return Decision.REJECTED;
        }
        if (applicant.getCreditScore() < MIN_SCORE_FOR_LOAN) {
            return Decision.REJECTED;
        }

        BigDecimal installment = calculator.calculateMonthlyInstallment(application);
        BigDecimal maxInstallment = applicant.getMonthlyIncome().multiply(MAX_DEBT_RATIO);
        if (installment.compareTo(maxInstallment) > 0) {
            return Decision.REJECTED;
        }

        if (applicant.getCreditScore() < MIN_SCORE_WITHOUT_GUARANTOR) {
            return Decision.APPROVED_WITH_GUARANTOR;
        }
        return Decision.APPROVED;
    }
}
