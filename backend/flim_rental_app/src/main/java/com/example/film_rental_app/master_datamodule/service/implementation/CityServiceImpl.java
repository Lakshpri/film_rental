package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.location_store_staffmodule.repository.AddressRepository;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.exception.CityAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.CityInvalidOperationException;
import com.example.film_rental_app.master_datamodule.exception.CityNotFoundException;
import com.example.film_rental_app.master_datamodule.exception.CountryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import com.example.film_rental_app.master_datamodule.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    @Autowired private CityRepository    cityRepository;
    @Autowired private CountryRepository countryRepository;
    @Autowired private AddressRepository addressRepository;

    @Override
    @Transactional(readOnly = true)
    public List<City> getAllCities() {
        return cityRepository.findAll(Sort.by("cityId"));
    }

    @Override
    @Transactional(readOnly = true)
    public City getCityById(Integer cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException(cityId));
    }

    @Override
    public City createCity(City city) {
        if (city.getCountry() != null && !countryRepository.existsById(city.getCountry().getCountryId())) {
            throw new CountryNotFoundException(city.getCountry().getCountryId());
        }
        if (city.getCountry() != null
                && cityRepository.existsByCityAndCountry_CountryId(city.getCity(), city.getCountry().getCountryId())) {
            throw new CityAlreadyExistsException(city.getCity(), city.getCountry().getCountryId());
        }
        return cityRepository.save(city);
    }

    @Override
    public City updateCity(Integer cityId, City updated) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new CityNotFoundException(cityId));
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
        if (!cityRepository.existsById(cityId)) {
            throw new CityNotFoundException(cityId);
        }
        if (addressRepository.existsByCity_CityId(cityId)) {
            throw new CityInvalidOperationException(cityId,
                    "This city still has addresses linked to it. "
                            + "Please remove all addresses in this city before deleting it.");
        }
        cityRepository.deleteById(cityId);
        return true;
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
