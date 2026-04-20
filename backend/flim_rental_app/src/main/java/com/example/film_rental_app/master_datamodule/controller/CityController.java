package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.location_store_staffmodule.dto.response.AddressResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.mapper.AddressMapper;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.master_datamodule.dto.request.CityRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CityResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.mapper.CityMapper;
import com.example.film_rental_app.master_datamodule.service.CityService;
import com.example.film_rental_app.master_datamodule.service.CountryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/cities")
public class CityController {

    @Autowired private CityService cityService;
    @Autowired private CountryService countryService;
    @Autowired private CityMapper cityMapper;
    @Autowired private AddressService addressService;
    @Autowired private AddressMapper addressMapper;

    @GetMapping
    public ResponseEntity<List<CityResponseDTO>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities().stream()
                .map(cityMapper::toResponseDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityResponseDTO> getCityById(
            @PathVariable @Positive(message = "City ID must be a positive number") Integer cityId) {
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
    public ResponseEntity<CityResponseDTO> updateCity(
            @PathVariable @Positive(message = "City ID must be a positive number") Integer cityId,
            @Valid @RequestBody CityRequestDTO dto) {
        City existing = cityService.getCityById(cityId);
        existing.setCity(dto.getCity());
        if (dto.getCountryId() != null) {
            existing.setCountry(countryService.getCountryById(dto.getCountryId()));
        }
        return ResponseEntity.ok(cityMapper.toResponseDTO(cityService.updateCity(cityId, existing)));
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<Map<String, Object>> deleteCity(
            @PathVariable @Positive(message = "City ID must be a positive number") Integer cityId) {

        boolean deleted = cityService.deleteCity(cityId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);
        response.put("message", "City deleted successfully");
        response.put("cityId", cityId);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{cityId}/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getAddressesByCity(
            @PathVariable @Positive(message = "City ID must be a positive number") Integer cityId) {
        return ResponseEntity.ok(addressService.getAddressesByCity(cityId).stream()
                .map(addressMapper::toResponseDTO).collect(Collectors.toList()));
    }
}
