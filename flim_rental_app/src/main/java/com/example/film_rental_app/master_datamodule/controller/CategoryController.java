package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.dto.request.CategoryRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.CategoryResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.mapper.CategoryMapper;
import com.example.film_rental_app.master_datamodule.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@Validated
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;


    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> result = categoryService.getAllCategories().stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable @Positive(message = "Category ID must be a positive number") Integer categoryId) {
        return ResponseEntity.ok(categoryMapper.toResponseDTO(categoryService.getCategoryById(categoryId)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        return ResponseEntity.status(201).body(categoryMapper.toResponseDTO(categoryService.createCategory(category)));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable @Positive(message = "Category ID must be a positive number") Integer categoryId,
                                                              @Valid @RequestBody CategoryRequestDTO dto) {
        Category existing = categoryService.getCategoryById(categoryId);
        categoryMapper.updateEntity(existing, dto);
        return ResponseEntity.ok(categoryMapper.toResponseDTO(categoryService.updateCategory(categoryId, existing)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Object>> deleteCategory(
            @PathVariable @Positive(message = "Category ID must be a positive number") Integer categoryId) {

        boolean deleted = categoryService.deleteCategory(categoryId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", deleted);
        response.put("message", "Category deleted successfully");
        response.put("categoryId", categoryId);

        return ResponseEntity.ok(response);
    }
}