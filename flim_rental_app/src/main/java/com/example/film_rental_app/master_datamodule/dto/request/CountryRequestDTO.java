package com.example.film_rental_app.master_datamodule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CountryRequestDTO {

    @NotBlank(message = "Country name is required")
    @Size(max = 50, message = "Country name must not exceed 50 characters")
    private String country;

    public CountryRequestDTO() {}

    public CountryRequestDTO(String country) { this.country = country; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
