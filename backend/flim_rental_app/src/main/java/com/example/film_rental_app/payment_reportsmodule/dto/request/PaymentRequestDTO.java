package com.example.film_rental_app.payment_reportsmodule.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Defines what the client must send when creating a payment
// Validated before reaching the mapper or service
public class PaymentRequestDTO {

    // Required — must be greater than 0.01
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero. Please enter a valid amount (e.g. 4.99)")
    private BigDecimal amount;

    // Required — cannot be a future date
    @NotNull(message = "Payment date is required")
    @PastOrPresent(message = "Payment date cannot be a future date. Please enter today's date or a past date")
    private LocalDateTime paymentDate;

    // Required — links payment to an existing customer
    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer customerId;

    // Required — links payment to the staff member processing it
    @NotNull(message = "Staff ID is required")
    @Positive(message = "Staff ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer staffId;

    // Optional — links payment to a rental if one exists
    // No @NotNull because a payment can exist without a rental
    @Positive(message = "Rental ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer rentalId;

    public PaymentRequestDTO() {}

    // Getters — called by Spring to read values from incoming JSON
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public Integer getCustomerId() { return customerId; }
    public Integer getStaffId() { return staffId; }
    public Integer getRentalId() { return rentalId; }

    // Setters — called by Spring to map incoming JSON fields into this object
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }
}