package com.example.film_rental_app.Payment_ReportsModule.repository;


import com.example.film_rental_app.Payment_ReportsModule.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByCustomer_CustomerId(Integer customerId);
}
