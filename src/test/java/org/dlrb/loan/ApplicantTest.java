package org.dlrb.loan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicantTest {
    private static final BigDecimal VALID_INCOME = new BigDecimal("20000");
    private static final int VALID_AGE = 30;
    private static final int VALID_SCORE = 1500;

    @Test
    @DisplayName("Valid data creates an applicant")
    void createsApplicantWithValidData() {
        Applicant applicant = new Applicant(VALID_AGE, VALID_INCOME, VALID_SCORE, false);

        assertEquals(30, applicant.getAge());
        assertEquals(VALID_INCOME, applicant.getMonthlyIncome());
        assertEquals(1500, applicant.getCreditScore());
        assertFalse(applicant.hasOverdueDebt());
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 119, 120})
    @DisplayName("Age on valid boundaries is accepted")
    void acceptsAgeOnValidBoundaries(int age) {
        assertDoesNotThrow(() -> new Applicant(age, VALID_INCOME, VALID_SCORE, false));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 121})
    @DisplayName("Age outside valid range is rejected")
    void rejectsAgeOutsideValidRange(int age) {
        assertThrows(IllegalArgumentException.class,
                () -> new Applicant(age, VALID_INCOME, VALID_SCORE, false));
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1899, 1900})
    @DisplayName("Credit score on valid boundaries is accepted")
    void acceptsCreditScoreOnValidBoundaries(int score) {
        assertDoesNotThrow(() -> new Applicant(VALID_AGE, VALID_INCOME, score, false));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1901})
    @DisplayName("Credit score outside valid range is rejected")
    void rejectsCreditScoreOutsideValidRange(int score) {
        assertThrows(IllegalArgumentException.class,
                () -> new Applicant(VALID_AGE, VALID_INCOME, score, false));
    }


    @ParameterizedTest
    @ValueSource(strings = {"0", "-0.01", "-5000"})
    @DisplayName("Zero or negative income is rejected")
    void rejectsZeroOrNegativeIncome(String income) {
        assertThrows(IllegalArgumentException.class,
                () -> new Applicant(VALID_AGE, new BigDecimal(income), VALID_SCORE, false));
    }

    @Test
    @DisplayName("Null income is rejected")
    void rejectsNullIncome() {
        assertThrows(IllegalArgumentException.class,
                () -> new Applicant(VALID_AGE, null, VALID_SCORE, false));
    }

    @Test
    @DisplayName("Smallest positive income is accepted")
    void acceptsSmallestPositiveIncome() {
        assertDoesNotThrow(() -> new Applicant(VALID_AGE, new BigDecimal("0.01"), VALID_SCORE, false));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.001", "15000.555"})
    @DisplayName("Income with more than 2 decimals is rejected")
    void rejectsIncomeWithMoreThanTwoDecimals(String income) {
        assertThrows(IllegalArgumentException.class,
                () -> new Applicant(VALID_AGE, new BigDecimal(income), VALID_SCORE, false));
    }
}
