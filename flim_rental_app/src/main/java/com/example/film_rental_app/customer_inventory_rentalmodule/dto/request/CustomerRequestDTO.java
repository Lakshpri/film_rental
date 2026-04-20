package com.example.film_rental_app.customer_inventory_rentalmodule.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CustomerRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name must not exceed 45 characters")
    @Pattern(regexp = "^[\\p{L}\\s.''-]+$", message = "First name must contain letters only (e.g. Arjun, Kavitha)")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name must not exceed 45 characters")
    @Pattern(regexp = "^[\\p{L}\\s.''-]+$", message = "Last name must contain letters only (e.g. Murugan, Rajan)")
    private String lastName;

    @Email(message = "Please enter a valid email address (e.g. user@example.com)")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    private boolean active = true;

    @NotNull(message = "Store ID is required")
    @Positive(message = "Store ID must be a positive number")
    private Integer storeId;

    @NotNull(message = "Address ID is required")
    @Positive(message = "Address ID must be a positive number")
    private Integer addressId;

    public CustomerRequestDTO() {}
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
}
