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

        // Common test data
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setActive(true);
    }

    // 1. Get all customers
    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        assertEquals(1, customerService.getAllCustomers().size());
    }

    // 2. Get customer by ID
    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        assertEquals("John", customerService.getCustomerById(1).getFirstName());
    }

    // 3. Create customer (success)
    @Test
    void testCreateCustomer() {
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);
        assertNotNull(customerService.createCustomer(customer));
    }

    // 4. Create customer (duplicate email)
    @Test
    void testCreateCustomerDuplicateEmail() {
        when(customerRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(CustomerAlreadyExistsException.class,
                () -> customerService.createCustomer(customer));
    }

    // 5. Update customer
    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(any())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);

        Customer updated = new Customer();
        updated.setFirstName("New");

        assertEquals("New", customerService.updateCustomer(1, updated).getFirstName());
    }

    // 6. Delete customer (inactive)
    @Test
    void testDeleteCustomer() {
        customer.setActive(false);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        assertTrue(customerService.deleteCustomer(1));
    }

    // 7. Delete customer (active → error)
    @Test
    void testDeleteActiveCustomer() {
        customer.setActive(true);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        assertThrows(CustomerInvalidOperationException.class,
                () -> customerService.deleteCustomer(1));
    }

    // 8. Customer not found
    @Test
    void testCustomerNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerById(1));
    }

    // 9. Filter by store and status
    @Test
    void testGetByStoreAndStatus() {
        when(customerRepository.findByStoreIdAndActiveStatus(1, true))
                .thenReturn(List.of(customer));

        assertEquals(1,
                customerService.getCustomersByStoreAndStatus(1, true).size());
    }

    // 10. Search by name
    @Test
    void testSearchCustomers() {
        when(customerRepository.searchByName("John"))
                .thenReturn(List.of(customer));

        assertEquals(1,
                customerService.searchCustomersByName("John").size());
    }
}