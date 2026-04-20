package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.*;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setActive(true);
    }

    // ================= POSITIVE TEST CASES =================

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1);

        assertEquals("John", result.getFirstName());
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertNotNull(result);
    }

    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);

        Customer updated = new Customer();
        updated.setFirstName("New");
        updated.setLastName("Name");
        updated.setEmail("new@example.com");
        updated.setActive(false);

        Customer result = customerService.updateCustomer(1, updated);

        assertEquals("New", result.getFirstName());
    }

    @Test
    void testDeleteCustomer() {
        customer.setActive(false);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        boolean result = customerService.deleteCustomer(1);

        assertTrue(result);
    }

    @Test
    void testGetCustomersByStoreAndStatus() {
        when(customerRepository.findByStoreIdAndActiveStatus(1, true))
                .thenReturn(List.of(customer));

        List<Customer> result = customerService.getCustomersByStoreAndStatus(1, true);

        assertEquals(1, result.size());
    }

    @Test
    void testSearchCustomersByName() {
        when(customerRepository.searchByName("John")).thenReturn(List.of(customer));

        List<Customer> result = customerService.searchCustomersByName("John");

        assertEquals(1, result.size());
    }

    @Test
    void testCreateCustomerWithoutEmail() {
        customer.setEmail(null);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertNotNull(result);
    }

    // ================= NEGATIVE TEST CASES =================

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerById(1));
    }

    @Test
    void testCreateCustomer_DuplicateEmail() {
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class,
                () -> customerService.createCustomer(customer));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.updateCustomer(1, customer));
    }

    @Test
    void testUpdateCustomer_DuplicateEmail() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(any())).thenReturn(true);

        Customer updated = new Customer();
        updated.setEmail("duplicate@example.com");

        assertThrows(CustomerAlreadyExistsException.class,
                () -> customerService.updateCustomer(1, updated));
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.deleteCustomer(1));
    }

    @Test
    void testDeleteCustomer_ActiveCustomer() {
        customer.setActive(true);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        assertThrows(CustomerInvalidOperationException.class,
                () -> customerService.deleteCustomer(1));
    }

    @Test
    void testUpdateCustomer_NullEmailSafe() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer updated = new Customer();
        updated.setEmail(null);

        Customer result = customerService.updateCustomer(1, updated);

        assertNotNull(result);
    }
}