package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmCategoryRepository;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.exception.CategoryAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.CategoryInvalidOperationException;
import com.example.film_rental_app.master_datamodule.exception.CategoryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CategoryRepository;
import com.example.film_rental_app.master_datamodule.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private FilmCategoryRepository filmCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by("categoryId"));
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new CategoryAlreadyExistsException(category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Integer categoryId, Category updated) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (!category.getName().equalsIgnoreCase(updated.getName())
                && categoryRepository.existsByName(updated.getName())) {
            throw new CategoryAlreadyExistsException(updated.getName());
        }
        category.setName(updated.getName());
        return categoryRepository.save(category);
    }

    @Override
    public boolean deleteCategory(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
        if (!filmCategoryRepository.findById_CategoryId(categoryId).isEmpty()) {
            throw new CategoryInvalidOperationException(categoryId,
                    "This category is still assigned to one or more films. "
                            + "Please remove it from all films before deleting this category.");
        }
        categoryRepository.deleteById(categoryId);
        return true;
    }
}
