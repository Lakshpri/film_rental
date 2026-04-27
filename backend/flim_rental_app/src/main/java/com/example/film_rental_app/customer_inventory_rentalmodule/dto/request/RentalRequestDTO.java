package com.example.film_rental_app.customer_inventory_rentalmodule.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

// DTO used to receive rental input from frontend
public class RentalRequestDTO {

    // Rental date must not be null and cannot be future
    @NotNull(message = "Rental date is required")
    @PastOrPresent(message = "Rental date cannot be a future date")
    private LocalDateTime rentalDate;

    // Return date (optional, may be null if not returned)
    private LocalDateTime returnDate;

    // Inventory ID must not be null and must be positive
    @NotNull(message = "Inventory ID is required")
    @Positive(message = "Inventory ID must be a number greater than zero")
    private Integer inventoryId;

    // Customer ID must not be null and must be positive
    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a number greater than zero")
    private Integer customerId;

    // Staff ID must not be null and must be positive
    @NotNull(message = "Staff ID is required")
    @Positive(message = "Staff ID must be a number greater than zero")
    private Integer staffId;

    // Default constructor
    public RentalRequestDTO() {}

    // Getters and Setters

    public LocalDateTime getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDateTime rentalDate) { this.rentalDate = rentalDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }
}