package com.example.film_rental_app.master_datamodule;

import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setName("Action");

        Category saved = categoryRepository.save(category);

        assertNotNull(saved.getCategoryId());
        System.out.println("Created: " + saved);
    }

    @Test
    void testDeleteById() {
        Category category = new Category();
        category.setName("Horror");
        Category saved = categoryRepository.save(category);

        categoryRepository.deleteById(saved.getCategoryId());

        Optional<Category> deleted = categoryRepository.findById(saved.getCategoryId());

        assertFalse(deleted.isPresent());
        System.out.println("Deleted successfully");
    }
    // Tests the custom @Query
    @Test
    void testSearchByName_NoMatch_ReturnsEmptyList() {

        List<Category> result = categoryRepository.searchByName("zzznomatch");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        System.out.println("No match result: " + result);
    }
}