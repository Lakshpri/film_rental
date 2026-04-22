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

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired private PaymentRepository  paymentRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PaymentMapper      paymentMapper; // ✅ needed for mapping before delete

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
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentInvalidOperationException(
                    "The payment amount must be greater than zero. You entered: "
                            + payment.getAmount()
                            + ". Please enter a valid amount greater than zero (e.g. 4.99).");
        }
        if (payment.getRental() != null
                && paymentRepository.existsByRental_RentalId(payment.getRental().getRentalId())) {
            throw new PaymentAlreadyExistsException(payment.getRental().getRentalId());
        }
        return paymentRepository.save(payment);
    }

    @Override
    // ✅ Return type changed from boolean to PaymentResponseDTO
    public PaymentResponseDTO deletePayment(Integer paymentId) {
        // Fetch first — throw 404 if not found
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        // ✅ Map to DTO BEFORE deleting — once deleted, data is gone
        PaymentResponseDTO response = paymentMapper.toResponseDTO(payment);

        paymentRepository.deleteById(paymentId);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomer(Integer customerId) {
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