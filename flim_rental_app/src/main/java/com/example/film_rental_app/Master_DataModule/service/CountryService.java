package com.example.film_rental_app.Master_DataModule.service;

import com.example.film_rental_app.Master_DataModule.entity.City;
import com.example.film_rental_app.Master_DataModule.entity.Country;

import java.util.List;

public interface CountryService {

    List<Country> getAllCountries();

    Country getCountryById(Integer countryId);

    Country createCountry(Country country);

    Country updateCountry(Integer countryId, Country updated);

    void deleteCountry(Integer countryId);

    List<City> getCitiesByCountry(Integer countryId);
}
