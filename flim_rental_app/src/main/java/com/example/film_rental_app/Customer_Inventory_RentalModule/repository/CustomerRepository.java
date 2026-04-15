package com.example.film_rental_app.Customer_Inventory_RentalModule.repository;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
