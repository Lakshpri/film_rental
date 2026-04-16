package com.example.film_rental_app.Payment_ReportsModule;

import com.example.film_rental_app.Payment_ReportsModule.entity.Payment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRepositoryTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Valid payment should have zero violations
    @Test
    void validPayment_shouldHaveNoViolations() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("9.99"));
        payment.setPaymentDate(LocalDateTime.now());

        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertThat(violations).isEmpty();
    }

    //  Null amount should trigger @NotNull
    @Test
    void nullAmount_shouldFailValidation() {
        Payment payment = new Payment();
        payment.setAmount(null);
        payment.setPaymentDate(LocalDateTime.now());

        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
    }

    // Null paymentDate should trigger @NotNull
    @Test
    void nullPaymentDate_shouldFailValidation() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("5.00"));
        payment.setPaymentDate(null);

        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("paymentDate"));
    }

    //  Negative amount should trigger @DecimalMin("0.00")
    @Test
    void negativeAmount_shouldFailValidation() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("-1.00"));
        payment.setPaymentDate(LocalDateTime.now());

        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
    }

    //  Zero amount should pass since @DecimalMin("0.00") is inclusive
    @Test
    void zeroAmount_shouldPassValidation() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("0.00"));
        payment.setPaymentDate(LocalDateTime.now());

        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertThat(violations).isEmpty();
    }
}