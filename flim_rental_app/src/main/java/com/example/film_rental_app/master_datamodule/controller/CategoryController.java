package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.dto.request.CategoryRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CategoryResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.mapper.CategoryMapper;
import com.example.film_rental_app.master_datamodule.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> result = categoryService.getAllCategories().stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(categoryMapper.toResponseDTO(categoryService.getCategoryById(categoryId)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        return ResponseEntity.status(201).body(categoryMapper.toResponseDTO(categoryService.createCategory(category)));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Integer categoryId,
                                                              @Valid @RequestBody CategoryRequestDTO dto) {
        Category existing = categoryService.getCategoryById(categoryId);
        categoryMapper.updateEntity(existing, dto);
        return ResponseEntity.ok(categoryMapper.toResponseDTO(categoryService.updateCategory(categoryId, existing)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}