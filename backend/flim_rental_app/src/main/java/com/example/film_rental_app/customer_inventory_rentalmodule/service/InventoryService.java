package com.example.film_rental_app.customer_inventory_rentalmodule.service;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;

import java.util.List;

// Service interface for inventory operations
public interface InventoryService {

    // Get all inventory records
    List<Inventory> getAllInventory();

    // Get inventory by ID
    Inventory getInventoryById(Integer inventoryId);

    // Create new inventory
    Inventory createInventory(Inventory inventory);

    // Update existing inventory
    Inventory updateInventory(Integer inventoryId, Inventory updated);

    // Delete inventory by ID
    boolean deleteInventory(Integer inventoryId);

    // Get inventory by store ID
    List<Inventory> getInventoryByStore(Integer storeId);

    // Get inventory by film ID
    List<Inventory> getInventoryByFilm(Integer filmId);

    // Get inventory by both store and film
    List<Inventory> getInventoryByStoreAndFilm(Integer storeId, Integer filmId);
}