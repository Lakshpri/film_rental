package com.example.film_rental_app.master_datamodule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CityRequestDTO {

    @NotBlank(message = "City name is required")
    @Size(max = 50, message = "City name must not exceed 50 characters")
    @Pattern(regexp = "^[\\p{L}\\s''-]+$", message = "City name must contain letters only (e.g. Chennai, Kuala Lumpur)")
    private String city;

    @NotNull(message = "Country ID is required")
    @Positive(message = "Country ID must be a positive number")
    private Integer countryId;

    public CityRequestDTO() {}
    public CityRequestDTO(String city, Integer countryId) { this.city = city; this.countryId = countryId; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Integer getCountryId() { return countryId; }
    public void setCountryId(Integer countryId) { this.countryId = countryId; }
}
