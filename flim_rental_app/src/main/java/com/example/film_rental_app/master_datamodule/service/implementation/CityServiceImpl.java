package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.exception.CityNotFoundException;
import com.example.film_rental_app.master_datamodule.exception.CountryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import com.example.film_rental_app.master_datamodule.service.CityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public City getCityById(Integer cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException(cityId));
    }

    @Override
    public City createCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public City updateCity(Integer cityId, City updated) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException(cityId));
        city.setCity(updated.getCity());
        if (updated.getCountry() != null) city.setCountry(updated.getCountry());
        return cityRepository.save(city);
    }

    @Override
    public void deleteCity(Integer cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new CityNotFoundException(cityId);
        }
        cityRepository.deleteById(cityId);
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
