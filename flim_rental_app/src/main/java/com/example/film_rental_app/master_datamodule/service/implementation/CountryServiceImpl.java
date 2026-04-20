package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.exception.CountryAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.CountryInvalidOperationException;
import com.example.film_rental_app.master_datamodule.exception.CountryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import com.example.film_rental_app.master_datamodule.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Country getCountryById(Integer countryId) {
        // ResourceNotFoundException → HTTP 404
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new CountryNotFoundException(countryId));
    }

    @Override
    public Country createCountry(Country country) {
        // DuplicateResourceException → HTTP 409
        if (countryRepository.existsByCountry(country.getCountry())) {
            throw new CountryAlreadyExistsException(country.getCountry());
        }
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(Integer countryId, Country updated) {
        // ResourceNotFoundException → HTTP 404
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new CountryNotFoundException(countryId));
        // DuplicateResourceException → HTTP 409
        if (!country.getCountry().equalsIgnoreCase(updated.getCountry())
                && countryRepository.existsByCountry(updated.getCountry())) {
            throw new CountryAlreadyExistsException(updated.getCountry());
        }
        country.setCountry(updated.getCountry());
        return countryRepository.save(country);
    }

    @Override
    public boolean deleteCountry(Integer countryId) {
        // ResourceNotFoundException → HTTP 404
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        // InvalidOperationException → HTTP 400
        if (cityRepository.existsByCountry_CountryId(countryId)) {
            throw new CountryInvalidOperationException(countryId,
                    "This country still has cities under it. Please delete all cities in this country first, then try deleting the country.");
        }
        countryRepository.deleteById(countryId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getCitiesByCountry(Integer countryId) {
        // ResourceNotFoundException → HTTP 404
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        return cityRepository.findByCountry_CountryId(countryId);
    }
}
