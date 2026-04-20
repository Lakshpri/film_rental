package com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.InventoryUnavailableException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.RentalAlreadyExistsException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.RentalInvalidOperationException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.RentalNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    @Autowired private RentalRepository rentalRepository;
    @Autowired private CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rental getRentalById(Integer rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
    }

    @Override
    public Rental createRental(Rental rental) {
        if (rental.getCustomer() != null && rental.getInventory() != null) {
            List<Rental> existingRentals = rentalRepository
                    .findByCustomer_CustomerId(rental.getCustomer().getCustomerId());

            boolean inventoryCurrentlyRented = rentalRepository.findAll().stream()
                    .anyMatch(r -> r.getInventory().getInventoryId()
                            .equals(rental.getInventory().getInventoryId())
                            && r.getReturnDate() == null);
            if (inventoryCurrentlyRented) {
                throw new InventoryUnavailableException(rental.getInventory().getInventoryId());
            }

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
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
        if (rental.getReturnDate() != null && updated.getReturnDate() != null
                && !rental.getReturnDate().equals(updated.getReturnDate())) {
            throw new RentalInvalidOperationException(rentalId,
                    "This rental was already returned on " + rental.getReturnDate()
                            + ". A completed rental cannot be changed.");
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
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
        if (rental.getReturnDate() == null) {
            throw new RentalInvalidOperationException(rentalId,
                    "This rental is still active — the item has not been returned yet. "
                            + "Please process the return first before trying to delete this record.");
        }
        rentalRepository.deleteById(rentalId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getRentalsByCustomer(Integer customerId) {
        // Verify customer exists first — throws 404 if not found (returns [] no more)
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        return rentalRepository.findByCustomer_CustomerId(customerId);
    }
}
