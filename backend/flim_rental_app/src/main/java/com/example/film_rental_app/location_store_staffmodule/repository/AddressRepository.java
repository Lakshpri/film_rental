package com.example.film_rental_app.location_store_staffmodule.repository;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
        List<Address> findByCity_CityId(Integer cityId);
        boolean existsByAddress(String address);
        boolean existsByCity_CityId(Integer cityId);
        boolean existsByAddressAndAddressIdNot(String address, Integer addressId);
}
