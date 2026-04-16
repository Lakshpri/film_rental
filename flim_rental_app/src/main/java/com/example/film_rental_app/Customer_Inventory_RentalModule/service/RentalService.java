package com.example.film_rental_app.Customer_Inventory_RentalModule.service;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Rental;

import java.util.List;

public interface RentalService {

    List<Rental> getAllRentals();

    Rental getRentalById(Integer rentalId);

    Rental createRental(Rental rental);

    Rental updateRental(Integer rentalId, Rental updated);

    void deleteRental(Integer rentalId);

    List<Rental> getRentalsByCustomer(Integer customerId);
}
