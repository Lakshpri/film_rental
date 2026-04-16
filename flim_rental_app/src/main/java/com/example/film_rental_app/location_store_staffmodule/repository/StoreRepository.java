package com.example.film_rental_app.location_store_staffmodule.repository;

import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {
}
