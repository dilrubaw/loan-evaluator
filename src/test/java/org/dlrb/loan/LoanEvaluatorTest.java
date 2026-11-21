package org.dlrb.loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class LoanEvaluatorTest {

    private static final BigDecimal LOAN_AMOUNT = new BigDecimal("10000");
    private static final int LOAN_TERM = 12;

    private LoanEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new LoanEvaluator(new InstallmentCalculator());
    }

    private Decision evaluate(int age, String income, int score, boolean hasOverdueDebt) {
        Applicant applicant = new Applicant(age, new BigDecimal(income), score, hasOverdueDebt);
        LoanApplication application =
                new LoanApplication(applicant, LoanType.PERSONAL, LOAN_AMOUNT, LOAN_TERM);
        return evaluator.evaluate(application);
    }

    @ParameterizedTest(name = "{0}: age={1}, income={2}, score={3}, overdue={4} -> {5}")
    @CsvSource({
            "DT1 (too young),         17, 20000, 1500, false, REJECTED",
            "DT1 (too old),           66, 20000, 1500, false, REJECTED",
            "DT2 (overdue debt),      30, 20000, 1500, true,  REJECTED",
            "DT3 (low score),         30, 20000,  500, false, REJECTED",
            "'DT4 (mid score, high installment)',  30, 2000,  900,  false, REJECTED",
            "'DT5 (high score, high installment)', 30, 2000,  1500, false, REJECTED",
            "'DT6 (mid score, ok installment)',    30, 20000, 900,  false, APPROVED_WITH_GUARANTOR",
            "'DT7 (high score, ok installment)',   30, 20000, 1500, false, APPROVED"
    })
    @DisplayName("Decision table rules")
    void followsDecisionTable(String rule, int age, String income, int score,
                              boolean hasOverdueDebt, Decision expected) {
        assertEquals(expected, evaluate(age, income, score, hasOverdueDebt));
    }


    @ParameterizedTest(name = "age {0} -> {1}")
    @CsvSource({
            "17, REJECTED",
            "18, APPROVED",
            "65, APPROVED",
            "66, REJECTED"
    })
    @DisplayName("Age boundaries")
    void checksAgeBoundaries(int age, Decision expected) {
        assertEquals(expected, evaluate(age, "20000", 1500, false));
    }


    @ParameterizedTest(name = "score {0} -> {1}")
    @CsvSource({
            "699,  REJECTED",
            "700,  APPROVED_WITH_GUARANTOR",
            "1199, APPROVED_WITH_GUARANTOR",
            "1200, APPROVED"
    })
    @DisplayName("Credit score boundaries")
    void checksCreditScoreBoundaries(int score, Decision expected) {
        assertEquals(expected, evaluate(30, "20000", score, false));
    }


    @ParameterizedTest(name = "income {0} (limit = income x 0.50) -> {1}")
    @CsvSource({
            "2009.24, APPROVED",
            "2009.23, REJECTED"
    })
    @DisplayName("Debt ratio boundary")
    void checksDebtRatioBoundary(String income, Decision expected) {
        assertEquals(expected, evaluate(30, income, 1500, false));
    }


    @Test
    @DisplayName("Null application is rejected")
    void rejectsNullApplication() {
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluate(null));
    }

    @Test
    @DisplayName("Null calculator is rejected")
    void rejectsNullCalculator() {
        assertThrows(IllegalArgumentException.class, () -> new LoanEvaluator(null));
    }
}
