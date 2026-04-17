package com.example.film_rental_app.customer_inventory_rentalmodule.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RentalRequestDTO {

    @NotNull(message = "Rental date is required")
    private LocalDateTime rentalDate;

    private LocalDateTime returnDate;

    @NotNull(message = "Inventory ID is required")
    private Integer inventoryId;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Staff ID is required")
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
