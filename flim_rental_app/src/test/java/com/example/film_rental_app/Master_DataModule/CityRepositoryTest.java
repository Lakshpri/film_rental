package com.example.film_rental_app.Master_DataModule;
import com.example.film_rental_app.Master_DataModule.entity.City;
import com.example.film_rental_app.Master_DataModule.entity.Country;
import com.example.film_rental_app.Master_DataModule.repository.CityRepository;
import com.example.film_rental_app.Master_DataModule.repository.CountryRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CityRepositoryTest {

    @Autowired private CityRepository cityRepository;
    @Autowired private CountryRepository countryRepository;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setCountry("India");
        country = countryRepository.save(country);
    }

    private City buildValid(String name) {
        City c = new City();
        c.setCity(name);
        c.setCountry(country);
        return c;
    }

    @Test
    @DisplayName("Save city with valid data should persist")
    void saveCity_withValidData_shouldPersist() {
        City saved = cityRepository.saveAndFlush(buildValid("Pune"));
        assertThat(saved.getCityId()).isNotNull();
    }

    @Test
    void saveCity_withBlankName_shouldThrow() {
        City c = buildValid("  ");
        assertThatThrownBy(() -> cityRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void saveCity_withNullName_shouldThrow() {
        City c = buildValid(null);
        assertThatThrownBy(() -> cityRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void saveCity_withTooLongName_shouldThrow() {
        City c = buildValid("C".repeat(51));
        assertThatThrownBy(() -> cityRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void testFindCityById() {
        City saved = cityRepository.save(buildValid("Chennai"));
        Optional<City> found = cityRepository.findById(saved.getCityId());
        assertTrue(found.isPresent());
    }

    @Test
    void testFindAllCities() {
        List<City> cities = cityRepository.findAll();
        assertNotNull(cities);
    }

    @Test
    void testUpdateCity() {
        City saved = cityRepository.save(buildValid("Mumbai"));

        saved.setCity("Mumbai Updated");
        City updated = cityRepository.save(saved);

        assertEquals("Mumbai Updated", updated.getCity());
    }

    @Test
    void testDeleteCity() {
        City saved = cityRepository.save(buildValid("Delhi"));

        cityRepository.deleteById(saved.getCityId());

        Optional<City> deleted = cityRepository.findById(saved.getCityId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByCountryId() {
        cityRepository.save(buildValid("Bangalore"));
        cityRepository.save(buildValid("Hyderabad"));

        List<City> cities =
                cityRepository.findByCountry_CountryId(country.getCountryId());

        assertEquals(2, cities.size());
    }
}