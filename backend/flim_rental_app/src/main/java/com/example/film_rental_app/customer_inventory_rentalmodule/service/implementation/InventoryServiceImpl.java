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

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private RentalRepository    rentalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventoryById(Integer inventoryId) {
        // ResourceNotFoundException → HTTP 404
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        // A store can have multiple copies of the same film — no duplicate block here
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Integer inventoryId, Inventory updated) {
        // ResourceNotFoundException → HTTP 404
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException(inventoryId));
        // InvalidOperationException → HTTP 400: cannot update inventory that is currently rented
        boolean isCurrentlyRented = rentalRepository.findAll().stream()
                .anyMatch(r -> r.getInventory().getInventoryId().equals(inventoryId)
                        && r.getReturnDate() == null);
        if (isCurrentlyRented) {
            throw new InventoryUnavailableException(inventoryId);
        }
        if (updated.getFilm()  != null) inventory.setFilm(updated.getFilm());
        if (updated.getStore() != null) inventory.setStore(updated.getStore());
        return inventoryRepository.save(inventory);
    }

    @Override
    public boolean deleteInventory(Integer inventoryId) {
        // ResourceNotFoundException → HTTP 404
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new InventoryNotFoundException(inventoryId);
        }
        // InvalidOperationException → HTTP 400: cannot delete inventory that is currently rented
        boolean isCurrentlyRented = rentalRepository.findAll().stream()
                .anyMatch(r -> r.getInventory().getInventoryId().equals(inventoryId)
                        && r.getReturnDate() == null);
        if (isCurrentlyRented) {
            throw new InventoryUnavailableException(inventoryId);
        }
        inventoryRepository.deleteById(inventoryId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByStore(Integer storeId) {
        return inventoryRepository.findByStore_StoreId(storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByFilm(Integer filmId) {
        return inventoryRepository.findByFilm_FilmId(filmId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getInventoryByStoreAndFilm(Integer storeId, Integer filmId) {
        return inventoryRepository.findByStore_StoreIdAndFilm_FilmId(storeId, filmId);
    }
}
