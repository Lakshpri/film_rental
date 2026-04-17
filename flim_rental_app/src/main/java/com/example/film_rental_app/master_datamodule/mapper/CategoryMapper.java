package com.example.film_rental_app.master_datamodule.mapper;

import com.example.film_rental_app.master_datamodule.dto.request.CategoryRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CategoryResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getCategoryId(),
                category.getName(),
                category.getLastUpdate()
        );
    }

    public void updateEntity(Category category, CategoryRequestDTO dto) {
        category.setName(dto.getName());
    }
}
