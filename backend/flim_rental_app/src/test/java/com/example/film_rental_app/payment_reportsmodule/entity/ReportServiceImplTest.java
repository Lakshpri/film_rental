package com.example.film_rental_app.payment_reportsmodule.entity;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.payment_reportsmodule.service.implementation.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private ReportServiceImpl reportService;

    // -------- POSITIVE --------

    @Test
    void testGetCustomerList() {
        Customer c1 = new Customer();
        Customer c2 = new Customer();

        when(customerService.getAllCustomers()).thenReturn(List.of(c1, c2));

        Map<String, Object> result = reportService.getCustomerList();

        assertEquals(2, ((List<?>) result.get("customers")).size());
    }

    @Test
    void testGetFilmList() {
        Map<String, Object> result = reportService.getFilmList();
        assertEquals("Film list data", result.get("films"));
    }

    @Test
    void testGetStaffList() {
        Map<String, Object> result = reportService.getStaffList();
        assertEquals("Staff list data", result.get("staff"));
    }

    @Test
    void testGetSalesByStore() {
        Map<String, Object> result = reportService.getSalesByStore();
        assertEquals("Sales by store data", result.get("salesByStore"));
    }

    @Test
    void testGetSalesByCategory() {
        Map<String, Object> result = reportService.getSalesByCategory();
        assertEquals("Sales by category data", result.get("salesByCategory"));
    }

    @Test
    void testGetActorInfo() {
        Map<String, Object> result = reportService.getActorInfo();
        assertEquals("Actor info data", result.get("actors"));
    }

    @Test
    void testCustomerListNotNull() {
        Customer c = new Customer();

        when(customerService.getAllCustomers()).thenReturn(List.of(c));

        Map<String, Object> result = reportService.getCustomerList();

        assertNotNull(result);
    }

    @Test
    void testFilmListContainsKey() {
        Map<String, Object> result = reportService.getFilmList();
        assertTrue(result.containsKey("films"));
    }

    // -------- NEGATIVE / EDGE --------

    @Test
    void testCustomerListEmpty() {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        Map<String, Object> result = reportService.getCustomerList();

        assertTrue(((List<?>) result.get("customers")).isEmpty());
    }

    @Test
    void testCustomerListNull() {
        when(customerService.getAllCustomers()).thenReturn(null);

        Map<String, Object> result = reportService.getCustomerList();

        assertNull(result.get("customers"));
    }

    @Test
    void testFilmListNotNull() {
        Map<String, Object> result = reportService.getFilmList();
        assertNotNull(result.get("films"));
    }

    @Test
    void testStaffListKeyExists() {
        Map<String, Object> result = reportService.getStaffList();
        assertTrue(result.containsKey("staff"));
    }

    @Test
    void testSalesByStoreKeyExists() {
        Map<String, Object> result = reportService.getSalesByStore();
        assertTrue(result.containsKey("salesByStore"));
    }

    @Test
    void testSalesByCategoryKeyExists() {
        Map<String, Object> result = reportService.getSalesByCategory();
        assertTrue(result.containsKey("salesByCategory"));
    }

    @Test
    void testActorInfoKeyExists() {
        Map<String, Object> result = reportService.getActorInfo();
        assertTrue(result.containsKey("actors"));
    }
}