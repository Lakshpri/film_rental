package com.example.film_rental_app.payment_reportsmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentRepository paymentRepository;
    private CustomerRepository customerRepository;

    public PaymentController(PaymentRepository paymentRepository, CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> list = paymentRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);

        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(payment);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Payment>> getPaymentsByCustomer(@PathVariable Integer customerId) {

        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.notFound().build();
        }

        List<Payment> list = paymentRepository.findByCustomer_CustomerId(customerId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {

        Payment saved = paymentRepository.save(payment);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable Integer paymentId) {

        if (!paymentRepository.existsById(paymentId)) {
            return ResponseEntity.notFound().build();
        }

        paymentRepository.deleteById(paymentId);
        return ResponseEntity.ok("Payment deleted successfully");
    }
}