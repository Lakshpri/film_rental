package com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.*;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marks as service layer
@Transactional // Enables transaction handling
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository; // Handles rental DB operations

    @Autowired
    private CustomerRepository customerRepository; // Used for validation

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        // Fetch all rentals
        return rentalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rental getRentalById(Integer rentalId) {
        // Fetch rental or throw exception if not found (HTTP 404)
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }

    @Override
    public Rental createRental(Rental rental) {

        // Get all rentals of the customer
        List<Rental> existingRentals =
                rentalRepository.findByCustomer_CustomerId(rental.getCustomer().getCustomerId());

        // Check if inventory is already rented (active rental)
        boolean inventoryCurrentlyRented = rentalRepository.findAll().stream()
                .anyMatch(r -> r.getInventory().getInventoryId()
                        .equals(rental.getInventory().getInventoryId())
                        && r.getReturnDate() == null);

        // Prevent renting unavailable inventory
        if (inventoryCurrentlyRented) {
            throw new InventoryUnavailableException(rental.getInventory().getInventoryId());
        }

        // Check if same customer already rented same inventory
        boolean customerAlreadyHasIt = existingRentals.stream()
                .anyMatch(r -> r.getInventory().getInventoryId()
                        .equals(rental.getInventory().getInventoryId())
                        && r.getReturnDate() == null);

        // Prevent duplicate rental
        if (customerAlreadyHasIt) {
            throw new RentalInvalidOperationException(
                    rental.getCustomer().getCustomerId(),
                    "Customer already rented this item"
            );
        }

        // Save rental
        return rentalRepository.save(rental);
    }

    @Override
    public Rental updateRental(Integer rentalId, Rental updated) {

        // Fetch rental or throw exception
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        // Prevent update if rental already completed
        if (rental.getReturnDate() != null) {
            throw new RentalInvalidOperationException(
                    rentalId,
                    "Cannot modify completed rental"
            );
        }

        // Update fields
        if (updated.getRentalDate() != null) {
            rental.setRentalDate(updated.getRentalDate());
        }

        rental.setReturnDate(updated.getReturnDate());

        // Save updated rental
        return rentalRepository.save(rental);
    }

    @Override
    public boolean deleteRental(Integer rentalId) {

        // Fetch rental or throw exception
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        // Prevent deletion if rental still active
        if (rental.getReturnDate() == null) {
            throw new RentalInvalidOperationException(
                    rentalId,
                    "Return item before deleting"
            );
        }

        // Delete rental
        rentalRepository.deleteById(rentalId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getRentalsByCustomer(Integer customerId) {

        // Check if customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        // Return rentals (empty list if none found)
        return rentalRepository.findByCustomer_CustomerId(customerId);
    }
}