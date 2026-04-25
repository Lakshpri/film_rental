package com.example.film_rental_app.location_store_staffmodule.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AddressRequestDTO {

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 50, message = "Address must not exceed 50 characters")
    private String address;

    @Size(max = 50, message = "Address line 2 must not exceed 50 characters")
    private String address2;

    @NotBlank(message = "District is required")
    @Size(max = 20, message = "District must not exceed 20 characters")
    private String district;

    @Size(max = 10, message = "Postal code must not exceed 10 characters")
    @Pattern(regexp = "^[0-9\\-\\s]*$", message = "Postal code must contain numbers only (e.g. 600002)")
    private String postalCode;

    @Size(max = 10, message = "Phone number must not exceed 10 characters")
    @Pattern(regexp = "\\s*\\d{10}\\s*", message = "Phone number must be exactly 10 digits")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @NotNull(message = "City ID is required")
    @Positive(message = "City ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer cityId;

    public AddressRequestDTO() {}

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getCityId() { return cityId; }
    public void setCityId(Integer cityId) { this.cityId = cityId; }
}
