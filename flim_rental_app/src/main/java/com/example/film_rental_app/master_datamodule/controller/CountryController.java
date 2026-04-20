package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.dto.request.CountryRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CityResponseDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CountryResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.mapper.CityMapper;
import com.example.film_rental_app.master_datamodule.mapper.CountryMapper;
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
@RequestMapping("/api/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;
    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private CityMapper cityMapper;

    @GetMapping
    public ResponseEntity<List<CountryResponseDTO>> getAllCountries() {
        List<CountryResponseDTO> result = countryService.getAllCountries().stream()
                .map(countryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<CountryResponseDTO> getCountryById(@PathVariable @Positive(message = "Country ID must be a positive number") Integer countryId) {
        return ResponseEntity.ok(countryMapper.toResponseDTO(countryService.getCountryById(countryId)));
    }

    @GetMapping("/{countryId}/cities")
    public ResponseEntity<List<CityResponseDTO>> getCitiesByCountry(@PathVariable @Positive(message = "Country ID must be a positive number") Integer countryId) {
        List<CityResponseDTO> result = countryService.getCitiesByCountry(countryId).stream()
                .map(cityMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CountryResponseDTO> createCountry(@Valid @RequestBody CountryRequestDTO dto) {
        Country country = countryMapper.toEntity(dto);
        return ResponseEntity.status(201).body(countryMapper.toResponseDTO(countryService.createCountry(country)));
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<CountryResponseDTO> updateCountry(@PathVariable @Positive(message = "Country ID must be a positive number") Integer countryId,
                                                            @Valid @RequestBody CountryRequestDTO dto) {
        Country existing = countryService.getCountryById(countryId);
        countryMapper.updateEntity(existing, dto);
        return ResponseEntity.ok(countryMapper.toResponseDTO(countryService.updateCountry(countryId, existing)));
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<Map<String, Object>> deleteCountry(
            @PathVariable @Positive(message = "Country ID must be a positive number") Integer countryId) {

        boolean deleted = countryService.deleteCountry(countryId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);
        response.put("message", "Country deleted successfully");
        response.put("countryId", countryId);

        return ResponseEntity.ok(response);
    }
}
