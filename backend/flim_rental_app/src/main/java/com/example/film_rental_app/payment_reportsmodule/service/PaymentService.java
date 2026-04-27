package com.example.film_rental_app.payment_reportsmodule.service;

import com.example.film_rental_app.payment_reportsmodule.dto.response.PaymentResponseDTO;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;

import java.util.List;

// Defines all payment operations available in the system
// PaymentServiceImpl provides the actual implementation
public interface PaymentService {

    // Returns all payments — used for the main payments table
    List<Payment> getAllPayments();

    // Returns a single payment by ID — throws 404 if not found
    Payment getPaymentById(Integer paymentId);

    // Saves a new payment after validation — throws error if amount invalid or rental already paid
    Payment createPayment(Payment payment);

    // Deletes payment and returns its data so client can confirm what was removed
    PaymentResponseDTO deletePayment(Integer paymentId);

    // Returns all payments for a specific customer — throws 404 if customer not found
    List<Payment> getPaymentsByCustomer(Integer customerId);
}