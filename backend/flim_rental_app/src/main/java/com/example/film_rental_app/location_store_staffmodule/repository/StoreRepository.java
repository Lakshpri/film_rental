package com.example.film_rental_app.location_store_staffmodule.repository;

import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByAddress_AddressId(Integer addressId);
    boolean existsByManagerStaff_StaffId(Integer staffId);
}
