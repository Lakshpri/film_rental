package com.example.film_rental_app.customer_inventory_rentalmodule.dto.response;

import java.time.LocalDateTime;

public class InventoryResponseDTO {

    private Integer inventoryId;
    private Integer filmId;
    private String filmTitle;
    private Integer storeId;
    private LocalDateTime lastUpdate;

    public InventoryResponseDTO() {}

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
