package com.example.film_rental_app.Master_DataModule.controller;


import com.example.film_rental_app.Master_DataModule.entity.City;
import com.example.film_rental_app.Master_DataModule.repository.CityRepository;
import com.example.film_rental_app.Master_DataModule.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CityController(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<City> getCityById(@PathVariable Integer cityId) {
        return cityRepository.findById(cityId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        return ResponseEntity.ok(cityRepository.save(city));
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<City> updateCity(@PathVariable Integer cityId, @RequestBody City updated) {
        return cityRepository.findById(cityId).map(city -> {
            city.setCity(updated.getCity());
            if (updated.getCountry() != null) city.setCountry(updated.getCountry());
            return ResponseEntity.ok(cityRepository.save(city));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer cityId) {
        if (!cityRepository.existsById(cityId)) return ResponseEntity.notFound().build();
        cityRepository.deleteById(cityId);
        return ResponseEntity.noContent().build();
    }
}

