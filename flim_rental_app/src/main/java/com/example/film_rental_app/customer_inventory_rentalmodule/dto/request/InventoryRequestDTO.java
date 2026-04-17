package com.example.film_rental_app.customer_inventory_rentalmodule.dto.request;

import jakarta.validation.constraints.NotNull;

public class InventoryRequestDTO {

    @NotNull(message = "Film ID is required")
    private Integer filmId;

    @NotNull(message = "Store ID is required")
    private Integer storeId;

    public InventoryRequestDTO() {}

    public InventoryRequestDTO(Integer filmId, Integer storeId) {
        this.filmId = filmId;
        this.storeId = storeId;
    }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
}
