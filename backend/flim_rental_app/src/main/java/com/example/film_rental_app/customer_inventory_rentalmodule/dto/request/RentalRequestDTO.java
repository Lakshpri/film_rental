package com.example.film_rental_app.customer_inventory_rentalmodule.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class RentalRequestDTO {

    @NotNull(message = "Rental date is required")
    @PastOrPresent(message = "Rental date cannot be a future date. Please enter today's date or a past date")
    private LocalDateTime rentalDate;

    private LocalDateTime returnDate;

    @NotNull(message = "Inventory ID is required")
    @Positive(message = "Inventory ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer inventoryId;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer customerId;

    @NotNull(message = "Staff ID is required")
    @Positive(message = "Staff ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer staffId;

    public RentalRequestDTO() {}

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
