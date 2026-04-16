package com.example.film_rental_app.Master_DataModule;

import com.example.film_rental_app.Master_DataModule.entity.Country;
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
    @DisplayName("Save country with valid data")
    void saveCountry_withValidData_shouldPersist() {
        Country saved = countryRepository.saveAndFlush(buildValid("USA"));
        assertThat(saved.getCountryId()).isNotNull();
    }

    @Test
    @DisplayName("Blank country name should throw exception")
    void saveCountry_withBlankName_shouldThrow() {
        Country c = buildValid("  ");
        assertThatThrownBy(() -> countryRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Null country name should throw exception")
    void saveCountry_withNullName_shouldThrow() {
        Country c = buildValid(null);
        assertThatThrownBy(() -> countryRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Country name > 50 chars should throw exception")
    void saveCountry_withTooLongName_shouldThrow() {
        Country c = buildValid("C".repeat(51));
        assertThatThrownBy(() -> countryRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
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
}