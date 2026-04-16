package com.example.film_rental_app.Payment_ReportsModule.service;


import com.example.film_rental_app.Payment_ReportsModule.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    List<Payment> getAllPayments();

    Payment getPaymentById(Integer paymentId);

    Payment createPayment(Payment payment);

    void deletePayment(Integer paymentId);

    List<Payment> getPaymentsByCustomer(Integer customerId);

    List<Payment> getPaymentsByStaff(Integer staffId);

    List<Payment> getPaymentsGreaterThan(BigDecimal amount);
}
