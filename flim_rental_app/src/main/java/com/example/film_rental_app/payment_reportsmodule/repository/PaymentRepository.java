package com.example.film_rental_app.payment_reportsmodule.repository;


import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Already existing
    List<Payment> findByCustomer_CustomerId(Integer customerId);

    // Find payments greater than amount
    List<Payment> findByAmountGreaterThan(BigDecimal amount);

    //  Find payments by staff
    List<Payment> findByStaff_StaffId(Integer staffId);
}