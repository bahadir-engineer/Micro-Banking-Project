package com.example.microbankingproject.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SixteenDigitCardNumberValidator.class)
public @interface SixteenDigitCardNumber {
    String message() default "Kart nömrəsi düz 16 rəqəmdən ibarət olmalıdır";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}