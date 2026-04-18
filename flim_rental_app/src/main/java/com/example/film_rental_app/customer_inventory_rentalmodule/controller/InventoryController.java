package com.example.film_rental_app.customer_inventory_rentalmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.InventoryRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.InventoryResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.InventoryMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private InventoryMapper inventoryMapper;

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        List<InventoryResponseDTO> result = inventoryService.getAllInventory().stream()
                .map(inventoryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Integer inventoryId) {
        return ResponseEntity.ok(inventoryMapper.toResponseDTO(inventoryService.getInventoryById(inventoryId)));
    }

    @GetMapping("/by-store/{storeId}")
    public ResponseEntity<List<InventoryResponseDTO>> getInventoryByStore(@PathVariable Integer storeId) {
        List<InventoryResponseDTO> result = inventoryService.getInventoryByStore(storeId).stream()
                .map(inventoryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-film/{filmId}")
    public ResponseEntity<List<InventoryResponseDTO>> getInventoryByFilm(@PathVariable Integer filmId) {
        List<InventoryResponseDTO> result = inventoryService.getInventoryByFilm(filmId).stream()
                .map(inventoryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryRequestDTO dto) {
        Inventory inventory = inventoryMapper.toEntity(dto);
        inventory.setFilm(filmService.getFilmById(dto.getFilmId()));
        inventory.setStore(storeService.getStoreById(dto.getStoreId()));
        return ResponseEntity.status(201).body(inventoryMapper.toResponseDTO(inventoryService.createInventory(inventory)));
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(@PathVariable Integer inventoryId,
                                                                @Valid @RequestBody InventoryRequestDTO dto) {
        Inventory existing = inventoryService.getInventoryById(inventoryId);
        if (dto.getFilmId() != null) existing.setFilm(filmService.getFilmById(dto.getFilmId()));
        if (dto.getStoreId() != null) existing.setStore(storeService.getStoreById(dto.getStoreId()));
        return ResponseEntity.ok(inventoryMapper.toResponseDTO(inventoryService.updateInventory(inventoryId, existing)));
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }
}
