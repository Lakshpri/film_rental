package com.example.film_rental_app.customer_inventory_rentalmodule.dto.response;

import java.time.LocalDateTime;

// DTO used to send rental details to frontend
public class RentalResponseDTO {

    // Response message
    private String message;

    // Rental details
    private Integer rentalId;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;

    // Inventory details
    private Integer inventoryId;

    // Film details (from Inventory → Film)
    private Integer filmId;
    private String filmTitle;

    // Customer details
    private Integer customerId;
    private String customerName;

    // Staff details
    private Integer staffId;
    private String staffName;

    // Last update timestamp
    private LocalDateTime lastUpdate;

    // Default constructor
    public RentalResponseDTO() {}

    // Getters and Setters

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }

    public LocalDateTime getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDateTime rentalDate) { this.rentalDate = rentalDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public String getFilmTitle() { return filmTitle; }
    public void setFilmTitle(String filmTitle) { this.filmTitle = filmTitle; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}