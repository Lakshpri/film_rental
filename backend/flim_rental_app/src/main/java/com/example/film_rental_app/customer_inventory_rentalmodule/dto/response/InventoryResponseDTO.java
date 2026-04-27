package com.example.film_rental_app.customer_inventory_rentalmodule.dto.response;

import java.time.LocalDateTime;

// DTO used to send inventory data to frontend
public class InventoryResponseDTO {

    // Response message
    private String message;

    // Inventory ID
    private Integer inventoryId;

    // Film ID (from Film entity)
    private Integer filmId;

    // Film title (derived from Film entity)
    private String filmTitle;

    // Store ID (from Store entity)
    private Integer storeId;

    // Last update timestamp
    private LocalDateTime lastUpdate;

    // Default constructor
    public InventoryResponseDTO() {}

    // Getters and Setters

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public String getFilmTitle() { return filmTitle; }
    public void setFilmTitle(String filmTitle) { this.filmTitle = filmTitle; }

    public Integer getStoreId() { return storeId; }
    public void setStoreId(Integer storeId) { this.storeId = storeId; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}