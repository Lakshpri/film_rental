package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.exception.CountryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import com.example.film_rental_app.master_datamodule.service.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public CountryServiceImpl(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Country getCountryById(Integer countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new CountryNotFoundException(countryId));
    }

    @Override
    public Country createCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(Integer countryId, Country updated) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new CountryNotFoundException(countryId));
        country.setCountry(updated.getCountry());
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Integer countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        countryRepository.deleteById(countryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getCitiesByCountry(Integer countryId) {
        if (!countryRepository.existsById(countryId)) {
            throw new CountryNotFoundException(countryId);
        }
        return cityRepository.findByCountry_CountryId(countryId);
    }
}
