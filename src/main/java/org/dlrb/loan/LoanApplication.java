package org.dlrb.loan;

import java.math.BigDecimal;

public class LoanApplication {
    private final Applicant applicant;
    private final LoanType loanType;
    private final BigDecimal amount;
    private final int termMonths;

    public LoanApplication(Applicant applicant, LoanType loanType, BigDecimal amount, int termMonths) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }
        if (loanType == null) {
            throw new IllegalArgumentException("Loan type cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount cannot have more than 2 decimal places");
        }
        if (amount.compareTo(loanType.getMinAmount()) < 0
                || amount.compareTo(loanType.getMaxAmount()) > 0) {
            throw new IllegalArgumentException("Amount is outside the limits of " + loanType);
        }
        if (termMonths < loanType.getMinTermMonths()
                || termMonths > loanType.getMaxTermMonths()) {
            throw new IllegalArgumentException("Term is outside the limits of " + loanType);
        }
        this.applicant = applicant;
        this.loanType = loanType;
        this.amount = amount;
        this.termMonths = termMonths;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getTermMonths() {
        return termMonths;
    }
}
