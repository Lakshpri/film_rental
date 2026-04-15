package com.example.film_rental_app.controller.Customer_Inventory_RentalModule;

import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Rental;
import com.example.film_rental_app.repository.Customer_Inventory_RentalModule.CustomerRepository;
import com.example.film_rental_app.repository.Customer_Inventory_RentalModule.RentalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class RentalController {

    private final RentalRepository rentalRepository;
    private final com.example.film_rental_app.repository.Customer_Inventory_RentalModule.CustomerRepository customerRepository;

    public RentalController(RentalRepository rentalRepository, com.example.film_rental_app.repository.Customer_Inventory_RentalModule.CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/api/rentals")
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @GetMapping("/api/rentals/{rentalId}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Integer rentalId) {
        return rentalRepository.findById(rentalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/customers/{customerId}/rentals")
    public ResponseEntity<List<Rental>> getRentalsByCustomer(@PathVariable Integer customerId) {
        if (!customerRepository.existsById(customerId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(rentalRepository.findByCustomer_CustomerId(customerId));
    }

    @PostMapping("/api/rentals")
    public Rental createRental(@RequestBody Rental rental) {
        return rentalRepository.save(rental);
    }

    @PutMapping("/api/rentals/{rentalId}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Integer rentalId) {
        return rentalRepository.findById(rentalId).map(rental -> {
            rental.setReturnDate(LocalDateTime.now());
            return ResponseEntity.ok(rentalRepository.save(rental));
        }).orElse(ResponseEntity.notFound().build());
    }
}

