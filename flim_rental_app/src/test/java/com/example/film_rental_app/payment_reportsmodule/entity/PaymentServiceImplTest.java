package com.example.film_rental_app.payment_reportsmodule.entity;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentAlreadyExistsException;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentInvalidOperationException;
import com.example.film_rental_app.payment_reportsmodule.exception.PaymentNotFoundException;
import com.example.film_rental_app.payment_reportsmodule.repository.PaymentRepository;
import com.example.film_rental_app.payment_reportsmodule.service.implementation.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // ---------------- POSITIVE ----------------

    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of(new Payment(), new Payment()));

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
    }

    @Test
    void testGetPaymentById() {
        Payment p = new Payment();
        when(paymentRepository.findById(1)).thenReturn(Optional.of(p));

        Payment result = paymentService.getPaymentById(1);

        assertNotNull(result);
    }

    @Test
    void testCreatePaymentValid() {
        Payment p = new Payment();
        p.setAmount(new BigDecimal("100"));

        when(paymentRepository.save(p)).thenReturn(p);

        Payment result = paymentService.createPayment(p);

        assertNotNull(result);
    }

    @Test
    void testCreatePaymentWithoutRental() {
        Payment p = new Payment();
        p.setAmount(new BigDecimal("200"));

        when(paymentRepository.save(p)).thenReturn(p);

        Payment result = paymentService.createPayment(p);

        assertEquals(new BigDecimal("200"), result.getAmount());
    }

    @Test
    void testGetPaymentsByCustomer() {
        when(customerRepository.existsById(1)).thenReturn(true);
        when(paymentRepository.findByCustomer_CustomerId(1)).thenReturn(List.of(new Payment()));

        List<Payment> result = paymentService.getPaymentsByCustomer(1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetPaymentsByStaff() {
        when(paymentRepository.findByStaff_StaffId(1)).thenReturn(List.of(new Payment()));

        List<Payment> result = paymentService.getPaymentsByStaff(1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetPaymentsGreaterThan() {
        when(paymentRepository.findByAmountGreaterThan(new BigDecimal("50")))
                .thenReturn(List.of(new Payment()));

        List<Payment> result = paymentService.getPaymentsGreaterThan(new BigDecimal("50"));

        assertEquals(1, result.size());
    }

    @Test
    void testCreatePaymentWithRental() {
        Payment p = new Payment();
        p.setAmount(new BigDecimal("150"));

        Rental r = new Rental();
        r.setRentalId(1);
        p.setRental(r);

        when(paymentRepository.existsByRental_RentalId(1)).thenReturn(false);
        when(paymentRepository.save(p)).thenReturn(p);

        Payment result = paymentService.createPayment(p);

        assertNotNull(result);
    }

    // ---------------- NEGATIVE ----------------

    @Test
    void testGetPaymentNotFound() {
        when(paymentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class,
                () -> paymentService.getPaymentById(1));
    }

    @Test
    void testCreatePaymentZeroAmount() {
        Payment p = new Payment();
        p.setAmount(BigDecimal.ZERO);

        assertThrows(PaymentInvalidOperationException.class,
                () -> paymentService.createPayment(p));
    }

    @Test
    void testCreatePaymentNegativeAmount() {
        Payment p = new Payment();
        p.setAmount(new BigDecimal("-10"));

        assertThrows(PaymentInvalidOperationException.class,
                () -> paymentService.createPayment(p));
    }

    @Test
    void testCreateDuplicatePayment() {
        Payment p = new Payment();
        p.setAmount(new BigDecimal("100"));

        Rental r = new Rental();
        r.setRentalId(1);
        p.setRental(r);

        when(paymentRepository.existsByRental_RentalId(1)).thenReturn(true);

        assertThrows(PaymentAlreadyExistsException.class,
                () -> paymentService.createPayment(p));
    }

    @Test
    void testDeletePaymentNotFound() {
        when(paymentRepository.existsById(1)).thenReturn(false);

        assertThrows(PaymentNotFoundException.class,
                () -> paymentService.deletePayment(1));
    }

    @Test
    void testDeletePaymentNotAllowed() {
        when(paymentRepository.existsById(1)).thenReturn(true);

        assertThrows(PaymentInvalidOperationException.class,
                () -> paymentService.deletePayment(1));
    }

    @Test
    void testCustomerNotFound() {
        when(customerRepository.existsById(1)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class,
                () -> paymentService.getPaymentsByCustomer(1));
    }
}