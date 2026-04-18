package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.master_datamodule.dto.request.CityRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CityResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.mapper.CityMapper;
import com.example.film_rental_app.master_datamodule.service.CityService;
import com.example.film_rental_app.master_datamodule.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    private CityService cityService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private AddressService addressService;


    @GetMapping
    public ResponseEntity<List<CityResponseDTO>> getAllCities() {
        List<CityResponseDTO> result = cityService.getAllCities().stream()
                .map(cityMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityResponseDTO> getCityById(@PathVariable Integer cityId) {
        return ResponseEntity.ok(cityMapper.toResponseDTO(cityService.getCityById(cityId)));
    }

    @PostMapping
    public ResponseEntity<CityResponseDTO> createCity(@Valid @RequestBody CityRequestDTO dto) {
        City city = cityMapper.toEntity(dto);
        Country country = countryService.getCountryById(dto.getCountryId());
        city.setCountry(country);
        return ResponseEntity.status(201).body(cityMapper.toResponseDTO(cityService.createCity(city)));
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<CityResponseDTO> updateCity(@PathVariable Integer cityId,
                                                      @Valid @RequestBody CityRequestDTO dto) {
        City existing = cityService.getCityById(cityId);
        existing.setCity(dto.getCity());
        if (dto.getCountryId() != null) {
            Country country = countryService.getCountryById(dto.getCountryId());
            existing.setCountry(country);
        }
        return ResponseEntity.ok(cityMapper.toResponseDTO(cityService.updateCity(cityId, existing)));
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer cityId) {
        cityService.deleteCity(cityId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{cityId}/addresses")
    public ResponseEntity<List<Address>> getAddressesByCity(@PathVariable Integer cityId) {
        List<Address> addresses = addressService.getAddressesByCity(cityId);
        return ResponseEntity.ok(addresses);
    }
}
