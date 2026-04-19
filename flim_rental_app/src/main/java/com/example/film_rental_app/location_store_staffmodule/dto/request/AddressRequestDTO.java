package com.example.film_rental_app.location_store_staffmodule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddressRequestDTO {

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 50, message = "Address must not exceed 50 characters")
    private String address;

    @Size(max = 50, message = "Address2 must not exceed 50 characters")
    private String address2;

    @NotBlank(message = "District is required")
    @Size(max = 20, message = "District must not exceed 20 characters")
    private String district;

    @Size(max = 10, message = "Postal code must not exceed 10 characters")
    private String postalCode;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotNull(message = "City ID is required")
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
