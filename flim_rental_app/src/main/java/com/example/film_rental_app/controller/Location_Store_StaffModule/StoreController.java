package com.example.film_rental_app.controller.Location_Store_StaffModule;


import com.example.film_rental_app.entity.Location_Store_StaffModule.Store;
import com.example.film_rental_app.repository.Location_Store_StaffModule.StoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreRepository storeRepository;

    public StoreController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @GetMapping
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<Store> getStoreById(@PathVariable Integer storeId) {
        return storeRepository.findById(storeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Store createStore(@RequestBody Store store) {
        return storeRepository.save(store);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<Store> updateStore(@PathVariable Integer storeId, @RequestBody Store updated) {
        return storeRepository.findById(storeId).map(store -> {
            if (updated.getManagerStaff() != null) store.setManagerStaff(updated.getManagerStaff());
            if (updated.getAddress() != null) store.setAddress(updated.getAddress());
            return ResponseEntity.ok(storeRepository.save(store));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Integer storeId) {
        if (!storeRepository.existsById(storeId)) return ResponseEntity.notFound().build();
        storeRepository.deleteById(storeId);
        return ResponseEntity.noContent().build();
    }
}

