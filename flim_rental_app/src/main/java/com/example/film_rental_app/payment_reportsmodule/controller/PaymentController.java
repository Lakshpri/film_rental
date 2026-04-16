package com.example.film_rental_app.payment_reportsmodule.controller;

import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    public PaymentController(PaymentRepository paymentRepository, CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/api/payments")
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/api/payments/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/customers/{customerId}/payments")
    public ResponseEntity<List<Payment>> getPaymentsByCustomer(@PathVariable Integer customerId) {
        if (!customerRepository.existsById(customerId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(paymentRepository.findByCustomer_CustomerId(customerId));
    }

    @PostMapping("/api/payments")
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentRepository.save(payment);
    }

    @DeleteMapping("/api/payments/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer paymentId) {
        if (!paymentRepository.existsById(paymentId)) return ResponseEntity.notFound().build();
        paymentRepository.deleteById(paymentId);
        return ResponseEntity.noContent().build();
    }
}