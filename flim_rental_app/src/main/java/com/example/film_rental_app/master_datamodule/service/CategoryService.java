package com.example.film_rental_app.master_datamodule.service;

import com.example.film_rental_app.master_datamodule.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Integer categoryId);

    Category createCategory(Category category);

    Category updateCategory(Integer categoryId, Category updated);

    void deleteCategory(Integer categoryId);
}
