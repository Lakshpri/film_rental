package com.example.film_rental_app.controller.Customer_Inventory_RentalModule;

import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Inventory;
import com.example.film_rental_app.repository.Customer_Inventory_RentalModule.InventoryRepository;
import com.example.film_rental_app.repository.Location_Store_StaffModule.StoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;

    public InventoryController(InventoryRepository inventoryRepository, StoreRepository storeRepository) {
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
    }

    @GetMapping("/api/inventory")
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @GetMapping("/api/inventory/{inventoryId}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Integer inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/stores/{storeId}/inventory")
    public ResponseEntity<List<Inventory>> getInventoryByStore(@PathVariable Integer storeId) {
        if (!storeRepository.existsById(storeId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(inventoryRepository.findByStore_StoreId(storeId));
    }

    @PostMapping("/api/inventory")
    public Inventory createInventory(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @DeleteMapping("/api/inventory/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer inventoryId) {
        if (!inventoryRepository.existsById(inventoryId)) return ResponseEntity.notFound().build();
        inventoryRepository.deleteById(inventoryId);
        return ResponseEntity.noContent().build();
    }
}
