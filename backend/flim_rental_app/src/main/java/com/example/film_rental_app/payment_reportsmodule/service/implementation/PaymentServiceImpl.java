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

    @Autowired private PaymentRepository  paymentRepository;
    @Autowired private CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    @Override
    public Payment createPayment(Payment payment) {
        // Amount must be greater than zero (0.01 minimum enforced by DTO too — double safety)
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentInvalidOperationException(
                    "The payment amount must be greater than zero. You entered: "
                            + payment.getAmount()
                            + ". Please enter a valid amount greater than zero (e.g. 4.99).");
        }
        // Each rental can only be paid once
        if (payment.getRental() != null
                && paymentRepository.existsByRental_RentalId(payment.getRental().getRentalId())) {
            throw new PaymentAlreadyExistsException(payment.getRental().getRentalId());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public boolean deletePayment(Integer paymentId) {
        // Check if payment exists — throw 404 if not
        if (!paymentRepository.existsById(paymentId)) {
            throw new PaymentNotFoundException(paymentId);
        }
        // ✅ Actually delete now
        paymentRepository.deleteById(paymentId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomer(Integer customerId) {
        // Throw 404 if customer does not exist — no empty list
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
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentInvalidOperationException(
                    "The amount you entered for filtering must be zero or a positive number. You entered: "
                            + amount + ". Negative amounts cannot be used here.");
        }
        return paymentRepository.findByAmountGreaterThan(amount);
    }
}
