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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final CustomerService customerService;
    private final InventoryService inventoryService;
    private final StaffService staffService;
    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, CustomerService customerService,
                            InventoryService inventoryService, StaffService staffService,
                            RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.customerService = customerService;
        this.inventoryService = inventoryService;
        this.staffService = staffService;
        this.rentalMapper = rentalMapper;
    }

    @GetMapping
    public ResponseEntity<List<RentalResponseDTO>> getAllRentals() {
        List<RentalResponseDTO> result = rentalService.getAllRentals().stream()
                .map(rentalMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@PathVariable Integer rentalId) {
        return ResponseEntity.ok(rentalMapper.toResponseDTO(rentalService.getRentalById(rentalId)));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<RentalResponseDTO>> getRentalsByCustomer(@PathVariable Integer customerId) {
        List<RentalResponseDTO> result = rentalService.getRentalsByCustomer(customerId).stream()
                .map(rentalMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<RentalResponseDTO> createRental(@Valid @RequestBody RentalRequestDTO dto) {
        Rental rental = rentalMapper.toEntity(dto);
        rental.setInventory(inventoryService.getInventoryById(dto.getInventoryId()));
        rental.setCustomer(customerService.getCustomerById(dto.getCustomerId()));
        rental.setStaff(staffService.getStaffById(dto.getStaffId()));
        return ResponseEntity.status(201).body(rentalMapper.toResponseDTO(rentalService.createRental(rental)));
    }

    @PutMapping("/{rentalId}/return")
    public ResponseEntity<RentalResponseDTO> returnRental(@PathVariable Integer rentalId) {
        Rental existing = rentalService.getRentalById(rentalId);
        existing.setReturnDate(LocalDateTime.now());
        return ResponseEntity.ok(rentalMapper.toResponseDTO(rentalService.updateRental(rentalId, existing)));
    }

    @DeleteMapping("/{rentalId}")
    public ResponseEntity<Void> deleteRental(@PathVariable Integer rentalId) {
        rentalService.deleteRental(rentalId);
        return ResponseEntity.noContent().build();
    }
}
