package com.example.film_rental_app.payment_reportsmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.payment_reportsmodule.dto.response.PaymentResponseDTO;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentAlreadyExistsException;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentInvalidOperationException;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentNotFoundException;
import com.example.film_rental_app.payment_reportsmodule.mapper.PaymentMapper;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// Contains all business logic for payment operations
// @Transactional ensures DB changes are rolled back if anything fails mid-operation
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired private PaymentRepository  paymentRepository;
    @Autowired private CustomerRepository customerRepository;

    // Needed to convert payment to DTO before deletion — data is gone after delete
    @Autowired private PaymentMapper paymentMapper;

    // readOnly = true — no DB writes, faster performance for GET operations
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(Integer paymentId) {
        // orElseThrow — returns 404 with clear message if payment not found
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Override
    public Payment createPayment(Payment payment) {

        // Guard 1 — reject zero or negative amounts before hitting the DB
        if (payment.getAmount() == null ||
                payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentInvalidOperationException(
                    "The payment amount must be greater than zero. You entered: "
                            + payment.getAmount()
                            + ". Please enter a valid amount greater than zero (e.g. 4.99).");
        }

        // Guard 2 — prevent charging the same rental twice
        // Only checked when a rental is linked — rental is optional
        if (payment.getRental() != null &&
                paymentRepository.existsByRental_RentalId(
                        payment.getRental().getRentalId())) {
            throw new PaymentAlreadyExistsException(
                    payment.getRental().getRentalId());
        }

        return paymentRepository.save(payment);
    }

    @Override
    public PaymentResponseDTO deletePayment(Integer paymentId) {

        // Step 1 — confirm payment exists before attempting delete
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // Step 2 — capture data as DTO BEFORE deleting
        // Once deleted from DB, lazy-loaded fields like customer and staff are gone
        PaymentResponseDTO response = paymentMapper.toResponseDTO(payment);

        // Step 3 — safe to delete now that data is captured
        paymentRepository.deleteById(paymentId);

        // Step 4 — return the deleted payment's data so client can confirm
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomer(Integer customerId) {

        // Check customer exists first — empty list would be misleading if customer doesn't exist
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        return paymentRepository.findByCustomer_CustomerId(customerId);
    }
}