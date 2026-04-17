package com.example.film_rental_app.master_datamodule.dto.response;

import java.time.LocalDateTime;

public class CountryResponseDTO {

    private Integer countryId;
    private String country;
    private LocalDateTime lastUpdate;

    public CountryResponseDTO() {}

    public CountryResponseDTO(Integer countryId, String country, LocalDateTime lastUpdate) {
        this.countryId = countryId;
        this.country = country;
        this.lastUpdate = lastUpdate;
    }

    public Integer getCountryId() { return countryId; }
    public void setCountryId(Integer countryId) { this.countryId = countryId; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
