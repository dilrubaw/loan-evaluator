package org.dlrb.loan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InstallmentCalculatorTest {
    private static final Applicant VALID_APPLICANT =
            new Applicant(30, new BigDecimal("20000"), 1500, false);

    private final InstallmentCalculator calculator = new InstallmentCalculator();

    private LoanApplication createApplication(LoanType type, String amount, int term) {
        return new LoanApplication(VALID_APPLICANT, type, new BigDecimal(amount), term);
    }

    @ParameterizedTest(name = "{0}: {1} TL, {2} months -> {3} TL")
    @CsvSource({
            "PERSONAL, 10000,   12, 1004.62",
            "PERSONAL, 100000,  36, 4580.38",
            "PERSONAL, 1000,     3, 353.53",
            "PERSONAL, 500000,  36, 22901.90",
            "VEHICLE,  50000,   24, 2795.64",
            "VEHICLE,  200000,  48, 7201.20",
            "VEHICLE,  1000000, 48, 36005.99"
    })
    @DisplayName("Monthly installment matches expected value")
    void calculatesMonthlyInstallment(LoanType type, String amount, int term, BigDecimal expected) {
        BigDecimal actual = calculator.calculateMonthlyInstallment(createApplication(type, amount, term));
        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "{0}: {1} TL, {2} months -> total {3} TL")
    @CsvSource({
            "PERSONAL, 10000,   12, 12055.44",
            "VEHICLE,  1000000, 48, 1728287.52"
    })
    @DisplayName("Total repayment equals installment times term")
    void calculatesTotalRepayment(LoanType type, String amount, int term, BigDecimal expected) {
        BigDecimal actual = calculator.calculateTotalRepayment(createApplication(type, amount, term));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Result always has exactly 2 decimal places")
    void resultHasTwoDecimalPlaces() {
        BigDecimal actual = calculator.calculateMonthlyInstallment(
                createApplication(LoanType.PERSONAL, "10000", 12));
        assertEquals(2, actual.scale());
    }

    @Test
    @DisplayName("Null application is rejected")
    void rejectsNullApplication() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateMonthlyInstallment(null));
    }
}

