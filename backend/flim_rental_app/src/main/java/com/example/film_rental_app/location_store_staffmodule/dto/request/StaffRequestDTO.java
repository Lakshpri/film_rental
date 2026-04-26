package com.example.film_rental_app.location_store_staffmodule.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class StaffRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name must not exceed 45 characters")
    @Pattern(regexp = "^[\\p{L}\\s.''-]+$", message = "First name must contain letters only")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name must not exceed 45 characters")
    @Pattern(regexp = "^[\\p{L}\\s.''-]+$", message = "Last name must contain letters only")
    private String lastName;

    @Email(message = "Please enter a valid email address (e.g. user@example.com)")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    private boolean active = true;

    @NotBlank(message = "Username is required")
    @Size(max = 16, message = "Username must not exceed 16 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must contain only letters, numbers, and underscores")
    private String username;

    @Size(min = 6, max = 40, message = "Password must be 6–40 characters")
    private String password;

    private MultipartFile picture;

    @NotNull(message = "Address ID is required")
    @Positive(message = "Address ID must be a positive number")
    private Integer addressId;

    @NotNull(message = "Store ID is required")
    @Positive(message = "Store ID must be a positive number")
    private Integer storeId;

    public StaffRequestDTO() {}
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }
    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
