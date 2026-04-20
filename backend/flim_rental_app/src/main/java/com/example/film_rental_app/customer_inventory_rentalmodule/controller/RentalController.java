package com.example.film_rental_app.customer_inventory_rentalmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.RentalRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.RentalResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.RentalMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Validated
public class RentalController {

    @Autowired private RentalService rentalService;
    @Autowired private CustomerService customerService;
    @Autowired private InventoryService inventoryService;
    @Autowired private StaffService staffService;
    @Autowired private RentalMapper rentalMapper;

    @GetMapping
    public ResponseEntity<List<RentalResponseDTO>> getAllRentals() {
        List<RentalResponseDTO> result = rentalService.getAllRentals().stream()
                .map(rentalMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<RentalResponseDTO> getRentalById(
            @PathVariable @Positive(message = "Rental ID must be a positive number") Integer rentalId) {
        return ResponseEntity.ok(rentalMapper.toResponseDTO(rentalService.getRentalById(rentalId)));
    }

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(@Valid @RequestBody RentalRequestDTO dto) {
        Rental rental = rentalMapper.toEntity(dto);
        rental.setInventory(inventoryService.getInventoryById(dto.getInventoryId()));
        rental.setCustomer(customerService.getCustomerById(dto.getCustomerId()));
        rental.setStaff(staffService.getStaffById(dto.getStaffId()));
        return ResponseEntity.status(201)
                .body(rentalMapper.toResponseDTO(rentalService.createRental(rental)));
    }

    @PutMapping("/{rentalId}/return")
    public ResponseEntity<RentalResponseDTO> returnRental(
            @PathVariable @Positive(message = "Rental ID must be a positive number") Integer rentalId) {
        Rental existing = rentalService.getRentalById(rentalId);
        existing.setReturnDate(LocalDateTime.now());
        return ResponseEntity.ok(rentalMapper.toResponseDTO(rentalService.updateRental(rentalId, existing)));
    }
}
