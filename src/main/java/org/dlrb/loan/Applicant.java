package org.dlrb.loan;

import java.math.BigDecimal;

public class Applicant {
    private static final int MIN_VALID_AGE = 0;
    private static final int MAX_VALID_AGE = 120;
    private static final int MIN_CREDIT_SCORE = 0;
    private static final int MAX_CREDIT_SCORE = 1900;

    private final int age;
    private final BigDecimal monthlyIncome;
    private final int creditScore;
    private final boolean hasOverdueDebt;

    public Applicant(int age, BigDecimal monthlyIncome, int creditScore, boolean hasOverdueDebt) {
        if (age < MIN_VALID_AGE || age > MAX_VALID_AGE) {
            throw new IllegalArgumentException("Age must be between 0 and 120");
        }
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monthly income must be greater than zero");
        }
        if (monthlyIncome.scale() > 2) {
            throw new IllegalArgumentException("Monthly income cannot have more than 2 decimal places");
        }
        if (creditScore < MIN_CREDIT_SCORE || creditScore > MAX_CREDIT_SCORE) {
            throw new IllegalArgumentException("Credit score must be between 0 and 1900");
        }
        this.age = age;
        this.monthlyIncome = monthlyIncome;
        this.creditScore = creditScore;
        this.hasOverdueDebt = hasOverdueDebt;
    }

    public int getAge() {
        return age;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public boolean hasOverdueDebt() {
        return hasOverdueDebt;
    }
}
