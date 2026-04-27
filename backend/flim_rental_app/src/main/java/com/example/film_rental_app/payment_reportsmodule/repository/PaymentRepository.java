package com.example.film_rental_app.payment_reportsmodule.repository;

import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

// Handles all database operations for the Payment table
// Extends JpaRepository — gives free CRUD methods (save, findById, findAll, delete etc.)
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Find all payments made by a specific customer
    List<Payment> findByCustomer_CustomerId(Integer customerId);

    // Check if a rental already has a payment — prevents duplicate payments
    boolean existsByRental_RentalId(Integer rentalId);

    // ── Sales by Store ─────────────────────────────────────────────
    // Aggregates total payments and revenue grouped by store
    // Chain followed: Payment → Staff → Store
    @Query("""
        SELECT p.staff.store.storeId    AS storeId,
               COUNT(p)                 AS totalPayments,
               SUM(p.amount)            AS totalRevenue
        FROM   Payment p
        GROUP  BY p.staff.store.storeId
        ORDER  BY totalRevenue DESC
    """)
    List<Object[]> findSalesByStore();

    // ── Sales by Category ──────────────────────────────────────────
    // Aggregates total payments and revenue grouped by film category
    // Chain followed: Payment → Rental → Inventory → Film → FilmCategories → Category
    @Query("""
        SELECT fc.category.name         AS categoryName,
               COUNT(p)                 AS totalPayments,
               SUM(p.amount)            AS totalRevenue
        FROM   Payment p
        JOIN   p.rental r
        JOIN   r.inventory i
        JOIN   i.film f
        JOIN   f.filmCategories fc
        GROUP  BY fc.category.name
        ORDER  BY totalRevenue DESC
    """)
    List<Object[]> findSalesByCategory();
}