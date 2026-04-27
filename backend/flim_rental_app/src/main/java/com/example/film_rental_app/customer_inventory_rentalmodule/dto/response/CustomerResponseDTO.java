package com.example.film_rental_app.customer_inventory_rentalmodule.dto.response;

import java.time.LocalDateTime;

// DTO used to send customer data to frontend
public class CustomerResponseDTO {

    // Message for response status
    private String message;

    // Customer ID
    private Integer customerId;

    // Customer basic details
    private String firstName;
    private String lastName;
    private String email;

    // Active status
    private boolean active;

    // Store ID (instead of full Store object)
    private Integer storeId;

    // Address ID (instead of full Address object)
    private Integer addressId;

    // Address line (custom field from Address entity)
    private String addressLine;

    // Creation timestamp
    private LocalDateTime createDate;

    // Last update timestamp
    private LocalDateTime lastUpdate;

    // Default constructor
    public CustomerResponseDTO() {}

    // Getters and Setters

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}