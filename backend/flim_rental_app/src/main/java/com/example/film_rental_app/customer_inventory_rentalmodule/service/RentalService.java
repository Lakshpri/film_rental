package com.example.film_rental_app.customer_inventory_rentalmodule.service;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;

import java.util.List;

// Service interface for rental operations
public interface RentalService {

    // Get all rentals
    List<Rental> getAllRentals();

    // Get rental by ID
    Rental getRentalById(Integer rentalId);

    // Create new rental
    Rental createRental(Rental rental);

    // Update existing rental
    Rental updateRental(Integer rentalId, Rental updated);

    // Delete rental by ID
    boolean deleteRental(Integer rentalId);

    // Get rentals by customer ID
    List<Rental> getRentalsByCustomer(Integer customerId);
}