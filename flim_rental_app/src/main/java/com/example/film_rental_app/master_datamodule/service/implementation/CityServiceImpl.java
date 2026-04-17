package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.exception.CityAlreadyExistsException;
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

    private final CityRepository    cityRepository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository    = cityRepository;
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
        // ResourceNotFoundException → HTTP 404
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException(cityId));
    }

    @Override
    public City createCity(City city) {
        // ResourceNotFoundException → HTTP 404 (parent country must exist)
        if (city.getCountry() != null && !countryRepository.existsById(city.getCountry().getCountryId())) {
            throw new CountryNotFoundException(city.getCountry().getCountryId());
        }
        // DuplicateResourceException → HTTP 409
        if (city.getCountry() != null
                && cityRepository.existsByCityAndCountry_CountryId(city.getCity(), city.getCountry().getCountryId())) {
            throw new CityAlreadyExistsException(city.getCity(), city.getCountry().getCountryId());
        }
        return cityRepository.save(city);
    }

    @Override
    public City updateCity(Integer cityId, City updated) {
        // ResourceNotFoundException → HTTP 404
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException(cityId));
        // DuplicateResourceException → HTTP 409
        Integer countryId = updated.getCountry() != null ? updated.getCountry().getCountryId()
                : (city.getCountry() != null ? city.getCountry().getCountryId() : null);
        if (countryId != null && !city.getCity().equalsIgnoreCase(updated.getCity())
                && cityRepository.existsByCityAndCountry_CountryId(updated.getCity(), countryId)) {
            throw new CityAlreadyExistsException(updated.getCity(), countryId);
        }
        city.setCity(updated.getCity());
        if (updated.getCountry() != null) city.setCountry(updated.getCountry());
        return cityRepository.save(city);
    }

    @Override
    public boolean deleteCity(Integer cityId) {
        // ResourceNotFoundException → HTTP 404
        if (!cityRepository.existsById(cityId)) {
            throw new CityNotFoundException(cityId);
        }
        // InvalidOperationException → HTTP 400
        // (guard: if addresses reference this city, deletion should be blocked)
        cityRepository.deleteById(cityId);
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
