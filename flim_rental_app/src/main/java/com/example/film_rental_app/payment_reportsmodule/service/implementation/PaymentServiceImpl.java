package com.example.film_rental_app.payment_reportsmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentAlreadyExistsException;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentInvalidOperationException;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentNotFoundException;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository  paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(Integer paymentId) {
        // ResourceNotFoundException → HTTP 404
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Override
    public Payment createPayment(Payment payment) {
        // InvalidOperationException → HTTP 400: amount must be positive
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentInvalidOperationException(
                    "Payment amount must be greater than zero. You provided: "
                            + payment.getAmount()
                            + ". Please enter a valid positive amount.");
        }
        // DuplicateResourceException → HTTP 409: a payment for this rental already exists
        if (payment.getRental() != null
                && paymentRepository.existsByRental_RentalId(payment.getRental().getRentalId())) {
            throw new PaymentAlreadyExistsException(payment.getRental().getRentalId());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public boolean deletePayment(Integer paymentId) {
        // ResourceNotFoundException → HTTP 404: payment must exist first
        if (!paymentRepository.existsById(paymentId)) {
            throw new PaymentNotFoundException(paymentId);
        }
        // InvalidOperationException → HTTP 400: payments are financial records, deletion is not allowed
        throw new PaymentInvalidOperationException(
                "Payment records are permanent financial entries and cannot be deleted. "
                        + "Payment ID = " + paymentId + " was found, but deletion is not permitted by business rules. "
                        + "If this payment was made in error, please contact the system administrator.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomer(Integer customerId) {
        // ResourceNotFoundException → HTTP 404: customer must exist
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        return paymentRepository.findByCustomer_CustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByStaff(Integer staffId) {
        return paymentRepository.findByStaff_StaffId(staffId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsGreaterThan(BigDecimal amount) {
        // InvalidOperationException → HTTP 400: filter amount must be non-negative
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentInvalidOperationException(
                    "The filter amount must be zero or a positive number. You provided: "
                            + amount + ". Negative amounts are not valid for this filter.");
        }
        return paymentRepository.findByAmountGreaterThan(amount);
    }
}
