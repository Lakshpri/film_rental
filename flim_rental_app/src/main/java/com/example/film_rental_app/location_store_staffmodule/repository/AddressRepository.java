package com.example.film_rental_app.location_store_staffmodule.repository;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Integer> {
        List<Address> findByCity_CityId(Integer cityId);
}

