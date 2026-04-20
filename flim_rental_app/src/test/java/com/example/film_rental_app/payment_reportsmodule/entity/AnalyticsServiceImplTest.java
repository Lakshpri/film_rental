package com.example.film_rental_app.payment_reportsmodule.entity;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmRepository;
import com.example.film_rental_app.location_store_staffmodule.exception.StoreNotFoundException;
import com.example.film_rental_app.location_store_staffmodule.repository.StoreRepository;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import com.example.film_rental_app.payment_reportsmodule.service.implementation.AnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceImplTest {

    @Mock private PaymentService     paymentService;
    @Mock private CustomerService    customerService;
    @Mock private RentalService      rentalService;
    @Mock private InventoryService   inventoryService;
    @Mock private FilmRepository     filmRepository;
    @Mock private StoreRepository    storeRepository;
    @Mock private CustomerRepository customerRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    // ======================================================
    // getCustomerBalance
    // ======================================================

    @Test
    void testGetCustomerBalance_returnsCorrectTotal() {
        Payment p1 = new Payment(); p1.setAmount(new BigDecimal("100"));
        Payment p2 = new Payment(); p2.setAmount(new BigDecimal("50"));

        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of(p1, p2));

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertEquals(150, ((BigDecimal) result.get("totalAmountPaid")).intValue());
    }

    @Test
    void testGetCustomerBalance_noPayments_returnsZero() {
        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertEquals(0, ((BigDecimal) result.get("totalAmountPaid")).intValue());
    }

    @Test
    void testGetCustomerBalance_returnsCorrectPaymentCount() {
        Payment p = new Payment(); p.setAmount(new BigDecimal("20"));

        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of(p));

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertEquals(1, result.get("totalPayments"));
    }

    @Test
    void testGetCustomerBalance_containsExpectedKeys() {
        when(customerService.getCustomerById(1)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(1)).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getCustomerBalance(1);

        assertTrue(result.containsKey("customerId"));
        assertTrue(result.containsKey("totalPayments"));
        assertTrue(result.containsKey("totalAmountPaid"));
    }

    @Test
    void testGetCustomerBalance_customerIdEchoedInResult() {
        when(customerService.getCustomerById(42)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(42)).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getCustomerBalance(42);

        assertEquals(42, result.get("customerId"));
    }

    @Test
    void testGetCustomerBalance_multiplePayments_summedCorrectly() {
        Payment p1 = new Payment(); p1.setAmount(new BigDecimal("10.50"));
        Payment p2 = new Payment(); p2.setAmount(new BigDecimal("5.25"));
        Payment p3 = new Payment(); p3.setAmount(new BigDecimal("4.25"));

        when(customerService.getCustomerById(2)).thenReturn(new Customer());
        when(paymentService.getPaymentsByCustomer(2)).thenReturn(List.of(p1, p2, p3));

        Map<String, Object> result = analyticsService.getCustomerBalance(2);

        assertEquals(new BigDecimal("20.00"), result.get("totalAmountPaid"));
        assertEquals(3, result.get("totalPayments"));
    }

    // ======================================================
    // getRewardsReport
    // ======================================================

    @Test
    void testGetRewardsReport_containsAverageRentals() {
        Customer c = new Customer();
        c.setCustomerId(1);
        c.setFirstName("Alice"); c.setLastName("Smith"); c.setEmail("alice@test.com");

        when(customerRepository.findAll()).thenReturn(List.of(c));
        when(rentalService.getRentalsByCustomer(1)).thenReturn(List.of(new Rental(), new Rental()));

        Map<String, Object> result = analyticsService.getRewardsReport();

        assertNotNull(result.get("averageRentals"));
        assertTrue(result.containsKey("rewardEligibleCustomers"));
    }

    @Test
    void testGetRewardsReport_emptyCustomers_averageIsZero() {
        when(customerRepository.findAll()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getRewardsReport();

        assertEquals(0.0, result.get("averageRentals"));
    }

    @Test
    void testGetRewardsReport_emptyCustomers_eligibleListEmpty() {
        when(customerRepository.findAll()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getRewardsReport();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> eligible = (List<Map<String, Object>>) result.get("rewardEligibleCustomers");
        assertTrue(eligible.isEmpty());
    }

    @Test
    void testGetRewardsReport_singleCustomer_notEligible_aboveAverageRequiresStrictlyGreater() {
        // Single customer: average == their rental count, so NOT eligible (needs strictly > avg)
        Customer c = new Customer();
        c.setCustomerId(1);

        when(customerRepository.findAll()).thenReturn(List.of(c));
        when(rentalService.getRentalsByCustomer(1)).thenReturn(List.of(new Rental(), new Rental()));

        Map<String, Object> result = analyticsService.getRewardsReport();

        @SuppressWarnings("unchecked")
        List<?> eligible = (List<?>) result.get("rewardEligibleCustomers");
        assertTrue(eligible.isEmpty());   // average == 2, rental count == 2, not strictly greater
    }

    @Test
    void testGetRewardsReport_multipleCustomers_correctEligibleCount() {
        Customer c1 = new Customer(); c1.setCustomerId(1);
        Customer c2 = new Customer(); c2.setCustomerId(2);

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        // c1 has 5 rentals, c2 has 1 rental → avg = 3 → only c1 is eligible
        when(rentalService.getRentalsByCustomer(1)).thenReturn(
                List.of(new Rental(), new Rental(), new Rental(), new Rental(), new Rental()));
        when(rentalService.getRentalsByCustomer(2)).thenReturn(List.of(new Rental()));

        Map<String, Object> result = analyticsService.getRewardsReport();

        @SuppressWarnings("unchecked")
        List<?> eligible = (List<?>) result.get("rewardEligibleCustomers");
        assertEquals(1, eligible.size());
    }

    @Test
    void testGetRewardsReport_averageCalculatedCorrectly() {
        Customer c1 = new Customer(); c1.setCustomerId(1);
        Customer c2 = new Customer(); c2.setCustomerId(2);

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(rentalService.getRentalsByCustomer(1)).thenReturn(List.of(new Rental(), new Rental()));
        when(rentalService.getRentalsByCustomer(2)).thenReturn(List.of(new Rental(), new Rental(), new Rental(), new Rental()));

        Map<String, Object> result = analyticsService.getRewardsReport();

        // avg = (2 + 4) / 2 = 3.0
        assertEquals(3.0, result.get("averageRentals"));
    }

    @Test
    void testGetRewardsReport_resultNotNull() {
        when(customerRepository.findAll()).thenReturn(List.of());

        assertNotNull(analyticsService.getRewardsReport());
    }

    // ======================================================
    // getFilmInStock
    // ======================================================

    @Test
    void testGetFilmInStock_oneAvailableCopy() {
        Inventory inv = new Inventory(); inv.setInventoryId(1);

        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of());   // no active rentals

        Map<String, Object> result = analyticsService.getFilmInStock(1, 1);

        assertEquals(1, result.get("inStockCount"));
        assertEquals(true, result.get("isAvailable"));
    }

    @Test
    void testGetFilmInStock_inventoryRentedOut_notCounted() {
        Inventory inv = new Inventory(); inv.setInventoryId(1);

        Rental activeRental = new Rental();
        activeRental.setInventory(inv);
        activeRental.setReturnDate(null);    // still rented

        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of(activeRental));

        Map<String, Object> result = analyticsService.getFilmInStock(1, 1);

        assertEquals(0, result.get("inStockCount"));
        assertEquals(false, result.get("isAvailable"));
    }

    @Test
    void testGetFilmInStock_noInventory_returnsZero() {
        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmInStock(1, 1);

        assertEquals(0, result.get("inStockCount"));
    }

    @Test
    void testGetFilmInStock_containsExpectedKeys() {
        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmInStock(1, 1);

        assertTrue(result.containsKey("filmId"));
        assertTrue(result.containsKey("storeId"));
        assertTrue(result.containsKey("inStockCount"));
        assertTrue(result.containsKey("isAvailable"));
        assertTrue(result.containsKey("inventoryIds"));   // correct key (not "inventoryItems")
        assertTrue(result.containsKey("message"));
    }

    @Test
    void testGetFilmInStock_returnedRental_itemCountsAsInStock() {
        Inventory inv = new Inventory(); inv.setInventoryId(5);

        Rental returned = new Rental();
        returned.setInventory(inv);
        returned.setReturnDate(LocalDateTime.now());   // already returned

        when(filmRepository.existsById(2)).thenReturn(true);
        when(storeRepository.existsById(2)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(2, 2)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of(returned));

        Map<String, Object> result = analyticsService.getFilmInStock(2, 2);

        assertEquals(1, result.get("inStockCount"));
    }

    @Test
    void testGetFilmInStock_filmNotFound_throwsException() {
        when(filmRepository.existsById(99)).thenReturn(false);

        assertThrows(FilmNotFoundException.class,
                () -> analyticsService.getFilmInStock(99, 1));
    }

    @Test
    void testGetFilmInStock_storeNotFound_throwsException() {
        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(99)).thenReturn(false);

        assertThrows(StoreNotFoundException.class,
                () -> analyticsService.getFilmInStock(1, 99));
    }

    // ======================================================
    // getFilmNotInStock
    // ======================================================

    @Test
    void testGetFilmNotInStock_activeRental_countedAsNotInStock() {
        Inventory inv = new Inventory(); inv.setInventoryId(1);

        Rental r = new Rental();
        r.setInventory(inv);
        r.setReturnDate(null);   // active

        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of(r));

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        assertEquals(1, result.get("notInStockCount"));
    }

    @Test
    void testGetFilmNotInStock_noActiveRentals_returnsZero() {
        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        assertEquals(0, result.get("notInStockCount"));
    }

    @Test
    void testGetFilmNotInStock_containsExpectedKeys() {
        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of());
        when(rentalService.getAllRentals()).thenReturn(List.of());

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        assertTrue(result.containsKey("filmId"));
        assertTrue(result.containsKey("storeId"));
        assertTrue(result.containsKey("notInStockCount"));
        assertTrue(result.containsKey("inventoryIds"));   // correct key (not "inventoryItems")
        assertTrue(result.containsKey("message"));
    }

    @Test
    void testGetFilmNotInStock_returnedRental_notCounted() {
        Inventory inv = new Inventory(); inv.setInventoryId(1);

        Rental returned = new Rental();
        returned.setInventory(inv);
        returned.setReturnDate(LocalDateTime.now());   // already returned

        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of(returned));

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        assertEquals(0, result.get("notInStockCount"));
    }

    @Test
    void testGetFilmNotInStock_inventoryIdsListContainsRentedId() {
        Inventory inv = new Inventory(); inv.setInventoryId(7);

        Rental r = new Rental();
        r.setInventory(inv);
        r.setReturnDate(null);

        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(1)).thenReturn(true);
        when(inventoryService.getInventoryByStoreAndFilm(1, 1)).thenReturn(List.of(inv));
        when(rentalService.getAllRentals()).thenReturn(List.of(r));

        Map<String, Object> result = analyticsService.getFilmNotInStock(1, 1);

        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) result.get("inventoryIds");
        assertTrue(ids.contains(7));
    }

    @Test
    void testGetFilmNotInStock_filmNotFound_throwsException() {
        when(filmRepository.existsById(99)).thenReturn(false);

        assertThrows(FilmNotFoundException.class,
                () -> analyticsService.getFilmNotInStock(99, 1));
    }

    @Test
    void testGetFilmNotInStock_storeNotFound_throwsException() {
        when(filmRepository.existsById(1)).thenReturn(true);
        when(storeRepository.existsById(99)).thenReturn(false);

        assertThrows(StoreNotFoundException.class,
                () -> analyticsService.getFilmNotInStock(1, 99));
    }
}
