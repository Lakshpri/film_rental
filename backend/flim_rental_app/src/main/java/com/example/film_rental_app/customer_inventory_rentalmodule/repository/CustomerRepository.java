package com.example.film_rental_app.customer_inventory_rentalmodule.repository;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Marks this interface as a repository (DAO layer)
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    // Checks if a customer exists with given email (used for duplicate validation)
    boolean existsByEmail(String email);

    // Checks if any customer is linked to a specific address
    boolean existsByAddress_AddressId(Integer addressId);

    // Checks if any customer belongs to a specific store
    boolean existsByStore_StoreId(Integer storeId);

    // Custom JPQL query to fetch customers based on store ID and active status
    @Query("SELECT c FROM Customer c WHERE c.store.storeId = :storeId AND c.active = :active")
    List<Customer> findByStoreIdAndActiveStatus(@Param("storeId") Integer storeId,
                                                @Param("active") boolean active);

    // Custom JPQL query for searching customers by first name or last name (case-insensitive)
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchByName(@Param("keyword") String keyword);
}