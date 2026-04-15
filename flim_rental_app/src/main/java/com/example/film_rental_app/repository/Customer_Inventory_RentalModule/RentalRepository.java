package com.example.film_rental_app.repository.Customer_Inventory_RentalModule;


import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByCustomer_CustomerId(Integer customerId);
}
