package com.example.film_rental_app.location_store_staffmodule.repository;

import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByStore_StoreId(Integer storeId);
    boolean existsByAddress_AddressId(Integer addressId);
    boolean existsByStore_StoreId(Integer storeId);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
}
