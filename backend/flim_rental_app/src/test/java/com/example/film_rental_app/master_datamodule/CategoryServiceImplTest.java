package com.example.film_rental_app.master_datamodule;

import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.exception.CategoryAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.CategoryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CategoryRepository;
import com.example.film_rental_app.master_datamodule.service.implementation.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1);
        category.setName("Action");
    }


    //  POSITIVE TEST CASES


    @Test
    void testGetAllCategories_Success() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        List<Category> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals("Action", result.getName());
    }

    @Test
    void testCreateCategory_Success() {
        when(categoryRepository.existsByName("Action")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        verify(categoryRepository).save(category);
    }

    @Test
    void testUpdateCategory_Success() {
        Category updated = new Category();
        updated.setName("Comedy");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName("Comedy")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategory(1, updated);

        assertEquals("Comedy", result.getName());
    }




    //  NEGATIVE TEST CASES


    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(1));
    }

    @Test
    void testCreateCategory_Duplicate() {
        when(categoryRepository.existsByName("Action")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.createCategory(category));
    }

    @Test
    void testUpdateCategory_NotFound() {
        Category updated = new Category();
        updated.setName("Comedy");

        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(1, updated));
    }

    @Test
    void testUpdateCategory_DuplicateName() {
        Category updated = new Category();
        updated.setName("Comedy");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName("Comedy")).thenReturn(true);

        assertThrows(CategoryAlreadyExistsException.class,
                () -> categoryService.updateCategory(1, updated));
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(1)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(1));
    }
}