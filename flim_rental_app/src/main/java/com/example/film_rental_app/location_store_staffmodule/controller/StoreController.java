package com.example.film_rental_app.location_store_staffmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final StaffService staffService;
    private final InventoryService inventoryService;

    public StoreController(StoreService storeService,
                           StaffService staffService,
                           InventoryService inventoryService) {
        this.storeService = storeService;
        this.staffService = staffService;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<Store> getStoreById(@PathVariable Integer storeId) {
        return ResponseEntity.ok(storeService.getStoreById(storeId));
    }

    @PostMapping
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) {
        Store created = storeService.createStore(store);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<Store> updateStore(
            @PathVariable Integer storeId,
            @Valid @RequestBody Store updated) {
        return ResponseEntity.ok(storeService.updateStore(storeId, updated));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{storeId}/staff")
    public ResponseEntity<List<Staff>> getStaffByStore(@PathVariable Integer storeId) {
        storeService.getStoreById(storeId); // validate existence
        return ResponseEntity.ok(staffService.getStaffByStore(storeId));
    }

    @GetMapping("/{storeId}/inventory")
    public ResponseEntity<List<Inventory>> getInventoryByStore(@PathVariable Integer storeId) {
        storeService.getStoreById(storeId); // validate existence
        return ResponseEntity.ok(inventoryService.getInventoryByStore(storeId));
    }
}