package com.example.film_rental_app.payment_reportsmodule.repository;

import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByCustomer_CustomerId(Integer customerId);
    List<Payment> findByAmountGreaterThan(BigDecimal amount);
    List<Payment> findByStaff_StaffId(Integer staffId);
    boolean existsByRental_RentalId(Integer rentalId);

    // ── Sales by Store ─────────────────────────────────────────────
    // Chain: Payment -> Staff -> Store
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
    // Chain: Payment -> Rental -> Inventory -> Film -> filmCategories -> Category
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