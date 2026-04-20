package com.example.film_rental_app.master_datamodule;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.exception.CityAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.CityNotFoundException;
import com.example.film_rental_app.master_datamodule.exception.CountryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import com.example.film_rental_app.master_datamodule.service.implementation.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private City city;
    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setCountryId(1);

        city = new City();
        city.setCityId(1);
        city.setCity("Chennai");
        city.setCountry(country);
    }


    //  POSITIVE TEST CASES


    @Test
    void testGetAllCities_Success() {
        when(cityRepository.findAll()).thenReturn(Arrays.asList(city));

        List<City> result = cityService.getAllCities();

        assertEquals(1, result.size());
        verify(cityRepository).findAll();
    }

    @Test
    void testGetCityById_Success() {
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));

        City result = cityService.getCityById(1);

        assertNotNull(result);
        assertEquals("Chennai", result.getCity());
    }

    @Test
    void testCreateCity_Success() {
        when(countryRepository.existsById(1)).thenReturn(true);
        when(cityRepository.existsByCityAndCountry_CountryId("Chennai", 1)).thenReturn(false);
        when(cityRepository.save(city)).thenReturn(city);

        City result = cityService.createCity(city);

        assertNotNull(result);
        verify(cityRepository).save(city);
    }

    @Test
    void testUpdateCity_Success() {
        City updated = new City();
        updated.setCity("Mumbai");
        updated.setCountry(country);

        when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        when(cityRepository.existsByCityAndCountry_CountryId("Mumbai", 1)).thenReturn(false);
        when(cityRepository.save(any(City.class))).thenReturn(city);

        City result = cityService.updateCity(1, updated);

        assertEquals("Mumbai", result.getCity());
    }



//NEGATIVE TEST CASES


    @Test
    void testGetCityById_NotFound() {
        when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class,
                () -> cityService.getCityById(1));
    }

    @Test
    void testCreateCity_CountryNotFound() {
        when(countryRepository.existsById(1)).thenReturn(false);

        assertThrows(CountryNotFoundException.class,
                () -> cityService.createCity(city));
    }

    @Test
    void testCreateCity_Duplicate() {
        when(countryRepository.existsById(1)).thenReturn(true);
        when(cityRepository.existsByCityAndCountry_CountryId("Chennai", 1)).thenReturn(true);

        assertThrows(CityAlreadyExistsException.class,
                () -> cityService.createCity(city));
    }

    @Test
    void testUpdateCity_NotFound() {
        City updated = new City();
        updated.setCity("Mumbai");

        when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class,
                () -> cityService.updateCity(1, updated));
    }

    @Test
    void testDeleteCity_NotFound() {
        when(cityRepository.existsById(1)).thenReturn(false);

        assertThrows(CityNotFoundException.class,
                () -> cityService.deleteCity(1));
    }
}