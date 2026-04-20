package com.example.film_rental_app.master_datamodule.dto.response;

import java.time.LocalDateTime;

public class CityResponseDTO {

    private Integer cityId;
    private String city;
    private Integer countryId;
    private String countryName;
    private LocalDateTime lastUpdate;

    public CityResponseDTO() {}

    public CityResponseDTO(Integer cityId, String city, Integer countryId,
                           String countryName, LocalDateTime lastUpdate) {
        this.cityId = cityId;
        this.city = city;
        this.countryId = countryId;
        this.countryName = countryName;
        this.lastUpdate = lastUpdate;
    }

    public Integer getCityId() { return cityId; }
    public void setCityId(Integer cityId) { this.cityId = cityId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Integer getCountryId() { return countryId; }
    public void setCountryId(Integer countryId) { this.countryId = countryId; }

    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
