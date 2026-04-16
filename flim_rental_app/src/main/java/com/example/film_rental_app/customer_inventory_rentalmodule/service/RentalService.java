package com.example.film_rental_app.customer_inventory_rentalmodule.service;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;

import java.util.List;

public interface RentalService {

    List<Rental> getAllRentals();

    Rental getRentalById(Integer rentalId);

    Rental createRental(Rental rental);

    Rental updateRental(Integer rentalId, Rental updated);

    void deleteRental(Integer rentalId);

    List<Rental> getRentalsByCustomer(Integer customerId);
}
