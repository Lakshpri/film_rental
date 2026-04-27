package com.example.film_rental_app.customer_inventory_rentalmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.InventoryRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.InventoryResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.InventoryMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Validated
public class InventoryController {

    @Autowired private InventoryService inventoryService;
    @Autowired private FilmService filmService;
    @Autowired private StoreService storeService;
    @Autowired private InventoryMapper inventoryMapper;

    // GET /api/inventory
    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        List<InventoryResponseDTO> result = inventoryService.getAllInventory().stream()
                .map(inventoryMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(result);
    }

    // GET /api/inventory/{inventoryId}
    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(
            @PathVariable @Positive(message = "Inventory ID must be a positive number") Integer inventoryId) {
        return ResponseEntity.ok(inventoryMapper.toResponseDTO(inventoryService.getInventoryById(inventoryId)));
    }

    // POST /api/inventory
    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryRequestDTO dto) {
        Inventory inventory = inventoryMapper.toEntity(dto);
        inventory.setFilm(filmService.getFilmById(dto.getFilmId()));
        inventory.setStore(storeService.getStoreById(dto.getStoreId()));
        InventoryResponseDTO response = inventoryMapper.toResponseDTO(inventoryService.createInventory(inventory));
        response.setMessage("Inventory item created successfully.");
        return ResponseEntity.status(201).body(response);
    }

    // DELETE /api/inventory/{inventoryId}
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseDTO> deleteInventory(
            @PathVariable @Positive(message = "Inventory ID must be a positive number") Integer inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        InventoryResponseDTO response = new InventoryResponseDTO();
        response.setMessage("Inventory item deleted successfully.");
        return ResponseEntity.ok(response);
    }
}