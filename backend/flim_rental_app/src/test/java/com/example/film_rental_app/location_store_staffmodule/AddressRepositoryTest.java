package com.example.film_rental_app.location_store_staffmodule;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.repository.AddressRepository;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    private City city;

    @BeforeEach
    void setup() {
        Country country = new Country();
        country.setCountry("India");
        country = countryRepository.save(country);

        city = new City();
        city.setCity("Chennai");
        city.setCountry(country);
        city = cityRepository.save(city);
    }

    private Address getAddress() {
        Address a = new Address();
        a.setAddress("Street 1");
        a.setDistrict("TN");
        a.setPhone("1234567890");
        a.setCity(city);
        return a;
    }

    //  CREATE
    @Test
    void testSave() {
        Address a = addressRepository.save(getAddress());
        assertThat(a.getAddressId()).isNotNull();
    }

    //  READ
    @Test
    void testFindById() {
        Address a = addressRepository.save(getAddress());

        Address result = addressRepository.findById(a.getAddressId()).orElse(null);

        assertThat(result).isNotNull();
    }

    //  UPDATE
    @Test
    void testUpdate() {
        Address a = addressRepository.save(getAddress());

        a.setDistrict("Updated");
        addressRepository.save(a);

        Address updated = addressRepository.findById(a.getAddressId()).get();
        assertThat(updated.getDistrict()).isEqualTo("Updated");
    }

    //  DELETE
    @Test
    void testDelete() {
        Address a = addressRepository.save(getAddress());

        addressRepository.deleteById(a.getAddressId());

        assertThat(addressRepository.findById(a.getAddressId())).isEmpty();
    }
}
