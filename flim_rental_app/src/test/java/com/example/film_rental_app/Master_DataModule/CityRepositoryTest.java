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
    @DisplayName("Blank city name should throw ConstraintViolationException")
    void saveCity_withBlankName_shouldThrow() {
        City c = buildValid("  ");
        assertThatThrownBy(() -> cityRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Null city name should throw ConstraintViolationException")
    void saveCity_withNullName_shouldThrow() {
        City c = buildValid(null);
        assertThatThrownBy(() -> cityRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("City name exceeding 50 chars should throw ConstraintViolationException")
    void saveCity_withTooLongName_shouldThrow() {
        City c = buildValid("C".repeat(51));
        assertThatThrownBy(() -> cityRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("findByCountry_CountryId should return cities for that country")
    void findByCountryId_shouldReturnMatchingCities() {
        cityRepository.save(buildValid("Mumbai"));
        cityRepository.save(buildValid("Kolkata"));
        cityRepository.flush();

        List<City> result = cityRepository.findByCountry_CountryId(country.getCountryId());
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findByCountry_CountryId with unknown id returns empty list")
    void findByCountryId_withUnknownId_shouldReturnEmpty() {
        List<City> result = cityRepository.findByCountry_CountryId(9999);
        assertThat(result).isEmpty();
    }
}
