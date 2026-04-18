package com.example.film_rental_app.payment_reportsmodule.entity;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import com.example.film_rental_app.payment_reportsmodule.service.implementation.AnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceImplTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private CustomerService customerService;

    @Mock
    private RentalService rentalService;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    // -------- CUSTOMER BALANCE --------

    @Test
    void testGetCustomerBalance() {
        Payment p1 = new Payment(); p1.setAmount(new BigDecimal("100"));
        Payment p2 = new Payment(); p2.setAmount(new BigDecimal("50"));

        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of(p1, p2));

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertEquals(150, ((BigDecimal) result.get("totalAmountPaid")).intValue());
    }

    @Test
    void testGetCustomerBalanceEmpty() {
        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertEquals(0, ((BigDecimal) result.get("totalAmountPaid")).intValue());
    }

    @Test
    void testCustomerBalanceCount() {
        Payment p = new Payment(); p.setAmount(new BigDecimal("20"));

        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of(p));

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertEquals(1, result.get("totalPayments"));
    }

    // -------- REWARDS REPORT --------

    @Test
    void testRewardsReport() {
        Customer c = new Customer();
        c.setCustomerId(1);
        c.setFirstName("A");
        c.setLastName("B");
        c.setEmail("a@mail");

        when(customerService.getAllCustomers()).thenReturn(List.of(c));
        when(rentalService.getRentalsByCustomer(1)).thenReturn(List.of(new Rental(), new Rental()));

        Map<String, Object> result = analyticsService.getRewardsReport();

        assertNotNull(result.get("averageRentals"));
    }

    @Test
    void testRewardsReportEligible() {
        Customer c = new Customer();
        c.setCustomerId(1);

        when(customerService.getAllCustomers()).thenReturn(List.of(c));
        when(rentalService.getRentalsByCustomer(1)).thenReturn(List.of(new Rental(), new Rental(), new Rental()));

        Map<String, Object> result = analyticsService.getRewardsReport();

        assertTrue(result.containsKey("rewardEligibleCustomers"));
    }

    @Test
    void testRewardsReportEmptyCustomers() {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getRewardsReport();

        assertEquals(0.0, result.get("averageRentals"));
    }

    @Test
    void testRewardsReportKeys() {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getRewardsReport();

        assertTrue(result.containsKey("averageRentals"));
    }

    // -------- FILM IN STOCK --------

    @Test
    void testFilmInStock() {
        Inventory inv = new Inventory();
        inv.setInventoryId(1);

        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmInStock(1, 1);

        assertEquals(1, result.get("inStockCount"));
    }

    @Test
    void testFilmInStockEmpty() {
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmInStock(1, 1);

        assertEquals(0, result.get("inStockCount"));
    }

    @Test
    void testFilmInStockKeys() {
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmInStock(1, 1);

        assertTrue(result.containsKey("inventoryItems"));
    }

    // -------- FILM NOT IN STOCK --------

    @Test
    void testFilmNotInStock() {
        Inventory inv = new Inventory();
        inv.setInventoryId(1);

        Rental r = new Rental();
        r.setInventory(inv);
        r.setReturnDate(null);

        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of(r));

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        assertEquals(1, result.get("notInStockCount"));
    }

    @Test
    void testFilmNotInStockEmpty() {
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        assertEquals(0, result.get("notInStockCount"));
    }

    @Test
    void testFilmNotInStockKeys() {
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        assertTrue(result.containsKey("inventoryItems"));
    }

    // -------- EXTRA EDGE CASES --------

    @Test
    void testCustomerBalanceKeyExists() {
        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertTrue(result.containsKey("totalAmountPaid"));
    }

    @Test
    void testRewardsReportNotNull() {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getRewardsReport();

        assertNotNull(result);
    }
}