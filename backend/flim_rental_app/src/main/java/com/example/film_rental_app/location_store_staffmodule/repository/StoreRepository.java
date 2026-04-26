package com.example.film_rental_app.location_store_staffmodule.repository;

import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByAddress_AddressId(Integer addressId);
    boolean existsByManagerStaff_StaffId(Integer staffId);

    @Query("SELECT s FROM Store s " +
            "JOIN FETCH s.managerStaff " +
            "JOIN FETCH s.address a " +
            "JOIN FETCH a.city c " +
            "JOIN FETCH c.country")
    List<Store> findAllWithDetails();
}
