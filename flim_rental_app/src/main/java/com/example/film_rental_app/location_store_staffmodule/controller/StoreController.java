package com.example.film_rental_app.location_store_staffmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.InventoryResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.InventoryMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.location_store_staffmodule.dto.request.StoreRequestDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StaffResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StoreResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.mapper.StaffMapper;
import com.example.film_rental_app.location_store_staffmodule.mapper.StoreMapper;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stores")
@Validated
public class StoreController {

    @Autowired private StoreService storeService;
    @Autowired private StaffService staffService;
    @Autowired private AddressService addressService;
    @Autowired private InventoryService inventoryService;
    @Autowired private StoreMapper storeMapper;
    @Autowired private StaffMapper staffMapper;
    @Autowired private InventoryMapper inventoryMapper;

    @GetMapping
    public ResponseEntity<List<StoreResponseDTO>> getAllStores() {
        List<StoreResponseDTO> result = storeService.getAllStores().stream()
                .map(storeMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDTO> getStoreById(
            @PathVariable @Positive(message = "Store ID must be a positive number") Integer storeId) {
        return ResponseEntity.ok(storeMapper.toResponseDTO(storeService.getStoreById(storeId)));
    }

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@Valid @RequestBody StoreRequestDTO dto) {
        Store store = storeMapper.toEntity(dto);
        Staff manager = staffService.getStaffById(dto.getManagerStaffId());
        store.setManagerStaff(manager);
        Address address = addressService.getAddressById(dto.getAddressId());
        store.setAddress(address);
        return ResponseEntity.status(201).body(storeMapper.toResponseDTO(storeService.createStore(store)));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDTO> updateStore(
            @PathVariable @Positive(message = "Store ID must be a positive number") Integer storeId,
            @Valid @RequestBody StoreRequestDTO dto) {
        Store existing = storeService.getStoreById(storeId);
        if (dto.getManagerStaffId() != null) existing.setManagerStaff(staffService.getStaffById(dto.getManagerStaffId()));
        if (dto.getAddressId() != null) existing.setAddress(addressService.getAddressById(dto.getAddressId()));
        return ResponseEntity.ok(storeMapper.toResponseDTO(storeService.updateStore(storeId, existing)));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Map<String, Object>> deleteStore(@PathVariable Integer storeId) {
        storeService.getStoreById(storeId); // throws StoreNotFoundException (404) if not found
        storeService.deleteStore(storeId);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Store with ID " + storeId + " has been successfully deleted."
        ));
    }

    @GetMapping("/{storeId}/staff")
    public ResponseEntity<List<StaffResponseDTO>> getStaffByStore(
            @PathVariable @Positive(message = "Store ID must be a positive number") Integer storeId) {
        storeService.getStoreById(storeId);
        List<StaffResponseDTO> result = staffService.getStaffByStore(storeId).stream()
                .map(staffMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{storeId}/inventory")
    public ResponseEntity<List<InventoryResponseDTO>> getInventoryByStore(
            @PathVariable @Positive(message = "Store ID must be a positive number") Integer storeId) {
        storeService.getStoreById(storeId);
        List<InventoryResponseDTO> result = inventoryService.getInventoryByStore(storeId).stream()
                .map(inventoryMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
