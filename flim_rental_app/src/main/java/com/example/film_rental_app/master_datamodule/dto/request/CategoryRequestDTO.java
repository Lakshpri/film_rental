package com.example.film_rental_app.master_datamodule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequestDTO {

    @NotBlank(message = "Category name is required")
    @Size(max = 25, message = "Category name must not exceed 25 characters")
    private String name;

    public CategoryRequestDTO() {}

    public CategoryRequestDTO(String name) { this.name = name; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
