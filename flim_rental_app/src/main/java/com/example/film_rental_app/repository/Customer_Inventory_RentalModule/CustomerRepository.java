package com.example.film_rental_app.repository.Customer_Inventory_RentalModule;

import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
