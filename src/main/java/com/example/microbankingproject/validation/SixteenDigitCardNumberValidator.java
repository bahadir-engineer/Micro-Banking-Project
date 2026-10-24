package com.example.microbankingproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SixteenDigitCardNumberValidator implements ConstraintValidator<SixteenDigitCardNumber, Long> {

    private static final long MIN_16_DIGIT = 1_000_000_000_000_000L;
    private static final long MAX_16_DIGIT = 9_999_999_999_999_999L;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= MIN_16_DIGIT && value <= MAX_16_DIGIT;
    }
}
