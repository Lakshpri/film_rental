package com.example.film_rental_app.customer_inventory_rentalmodule.mapper;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.InventoryRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.InventoryResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public Inventory toEntity(InventoryRequestDTO dto) {
        Inventory inventory = new Inventory();
        Film film = new Film();
        film.setFilmId(dto.getFilmId());
        inventory.setFilm(film);
        Store store = new Store();
        store.setStoreId(dto.getStoreId());
        inventory.setStore(store);
        return inventory;
    }

    public void updateEntity(Inventory inventory, InventoryRequestDTO dto) {
        if (dto.getFilmId() != null) {
            Film film = new Film();
            film.setFilmId(dto.getFilmId());
            inventory.setFilm(film);
        }
        if (dto.getStoreId() != null) {
            Store store = new Store();
            store.setStoreId(dto.getStoreId());
            inventory.setStore(store);
        }
    }

    public InventoryResponseDTO toResponseDTO(Inventory inventory) {
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setInventoryId(inventory.getInventoryId());
        dto.setLastUpdate(inventory.getLastUpdate());
        if (inventory.getFilm() != null) {
            dto.setFilmId(inventory.getFilm().getFilmId());
            dto.setFilmTitle(inventory.getFilm().getTitle());
        }
        if (inventory.getStore() != null) {
            dto.setStoreId(inventory.getStore().getStoreId());
        }
        return dto;
    }
}
