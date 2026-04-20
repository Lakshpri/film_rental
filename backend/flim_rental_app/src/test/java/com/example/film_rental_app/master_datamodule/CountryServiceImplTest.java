package com.example.film_rental_app.master_datamodule;

import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.exception.CountryAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.CountryInvalidOperationException;
import com.example.film_rental_app.master_datamodule.exception.CountryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import com.example.film_rental_app.master_datamodule.service.implementation.CountryServiceImpl;
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
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setCountryId(1);
        country.setCountry("India");
    }


    // POSITIVE TEST CASES


    @Test
    void testGetAllCountries_Success() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(country));

        List<Country> result = countryService.getAllCountries();

        assertEquals(1, result.size());
        verify(countryRepository).findAll();
    }

    @Test
    void testGetCountryById_Success() {
        when(countryRepository.findById(1)).thenReturn(Optional.of(country));

        Country result = countryService.getCountryById(1);

        assertNotNull(result);
        assertEquals("India", result.getCountry());
    }

    @Test
    void testCreateCountry_Success() {
        when(countryRepository.existsByCountry("India")).thenReturn(false);
        when(countryRepository.save(country)).thenReturn(country);

        Country result = countryService.createCountry(country);

        assertNotNull(result);
        verify(countryRepository).save(country);
    }

    @Test
    void testUpdateCountry_Success() {
        Country updated = new Country();
        updated.setCountry("USA");

        when(countryRepository.findById(1)).thenReturn(Optional.of(country));
        when(countryRepository.existsByCountry("USA")).thenReturn(false);
        when(countryRepository.save(any(Country.class))).thenReturn(country);

        Country result = countryService.updateCountry(1, updated);

        assertEquals("USA", result.getCountry());
    }

    @Test
    void testDeleteCountry_Success() {
        when(countryRepository.existsById(1)).thenReturn(true);
        when(cityRepository.existsByCountry_CountryId(1)).thenReturn(false);

        boolean result = countryService.deleteCountry(1);

        assertTrue(result);
        verify(countryRepository).deleteById(1);
    }

    //  NEGATIVE TEST CASES


    @Test
    void testGetCountryById_NotFound() {
        when(countryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class,
                () -> countryService.getCountryById(1));
    }

    @Test
    void testCreateCountry_Duplicate() {
        when(countryRepository.existsByCountry("India")).thenReturn(true);

        assertThrows(CountryAlreadyExistsException.class,
                () -> countryService.createCountry(country));
    }

    @Test
    void testUpdateCountry_NotFound() {
        Country updated = new Country();
        updated.setCountry("USA");

        when(countryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class,
                () -> countryService.updateCountry(1, updated));
    }

    @Test
    void testUpdateCountry_DuplicateName() {
        Country updated = new Country();
        updated.setCountry("USA");

        when(countryRepository.findById(1)).thenReturn(Optional.of(country));
        when(countryRepository.existsByCountry("USA")).thenReturn(true);

        assertThrows(CountryAlreadyExistsException.class,
                () -> countryService.updateCountry(1, updated));
    }

    @Test
    void testDeleteCountry_WithExistingCities() {
        when(countryRepository.existsById(1)).thenReturn(true);
        when(cityRepository.existsByCountry_CountryId(1)).thenReturn(true);

        assertThrows(CountryInvalidOperationException.class,
                () -> countryService.deleteCountry(1));
    }
}