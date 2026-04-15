package com.example.film_rental_app.Location_Store_StaffModule.repository;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}
