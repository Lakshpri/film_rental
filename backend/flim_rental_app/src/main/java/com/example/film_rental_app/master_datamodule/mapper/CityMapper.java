package com.example.film_rental_app.master_datamodule.mapper;

import com.example.film_rental_app.master_datamodule.dto.request.CityRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CityResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public City toEntity(CityRequestDTO dto) {
        City city = new City();
        city.setCity(dto.getCity());
        // Country is resolved by ID in the controller/service
        Country country = new Country();
        country.setCountryId(dto.getCountryId());
        city.setCountry(country);
        return city;
    }

    public CityResponseDTO toResponseDTO(City city) {
        CityResponseDTO dto = new CityResponseDTO();
        dto.setCityId(city.getCityId());
        dto.setCity(city.getCity());
        dto.setLastUpdate(city.getLastUpdate());
        if (city.getCountry() != null) {
            dto.setCountryId(city.getCountry().getCountryId());
            dto.setCountryName(city.getCountry().getCountry());
        }
        return dto;
    }

    public void updateEntity(City city, CityRequestDTO dto) {
        city.setCity(dto.getCity());
    }
}
