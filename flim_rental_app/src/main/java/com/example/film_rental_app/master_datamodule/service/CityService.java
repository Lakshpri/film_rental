package com.example.film_rental_app.master_datamodule.service;

import com.example.film_rental_app.master_datamodule.entity.City;

import java.util.List;

public interface CityService {

    List<City> getAllCities();

    City getCityById(Integer cityId);

    City createCity(City city);

    City updateCity(Integer cityId, City updated);

    void deleteCity(Integer cityId);

    List<City> getCitiesByCountry(Integer countryId);
}