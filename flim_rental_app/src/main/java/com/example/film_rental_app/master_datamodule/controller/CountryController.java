package com.example.film_rental_app.master_datamodule.controller;


import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public CountryController(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<Country> getCountryById(@PathVariable Integer countryId) {
        return countryRepository.findById(countryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        return countryRepository.save(country);
    }

    @PutMapping("/{countryId}")
    public ResponseEntity<Country> updateCountry(@PathVariable Integer countryId, @RequestBody Country updated) {
        return countryRepository.findById(countryId).map(country -> {
            country.setCountry(updated.getCountry());
            return ResponseEntity.ok(countryRepository.save(country));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{countryId}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Integer countryId) {
        if (!countryRepository.existsById(countryId)) return ResponseEntity.notFound().build();
        countryRepository.deleteById(countryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{countryId}/cities")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable Integer countryId) {
        if (!countryRepository.existsById(countryId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(cityRepository.findByCountry_CountryId(countryId));
    }
}
