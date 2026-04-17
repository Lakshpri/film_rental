package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<City> getCityById(@PathVariable Integer cityId) {
        return ResponseEntity.ok(cityService.getCityById(cityId));
    }

    @PostMapping
    public ResponseEntity<City> createCity(@Valid @RequestBody City city) {
        return ResponseEntity.ok(cityService.createCity(city));
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<City> updateCity(@PathVariable Integer cityId,
                                           @Valid @RequestBody City updated) {
        return ResponseEntity.ok(cityService.updateCity(cityId, updated));
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer cityId) {
        cityService.deleteCity(cityId);
        return ResponseEntity.noContent().build();
    }
}