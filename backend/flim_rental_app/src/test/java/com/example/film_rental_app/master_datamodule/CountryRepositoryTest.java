package com.example.film_rental_app.master_datamodule;

import com.example.film_rental_app.master_datamodule.entity.Country;
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
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setCountry("India");
        country = countryRepository.save(country);
    }

    private Country buildValid(String name) {
        Country c = new Country();
        c.setCountry(name);
        return c;
    }


    @Test
    void testFindById() {
        Optional<Country> found = countryRepository.findById(country.getCountryId());
        assertTrue(found.isPresent());
    }

    @Test
    void testFindAll() {
        List<Country> list = countryRepository.findAll();
        assertNotNull(list);
    }

    @Test
    void testUpdateCountry() {
        country.setCountry("India Updated");
        Country updated = countryRepository.save(country);

        assertEquals("India Updated", updated.getCountry());
    }

    @Test
    void testDeleteById() {
        countryRepository.deleteById(country.getCountryId());

        Optional<Country> deleted = countryRepository.findById(country.getCountryId());
        assertFalse(deleted.isPresent());
    }

    // Tests the custom @Query
    @Test
    @DisplayName("Custom @Query: findCountriesWithNameLongerThan returns only matching countries")
    void testFindCountriesWithNameLongerThan_ReturnsLongNames() {
        countryRepository.save(buildValid("US"));
        countryRepository.save(buildValid("Germany"));
        countryRepository.save(buildValid("Switzerland"));

        List<Country> result = countryRepository.findCountriesWithNameLongerThan(5);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        assertTrue(
                result.stream().allMatch(c -> c.getCountry().length() > 5),
                "All returned countries should have name length > 5"
        );
        System.out.println("Countries with name > 5 chars: " + result.stream().map(Country::getCountry).toList());
    }
}