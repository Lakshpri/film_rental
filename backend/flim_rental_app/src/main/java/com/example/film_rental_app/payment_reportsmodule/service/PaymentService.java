package com.example.film_rental_app.payment_reportsmodule.service;


import com.example.film_rental_app.payment_reportsmodule.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    List<Payment> getAllPayments();

    Payment getPaymentById(Integer paymentId);

    Payment createPayment(Payment payment);

    boolean deletePayment(Integer paymentId);

    List<Payment> getPaymentsByCustomer(Integer customerId);

    List<Payment> getPaymentsByStaff(Integer staffId);

    List<Payment> getPaymentsGreaterThan(BigDecimal amount);
}
