package com.example.film_rental_app.Master_DataModule.service;

import com.example.film_rental_app.Master_DataModule.entity.City;

import java.util.List;

public interface CityService {

    List<City> getAllCities();

    City getCityById(Integer cityId);

    City createCity(City city);

    City updateCity(Integer cityId, City updated);

    void deleteCity(Integer cityId);

    List<City> getCitiesByCountry(Integer countryId);
}