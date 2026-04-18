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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StaffMapper staffMapper;


    @GetMapping
    public ResponseEntity<List<StaffResponseDTO>> getAllStaff() {
        List<StaffResponseDTO> result = staffService.getAllStaff().stream()
                .map(staffMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable Integer staffId) {
        return ResponseEntity.ok(staffMapper.toResponseDTO(staffService.getStaffById(staffId)));
    }

    @PostMapping
    public ResponseEntity<StaffResponseDTO> createStaff(@Valid @RequestBody StaffRequestDTO dto) {
        Staff staff = staffMapper.toEntity(dto);
        Address address = addressService.getAddressById(dto.getAddressId());
        staff.setAddress(address);
        Store store = storeService.getStoreById(dto.getStoreId());
        staff.setStore(store);
        return ResponseEntity.status(201).body(staffMapper.toResponseDTO(staffService.createStaff(staff)));
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable Integer staffId,
                                                        @Valid @RequestBody StaffRequestDTO dto) {
        Staff existing = staffService.getStaffById(staffId);
        staffMapper.updateEntity(existing, dto);
        if (dto.getAddressId() != null) {
            existing.setAddress(addressService.getAddressById(dto.getAddressId()));
        }
        if (dto.getStoreId() != null) {
            existing.setStore(storeService.getStoreById(dto.getStoreId()));
        }
        return ResponseEntity.ok(staffMapper.toResponseDTO(staffService.updateStaff(staffId, existing)));
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Integer staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }
}
