package com.example.film_rental_app.customer_inventory_rentalmodule.mapper;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.InventoryRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.InventoryResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.stereotype.Component;

// Marks this class as Spring-managed component
@Component
public class InventoryMapper {

    // Converts RequestDTO to Entity
    public Inventory toEntity(InventoryRequestDTO dto) {
        Inventory inventory = new Inventory();

        // Set Film using only ID
        Film film = new Film();
        film.setFilmId(dto.getFilmId());
        inventory.setFilm(film);

        // Set Store using only ID
        Store store = new Store();
        store.setStoreId(dto.getStoreId());
        inventory.setStore(store);

        return inventory;
    }

    // Updates existing entity using DTO
    public void updateEntity(Inventory inventory, InventoryRequestDTO dto) {

        // Update Film only if provided
        if (dto.getFilmId() != null) {
            Film film = new Film();
            film.setFilmId(dto.getFilmId());
            inventory.setFilm(film);
        }

        // Update Store only if provided
        if (dto.getStoreId() != null) {
            Store store = new Store();
            store.setStoreId(dto.getStoreId());
            inventory.setStore(store);
        }
    }

    // Converts Entity to ResponseDTO
    public InventoryResponseDTO toResponseDTO(Inventory inventory) {
        InventoryResponseDTO dto = new InventoryResponseDTO();

        // Set basic fields
        dto.setInventoryId(inventory.getInventoryId());
        dto.setLastUpdate(inventory.getLastUpdate());

        // Extract Film details
        if (inventory.getFilm() != null) {
            dto.setFilmId(inventory.getFilm().getFilmId());
            dto.setFilmTitle(inventory.getFilm().getTitle());
        }

        // Extract Store ID
        if (inventory.getStore() != null) {
            dto.setStoreId(inventory.getStore().getStoreId());
        }

        return dto;
    }
}