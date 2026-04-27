package com.example.film_rental_app.master_datamodule;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
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

    // Custom @Query:
    @Test
    void testFindCitiesByCountrySorted() {
        cityRepository.save(buildValid("Mumbai"));
        cityRepository.save(buildValid("Ahmedabad"));

        List<City> result = cityRepository.findCitiesByCountrySorted(country.getCountryId());

        assertNotNull(result);
        assertEquals(2, result.size());
        System.out.println("Sorted cities: " + result);
    }
}