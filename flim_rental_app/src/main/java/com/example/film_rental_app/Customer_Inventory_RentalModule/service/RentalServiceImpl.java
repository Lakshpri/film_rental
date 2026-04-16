package com.example.film_rental_app.Customer_Inventory_RentalModule.service;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Rental;
import com.example.film_rental_app.Customer_Inventory_RentalModule.exception.RentalNotFoundException;
import com.example.film_rental_app.Customer_Inventory_RentalModule.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;

    public RentalServiceImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

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
        return rentalRepository.save(rental);
    }

    @Override
    public Rental updateRental(Integer rentalId, Rental updated) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));
        if (updated.getRentalDate() != null) rental.setRentalDate(updated.getRentalDate());
        rental.setReturnDate(updated.getReturnDate());
        if (updated.getInventory() != null) rental.setInventory(updated.getInventory());
        if (updated.getCustomer() != null) rental.setCustomer(updated.getCustomer());
        if (updated.getStaff() != null) rental.setStaff(updated.getStaff());
        return rentalRepository.save(rental);
    }

    @Override
    public void deleteRental(Integer rentalId) {
        if (!rentalRepository.existsById(rentalId)) {
            throw new RentalNotFoundException(rentalId);
        }
        rentalRepository.deleteById(rentalId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getRentalsByCustomer(Integer customerId) {
        return rentalRepository.findByCustomer_CustomerId(customerId);
    }
}
