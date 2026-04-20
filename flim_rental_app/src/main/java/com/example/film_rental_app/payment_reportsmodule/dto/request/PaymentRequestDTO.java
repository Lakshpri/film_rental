package com.example.film_rental_app.payment_reportsmodule.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentRequestDTO {

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero. Please enter a valid amount (e.g. 4.99)")
    private BigDecimal amount;

    @NotNull(message = "Payment date is required")
    @PastOrPresent(message = "Payment date cannot be a future date. Please enter today's date or a past date")
    private LocalDateTime paymentDate;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer customerId;

    @NotNull(message = "Staff ID is required")
    @Positive(message = "Staff ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer staffId;

    @Positive(message = "Rental ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer rentalId;

    public PaymentRequestDTO() {}

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }
}
