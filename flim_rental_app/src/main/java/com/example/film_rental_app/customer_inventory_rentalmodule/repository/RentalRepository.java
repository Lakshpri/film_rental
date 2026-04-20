package com.example.film_rental_app.customer_inventory_rentalmodule.repository;


import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByCustomer_CustomerId(Integer customerId);
}
