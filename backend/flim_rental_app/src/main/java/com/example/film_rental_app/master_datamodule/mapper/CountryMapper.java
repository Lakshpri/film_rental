package com.example.film_rental_app.master_datamodule.mapper;

import com.example.film_rental_app.master_datamodule.dto.request.CountryRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CountryResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    public Country toEntity(CountryRequestDTO dto) {
        Country country = new Country();
        country.setCountry(dto.getCountry());
        return country;
    }

    public CountryResponseDTO toResponseDTO(Country country) {
        return new CountryResponseDTO(
                country.getCountryId(),
                country.getCountry(),
                country.getLastUpdate()
        );
    }

    public void updateEntity(Country country, CountryRequestDTO dto) {
        country.setCountry(dto.getCountry());
    }
}
