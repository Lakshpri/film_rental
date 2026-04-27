package com.example.film_rental_app.customer_inventory_rentalmodule.repository;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // Marks this interface as repository layer
public interface RentalRepository extends JpaRepository<Rental, Integer> {

    // Fetch all rentals belonging to a specific customer
    // Uses nested property: Rental → Customer → customerId
    List<Rental> findByCustomer_CustomerId(Integer customerId);
}