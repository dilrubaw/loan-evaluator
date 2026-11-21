package org.dlrb.loan;
import java.math.BigDecimal;

public enum LoanType {
    PERSONAL(new BigDecimal("1000"), new BigDecimal("500000"), 3, 36, new BigDecimal("0.030")),
    VEHICLE(new BigDecimal("1000"), new BigDecimal("1000000"), 3, 60, new BigDecimal("0.025"));

    private final BigDecimal minAmount;
    private final BigDecimal maxAmount;
    private final int minTermMonths;
    private final int maxTermMonths;
    private final BigDecimal monthlyRate;

    LoanType(BigDecimal minAmount, BigDecimal maxAmount,
             int minTermMonths, int maxTermMonths, BigDecimal monthlyRate) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minTermMonths = minTermMonths;
        this.maxTermMonths = maxTermMonths;
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public int getMinTermMonths() {
        return minTermMonths;
    }

    public int getMaxTermMonths() {
        return maxTermMonths;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }
}
