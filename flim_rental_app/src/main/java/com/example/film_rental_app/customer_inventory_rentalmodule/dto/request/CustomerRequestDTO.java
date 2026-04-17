package com.example.film_rental_app.customer_inventory_rentalmodule.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CustomerRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 45)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45)
    private String lastName;

    @Email(message = "Invalid email format")
    @Size(max = 50)
    private String email;

    private boolean active = true;

    @NotNull(message = "Store ID is required")
    private Integer storeId;

    @NotNull(message = "Address ID is required")
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
