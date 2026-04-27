package com.example.film_rental_app.customer_inventory_rentalmodule.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// DTO used to receive inventory input from frontend
public class InventoryRequestDTO {

    // Film ID must not be null and must be positive
    @NotNull(message = "Film ID is required")
    @Positive(message = "Film ID must be a number greater than zero")
    private Integer filmId;

    // Store ID must not be null and must be positive
    @NotNull(message = "Store ID is required")
    @Positive(message = "Store ID must be a number greater than zero")
    private Integer storeId;

    // Default constructor
    public InventoryRequestDTO() {}

    // Constructor with values
    public InventoryRequestDTO(Integer filmId, Integer storeId) {
        this.filmId = filmId;
        this.storeId = storeId;
    }

    // Getters and Setters

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }
}