package com.example.film_rental_app.Customer_Inventory_RentalModule.repository;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.store.storeId = :storeId AND c.active = :active")
    List<Customer> findByStoreIdAndActiveStatus(@Param("storeId") Integer storeId,
                                                @Param("active") boolean active);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchByName(@Param("keyword") String keyword);
}

