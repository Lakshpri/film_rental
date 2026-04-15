package com.example.film_rental_app.Location_Store_StaffModule;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;
import com.example.film_rental_app.Location_Store_StaffModule.repository.AddressRepository;
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
class AddressRepositoryTest {

    @Autowired private AddressRepository addressRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private CountryRepository countryRepository;

    private City city;

    @BeforeEach
    void setUp() {
        Country country = new Country();
        country.setCountry("India");
        country = countryRepository.save(country);

        city = new City();
        city.setCity("Delhi");
        city.setCountry(country);
        city = cityRepository.save(city);
    }

    private Address buildValid() {
        Address a = new Address();
        a.setAddress("10 Church St");
        a.setDistrict("NCR");
        a.setPhone("9001234567");
        a.setCity(city);
        return a;
    }


    @Test
    @DisplayName("Save address with valid data should persist")
    void saveAddress_withValidData_shouldPersist() {
        Address saved = addressRepository.saveAndFlush(buildValid());

        assertThat(saved.getAddressId()).isNotNull();
    }


    @Test
    @DisplayName("Blank address line should throw ConstraintViolationException")
    void saveAddress_withBlankAddressLine_shouldThrow() {
        Address a = buildValid();
        a.setAddress("  ");

        assertThatThrownBy(() -> addressRepository.saveAndFlush(a))
                .isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    @DisplayName("Blank district should throw ConstraintViolationException")
    void saveAddress_withBlankDistrict_shouldThrow() {
        Address a = buildValid();
        a.setDistrict("");

        assertThatThrownBy(() -> addressRepository.saveAndFlush(a))
                .isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    @DisplayName("Blank phone should throw ConstraintViolationException")
    void saveAddress_withBlankPhone_shouldThrow() {
        Address a = buildValid();
        a.setPhone(null);

        assertThatThrownBy(() -> addressRepository.saveAndFlush(a))
                .isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    @DisplayName("address2 exceeding 50 chars should throw ConstraintViolationException")
    void saveAddress_withTooLongAddress2_shouldThrow() {
        Address a = buildValid();
        a.setAddress2("X".repeat(51));

        assertThatThrownBy(() -> addressRepository.saveAndFlush(a))
                .isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    @DisplayName("findByCity_CityId should return addresses in that city")
    void findByCityId_shouldReturnMatchingAddresses() {
        addressRepository.save(buildValid());
        addressRepository.save(buildValid());
        addressRepository.flush();

        List<Address> result = addressRepository.findByCity_CityId(city.getCityId());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findByCity_CityId with unknown id should return empty list")
    void findByCityId_withUnknownId_shouldReturnEmpty() {
        List<Address> result = addressRepository.findByCity_CityId(9999);

        assertThat(result).isEmpty();
    }
}
