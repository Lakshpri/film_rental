package com.example.film_rental_app.location_store_staffmodule.controller;

import com.example.film_rental_app.location_store_staffmodule.dto.request.StaffRequestDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StaffResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.mapper.StaffMapper;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/staff")
@Validated
public class StaffController {

    @Autowired private StaffService staffService;
    @Autowired private AddressService addressService;
    @Autowired private StoreService storeService;
    @Autowired private StaffMapper staffMapper;

    @GetMapping
    public ResponseEntity<List<StaffResponseDTO>> getAllStaff() {
        List<StaffResponseDTO> result = staffService.getAllStaff().stream()
                .map(staffMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> getStaffById(
            @PathVariable @Positive(message = "Staff ID must be a positive number") Integer staffId) {
        return ResponseEntity.ok(staffMapper.toResponseDTO(staffService.getStaffById(staffId)));
    }

    // FIX 1: path was "/staff/staff" — corrected to just POST on "/api/staff"
    // FIX 2: consumes multipart/form-data, uses @ModelAttribute (was already correct here)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> create(@ModelAttribute StaffRequestDTO dto) {
        Staff staff = staffMapper.toEntity(dto);
        Address address = addressService.getAddressById(dto.getAddressId());
        staff.setAddress(address);
        Store store = storeService.getStoreById(dto.getStoreId());
        staff.setStore(store);
        return ResponseEntity.status(201).body(staffMapper.toResponseDTO(staffService.createStaff(staff)));
    }

    // FIX 3: PUT also changed to multipart/form-data + @ModelAttribute so photo can be updated
    @PutMapping(value = "/{staffId}", consumes = "multipart/form-data")
    public ResponseEntity<StaffResponseDTO> updateStaff(
            @PathVariable @Positive(message = "Staff ID must be a positive number") Integer staffId,
            @ModelAttribute StaffRequestDTO dto) {
        Staff existing = staffService.getStaffById(staffId);
        staffMapper.updateEntity(existing, dto);
        if (dto.getAddressId() != null) existing.setAddress(addressService.getAddressById(dto.getAddressId()));
        if (dto.getStoreId() != null) existing.setStore(storeService.getStoreById(dto.getStoreId()));
        return ResponseEntity.ok(staffMapper.toResponseDTO(staffService.updateStaff(staffId, existing)));
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Map<String, Object>> deleteStaff(@PathVariable Integer staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Staff with ID " + staffId + " has been successfully deleted."
        ));
    }
}