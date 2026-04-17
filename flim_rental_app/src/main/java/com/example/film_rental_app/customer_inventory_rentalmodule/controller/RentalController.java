package com.example.film_rental_app.customer_inventory_rentalmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Integer rentalId) {
        return ResponseEntity.ok(rentalService.getRentalById(rentalId));
    }

    @PostMapping
    public Rental createRental(@Valid @RequestBody Rental rental) {
        return rentalService.createRental(rental);
    }

    @PutMapping("/{rentalId}")
    public ResponseEntity<Rental> updateRental(@PathVariable Integer rentalId,
                                               @Valid @RequestBody Rental updated) {
        return ResponseEntity.ok(rentalService.updateRental(rentalId, updated));
    }

    @PutMapping("/{rentalId}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Integer rentalId) {
        Rental existing = rentalService.getRentalById(rentalId);
        existing.setReturnDate(java.time.LocalDateTime.now());
        return ResponseEntity.ok(rentalService.updateRental(rentalId, existing));
    }

    @DeleteMapping("/{rentalId}")
    public ResponseEntity<Void> deleteRental(@PathVariable Integer rentalId) {
        rentalService.deleteRental(rentalId);
        return ResponseEntity.noContent().build();
    }
}
