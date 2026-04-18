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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
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

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> result = paymentService.getAllPayments().stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(paymentMapper.toResponseDTO(paymentService.getPaymentById(paymentId)));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByCustomer(@PathVariable Integer customerId) {
        List<PaymentResponseDTO> result = paymentService.getPaymentsByCustomer(customerId).stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-staff/{staffId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStaff(@PathVariable Integer staffId) {
        List<PaymentResponseDTO> result = paymentService.getPaymentsByStaff(staffId).stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/greater-than")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsGreaterThan(@RequestParam BigDecimal amount) {
        List<PaymentResponseDTO> result = paymentService.getPaymentsGreaterThan(amount).stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO dto) {
        Payment payment = paymentMapper.toEntity(dto);
        payment.setCustomer(customerService.getCustomerById(dto.getCustomerId()));
        payment.setStaff(staffService.getStaffById(dto.getStaffId()));
        if (dto.getRentalId() != null) {
            payment.setRental(rentalService.getRentalById(dto.getRentalId()));
        }
        return ResponseEntity.status(201).body(paymentMapper.toResponseDTO(paymentService.createPayment(payment)));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }
}
