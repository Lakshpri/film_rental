package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<Country> getCountryById(@PathVariable Integer countryId) {
        return ResponseEntity.ok(countryService.getCountryById(countryId));
    }

    @GetMapping("/{countryId}/cities")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable Integer countryId) {
        return ResponseEntity.ok(countryService.getCitiesByCountry(countryId));
    }

    @PostMapping
    public ResponseEntity<Country> createCountry(@Valid @RequestBody Country country) {
        return ResponseEntity.ok(countryService.createCountry(country));
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<Country> updateCountry(@PathVariable Integer countryId,
                                                 @Valid @RequestBody Country updated) {
        return ResponseEntity.ok(countryService.updateCountry(countryId, updated));
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Integer countryId) {
        countryService.deleteCountry(countryId);
        return ResponseEntity.noContent().build();
    }
}
