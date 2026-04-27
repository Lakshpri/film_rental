package com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.InventoryNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.InventoryUnavailableException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.InventoryRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marks this class as service layer
@Transactional // Enables transaction management for all methods
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository; // Used for inventory DB operations

    @Autowired
    private RentalRepository rentalRepository; // Used to check rental status

    @Override
    @Transactional(readOnly = true) // Read-only for performance optimization
    public List<Inventory> getAllInventory() {
        // Fetch all inventory records
        return inventoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventoryById(Integer inventoryId) {
        // Fetch inventory by ID or throw exception if not found (HTTP 404)
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        // No duplicate restriction since multiple copies of same film can exist
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Integer inventoryId, Inventory updated) {

        // Fetch existing inventory or throw exception (HTTP 404)
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));

        // Check if inventory is currently rented (returnDate == null means active rental)
        boolean isCurrentlyRented = rentalRepository.findAll().stream()
                .anyMatch(r -> r.getInventory().getInventoryId().equals(inventoryId)
                        && r.getReturnDate() == null);

        // If rented, prevent update (HTTP 400)
        if (isCurrentlyRented) {
            throw new InventoryUnavailableException(inventoryId);
        }

        // Update film if provided
        if (updated.getFilm() != null) {
            inventory.setFilm(updated.getFilm());
        }

        // Update store if provided
        if (updated.getStore() != null) {
            inventory.setStore(updated.getStore());
        }

        // Save updated inventory
        return inventoryRepository.save(inventory);
    }

    @Override
    public boolean deleteInventory(Integer inventoryId) {

        // Check if inventory exists, else throw exception (HTTP 404)
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new InventoryNotFoundException(inventoryId);
        }

        // Check if inventory is currently rented
        boolean isCurrentlyRented = rentalRepository.findAll().stream()
                .anyMatch(r -> r.getInventory().getInventoryId().equals(inventoryId)
                        && r.getReturnDate() == null);

        // Prevent deletion if active rental exists
        if (isCurrentlyRented) {
            throw new InventoryUnavailableException(inventoryId);
        }

        // Delete inventory
        inventoryRepository.deleteById(inventoryId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByStore(Integer storeId) {
        // Fetch inventory based on store ID
        return inventoryRepository.findByStore_StoreId(storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByFilm(Integer filmId) {
        // Fetch inventory based on film ID
        return inventoryRepository.findByFilm_FilmId(filmId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByStoreAndFilm(Integer storeId, Integer filmId) {
        // Fetch inventory based on both store and film filters
        return inventoryRepository.findByStore_StoreIdAndFilm_FilmId(storeId, filmId);
    }
}