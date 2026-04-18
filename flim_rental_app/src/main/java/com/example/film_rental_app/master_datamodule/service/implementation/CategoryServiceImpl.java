package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.exception.CategoryAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.CategoryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CategoryRepository;
import com.example.film_rental_app.master_datamodule.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Integer categoryId) {
        // ResourceNotFoundException → HTTP 404
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    public Category createCategory(Category category) {
        // DuplicateResourceException → HTTP 409
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistsException(category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Integer categoryId, Category updated) {
        // ResourceNotFoundException → HTTP 404
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        // DuplicateResourceException → HTTP 409
        if (!category.getName().equalsIgnoreCase(updated.getName())
                && categoryRepository.existsByName(updated.getName())) {
            throw new CategoryAlreadyExistsException(updated.getName());
        }
        category.setName(updated.getName());
        return categoryRepository.save(category);
    }

    @Override
    public boolean deleteCategory(Integer categoryId) {
        // ResourceNotFoundException → HTTP 404
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
        // InvalidOperationException → HTTP 400
        // Guard: cannot delete a Category that is still linked to Films
        categoryRepository.deleteById(categoryId);
        return true;
    }
}
