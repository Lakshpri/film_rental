package com.example.film_rental_app.customer_inventory_rentalmodule.repository;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // Marks this as repository layer
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    // Fetch inventory based on store ID (Inventory → Store → storeId)
    List<Inventory> findByStore_StoreId(Integer storeId);

    // Fetch inventory based on film ID (Inventory → Film → filmId)
    List<Inventory> findByFilm_FilmId(Integer filmId);

    // Fetch inventory based on both store ID and film ID
    List<Inventory> findByStore_StoreIdAndFilm_FilmId(Integer storeId, Integer filmId);
}