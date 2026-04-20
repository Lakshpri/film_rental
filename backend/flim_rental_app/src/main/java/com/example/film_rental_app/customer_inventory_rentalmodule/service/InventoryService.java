package com.example.film_rental_app.customer_inventory_rentalmodule.service;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;

import java.util.List;

public interface InventoryService {

    List<Inventory> getAllInventory();

    Inventory getInventoryById(Integer inventoryId);

    Inventory createInventory(Inventory inventory);

    Inventory updateInventory(Integer inventoryId, Inventory updated);

    boolean deleteInventory(Integer inventoryId);

    List<Inventory> getInventoryByStore(Integer storeId);

    List<Inventory> getInventoryByFilm(Integer filmId);

    List<Inventory> getInventoryByStoreAndFilm(Integer storeId, Integer filmId);
}
