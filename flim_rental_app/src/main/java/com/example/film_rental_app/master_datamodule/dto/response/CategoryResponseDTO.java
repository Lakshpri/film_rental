package com.example.film_rental_app.master_datamodule.dto.response;

import java.time.LocalDateTime;

public class CategoryResponseDTO {

    private Integer categoryId;
    private String name;
    private LocalDateTime lastUpdate;

    public CategoryResponseDTO() {}

    public CategoryResponseDTO(Integer categoryId, String name, LocalDateTime lastUpdate) {
        this.categoryId = categoryId;
        this.name = name;
        this.lastUpdate = lastUpdate;
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
