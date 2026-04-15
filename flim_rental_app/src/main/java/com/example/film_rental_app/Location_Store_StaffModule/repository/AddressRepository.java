package com.example.film_rental_app.Location_Store_StaffModule.repository;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AddressRepository extends JpaRepository<Address, Integer> {
        List<Address> findByCity_CityId(Integer cityId);
}

