package com.example.film_rental_app.payment_reportsmodule.controller;

import com.example.film_rental_app.payment_reportsmodule.dto.request.PaymentRequestDTO;
import com.example.film_rental_app.payment_reportsmodule.dto.response.PaymentResponseDTO;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.mapper.PaymentMapper;
import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@Validated // enables method-level constraint validation (e.g. @Positive on path variables)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private PaymentMapper paymentMapper;

    // GET /api/payments — returns all payments
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> result = paymentService.getAllPayments().stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // GET /api/payments/{paymentId} — returns a single payment by ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(
            @PathVariable @Positive(message = "Payment ID must be a number greater than zero (e.g. 1, 2, 3)") Integer paymentId) {
        return ResponseEntity.ok(paymentMapper.toResponseDTO(paymentService.getPaymentById(paymentId)));
    }

    // GET /api/payments/customer/{customerId} — returns all payments for a given customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByCustomer(
            @PathVariable @Positive(message = "Customer ID must be a number greater than zero (e.g. 1, 2, 3)") Integer customerId) {
        List<PaymentResponseDTO> result = paymentService.getPaymentsByCustomer(customerId)
                .stream()
                .map(paymentMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(result);
    }

    // POST /api/payments — creates a new payment record
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO dto) {
        Payment payment = paymentMapper.toEntity(dto);
        payment.setCustomer(customerService.getCustomerById(dto.getCustomerId()));
        payment.setStaff(staffService.getStaffById(dto.getStaffId()));

        // Rental is optional
        if (dto.getRentalId() != null) {
            payment.setRental(rentalService.getRentalById(dto.getRentalId()));
        }

        PaymentResponseDTO created = paymentMapper.toResponseDTO(paymentService.createPayment(payment));
        created.setMessage("Payment created!");
        return ResponseEntity.status(201).body(created);
    }

    // DELETE /api/payments/{paymentId} — deletes a payment and returns its data
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> deletePayment(
            @PathVariable @Positive(message = "Payment ID must be a number greater than zero (e.g. 1, 2, 3)") Integer paymentId) {
        PaymentResponseDTO deleted = paymentService.deletePayment(paymentId);
        deleted.setMessage("Payment deleted Successfully!");
        return ResponseEntity.ok(deleted);
    }
}