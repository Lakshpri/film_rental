package com.example.film_rental_app.payment_reportsmodule.service.implementation;


import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentNotFoundException;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import com.example.film_rental_app.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
    }

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
        return paymentRepository.save(payment);
    }

    @Override
    public boolean deletePayment(Integer paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new PaymentNotFoundException(paymentId);
        }
        paymentRepository.deleteById(paymentId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByCustomer(Integer customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", customerId);
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
        return paymentRepository.findByAmountGreaterThan(amount);
    }
}
