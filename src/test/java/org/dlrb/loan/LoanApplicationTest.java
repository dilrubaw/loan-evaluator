package org.dlrb.loan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoanApplicationTest {

    private static final Applicant VALID_APPLICANT =
            new Applicant(30, new BigDecimal("20000"), 1500, false);
    private static final BigDecimal VALID_AMOUNT = new BigDecimal("50000");
    private static final int VALID_TERM = 12;

    @Test
    @DisplayName("Valid data creates a loan application")
    void createsApplicationWithValidData() {
        LoanApplication application =
                new LoanApplication(VALID_APPLICANT, LoanType.PERSONAL, VALID_AMOUNT, VALID_TERM);

        assertEquals(VALID_APPLICANT, application.getApplicant());
        assertEquals(LoanType.PERSONAL, application.getLoanType());
        assertEquals(VALID_AMOUNT, application.getAmount());
        assertEquals(12, application.getTermMonths());
    }


    @ParameterizedTest(name = "{0} with amount {1} is accepted")
    @CsvSource({
            "PERSONAL, 1000",
            "PERSONAL, 1000.01",
            "PERSONAL, 499999.99",
            "PERSONAL, 500000",
            "VEHICLE,  1000",
            "VEHICLE,  1000000"
    })
    @DisplayName("Amount on valid boundaries is accepted")
    void acceptsAmountOnValidBoundaries(LoanType type, BigDecimal amount) {
        assertDoesNotThrow(() -> new LoanApplication(VALID_APPLICANT, type, amount, VALID_TERM));
    }

    @ParameterizedTest(name = "{0} with amount {1} is rejected")
    @CsvSource({
            "PERSONAL, 999.99",
            "PERSONAL, 500000.01",
            "VEHICLE,  999.99",
            "VEHICLE,  1000000.01"
    })
    @DisplayName("Amount outside limits is rejected")
    void rejectsAmountOutsideLimits(LoanType type, BigDecimal amount) {
        assertThrows(IllegalArgumentException.class,
                () -> new LoanApplication(VALID_APPLICANT, type, amount, VALID_TERM));
    }


    @ParameterizedTest(name = "{0} with term {1} months is accepted")
    @CsvSource({
            "PERSONAL, 3",
            "PERSONAL, 36",
            "VEHICLE,  3",
            "VEHICLE,  48"
    })
    @DisplayName("Term on valid boundaries is accepted")
    void acceptsTermOnValidBoundaries(LoanType type, int term) {
        assertDoesNotThrow(() -> new LoanApplication(VALID_APPLICANT, type, VALID_AMOUNT, term));
    }

    @ParameterizedTest(name = "{0} with term {1} months is rejected")
    @CsvSource({
            "PERSONAL, 2",
            "PERSONAL, 37",
            "VEHICLE,  2",
            "VEHICLE,  49"
    })
    @DisplayName("Term outside limits is rejected")
    void rejectsTermOutsideLimits(LoanType type, int term) {
        assertThrows(IllegalArgumentException.class,
                () -> new LoanApplication(VALID_APPLICANT, type, VALID_AMOUNT, term));
    }


    @Test
    @DisplayName("40 months is valid for VEHICLE but invalid for PERSONAL")
    void termOf40MonthsDependsOnLoanType() {
        assertDoesNotThrow(() -> new LoanApplication(VALID_APPLICANT, LoanType.VEHICLE, VALID_AMOUNT, 40));
        assertThrows(IllegalArgumentException.class,
                () -> new LoanApplication(VALID_APPLICANT, LoanType.PERSONAL, VALID_AMOUNT, 40));
    }


    @Test
    @DisplayName("Null applicant is rejected")
    void rejectsNullApplicant() {
        assertThrows(IllegalArgumentException.class,
                () -> new LoanApplication(null, LoanType.PERSONAL, VALID_AMOUNT, VALID_TERM));
    }

    @Test
    @DisplayName("Null loan type is rejected")
    void rejectsNullLoanType() {
        assertThrows(IllegalArgumentException.class,
                () -> new LoanApplication(VALID_APPLICANT, null, VALID_AMOUNT, VALID_TERM));
    }

    @Test
    @DisplayName("Null amount is rejected")
    void rejectsNullAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> new LoanApplication(VALID_APPLICANT, LoanType.PERSONAL, null, VALID_TERM));
    }

    @ParameterizedTest(name = "{0} with amount {1} (sub-cent) is rejected")
    @CsvSource({
            "PERSONAL, 1000.005",
            "VEHICLE,  50000.123"
    })
    @DisplayName("Amount with more than 2 decimals is rejected")
    void rejectsAmountWithMoreThanTwoDecimals(LoanType type, BigDecimal amount) {
        assertThrows(IllegalArgumentException.class,
                () -> new LoanApplication(VALID_APPLICANT, type, amount, VALID_TERM));
    }

}
