package org.dlrb.loan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class InstallmentCalculator {
    private static final MathContext PRECISION = MathContext.DECIMAL128;
    private static final int MONEY_SCALE = 2;

    public BigDecimal calculateMonthlyInstallment(LoanApplication application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        BigDecimal principal = application.getAmount();
        BigDecimal rate = application.getLoanType().getMonthlyRate();
        int months = application.getTermMonths();

        BigDecimal growth = BigDecimal.ONE.add(rate).pow(months, PRECISION);
        BigDecimal numerator = principal.multiply(rate).multiply(growth);
        BigDecimal denominator = growth.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, PRECISION)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the monthly installment (rule R6), rounded to 2 decimals.
     * @param application the loan application
     * @return the monthly installment
     * @throws IllegalArgumentException if application is null
     */

    public BigDecimal calculateTotalRepayment(LoanApplication application) {
        BigDecimal installment = calculateMonthlyInstallment(application);
        return installment.multiply(BigDecimal.valueOf(application.getTermMonths()));
    }
}
