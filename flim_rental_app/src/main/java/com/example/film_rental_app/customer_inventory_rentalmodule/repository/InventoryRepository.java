package com.example.film_rental_app.customer_inventory_rentalmodule.repository;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByStore_StoreId(Integer storeId);
    List<Inventory> findByFilm_FilmId(Integer filmId);
    List<Inventory> findByStore_StoreIdAndFilm_FilmId(Integer storeId, Integer filmId);
}

