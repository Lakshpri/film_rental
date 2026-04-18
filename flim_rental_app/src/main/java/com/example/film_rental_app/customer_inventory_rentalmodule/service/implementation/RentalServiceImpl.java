package com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.InventoryUnavailableException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.RentalAlreadyExistsException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.RentalInvalidOperationException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.RentalNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rental getRentalById(Integer rentalId) {
        // ResourceNotFoundException → HTTP 404
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }

    @Override
    public Rental createRental(Rental rental) {
        if (rental.getCustomer() != null && rental.getInventory() != null) {
            List<Rental> existingRentals = rentalRepository
                    .findByCustomer_CustomerId(rental.getCustomer().getCustomerId());

            // InvalidOperationException → HTTP 400: inventory currently rented out (not returned yet)
            boolean inventoryCurrentlyRented = rentalRepository.findAll().stream()
                    .anyMatch(r -> r.getInventory().getInventoryId()
                            .equals(rental.getInventory().getInventoryId())
                            && r.getReturnDate() == null);
            if (inventoryCurrentlyRented) {
                throw new InventoryUnavailableException(rental.getInventory().getInventoryId());
            }

            // DuplicateResourceException → HTTP 409: this customer already has this inventory rented
            boolean customerAlreadyHasIt = existingRentals.stream()
                    .anyMatch(r -> r.getInventory().getInventoryId()
                            .equals(rental.getInventory().getInventoryId())
                            && r.getReturnDate() == null);
            if (customerAlreadyHasIt) {
                throw new RentalAlreadyExistsException(
                        rental.getCustomer().getCustomerId(),
                        rental.getInventory().getInventoryId());
            }
        }
        return rentalRepository.save(rental);
    }

    @Override
    public Rental updateRental(Integer rentalId, Rental updated) {
        // ResourceNotFoundException → HTTP 404
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
        // InvalidOperationException → HTTP 400: already returned, cannot modify
        if (rental.getReturnDate() != null && updated.getReturnDate() != null
                && !rental.getReturnDate().equals(updated.getReturnDate())) {
            throw new RentalInvalidOperationException(rentalId,
                    "This Rental was already returned on " + rental.getReturnDate()
                            + ". A completed Rental cannot be modified.");
        }
        if (updated.getRentalDate() != null) rental.setRentalDate(updated.getRentalDate());
        rental.setReturnDate(updated.getReturnDate());
        if (updated.getInventory() != null) rental.setInventory(updated.getInventory());
        if (updated.getCustomer()  != null) rental.setCustomer(updated.getCustomer());
        if (updated.getStaff()     != null) rental.setStaff(updated.getStaff());
        return rentalRepository.save(rental);
    }

    @Override
    public boolean deleteRental(Integer rentalId) {
        // ResourceNotFoundException → HTTP 404
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
        // InvalidOperationException → HTTP 400: cannot delete an active (unreturned) rental
        if (rental.getReturnDate() == null) {
            throw new RentalInvalidOperationException(rentalId,
                    "This Rental is still active — the item has not been returned yet. "
                            + "Process the return before attempting to delete this Rental record.");
        }
        rentalRepository.deleteById(rentalId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getRentalsByCustomer(Integer customerId) {
        return rentalRepository.findByCustomer_CustomerId(customerId);
    }
}
