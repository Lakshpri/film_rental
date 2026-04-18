package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.dto.request.CountryRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CityResponseDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CountryResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.mapper.CityMapper;
import com.example.film_rental_app.master_datamodule.mapper.CountryMapper;
import com.example.film_rental_app.master_datamodule.service.CountryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;
    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private CityMapper cityMapper;

    public CountryController(CountryService countryService, CountryMapper countryMapper, CityMapper cityMapper) {
        this.countryService = countryService;
        this.countryMapper = countryMapper;
        this.cityMapper = cityMapper;
    }

    @GetMapping
    public ResponseEntity<List<CountryResponseDTO>> getAllCountries() {
        List<CountryResponseDTO> result = countryService.getAllCountries().stream()
                .map(countryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<CountryResponseDTO> getCountryById(@PathVariable Integer countryId) {
        return ResponseEntity.ok(countryMapper.toResponseDTO(countryService.getCountryById(countryId)));
    }

    @GetMapping("/{countryId}/cities")
    public ResponseEntity<List<CityResponseDTO>> getCitiesByCountry(@PathVariable Integer countryId) {
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
    public ResponseEntity<CountryResponseDTO> updateCountry(@PathVariable Integer countryId,
                                                            @Valid @RequestBody CountryRequestDTO dto) {
        Country existing = countryService.getCountryById(countryId);
        countryMapper.updateEntity(existing, dto);
        return ResponseEntity.ok(countryMapper.toResponseDTO(countryService.updateCountry(countryId, existing)));
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Integer countryId) {
        countryService.deleteCountry(countryId);
        return ResponseEntity.noContent().build();
    }
}
