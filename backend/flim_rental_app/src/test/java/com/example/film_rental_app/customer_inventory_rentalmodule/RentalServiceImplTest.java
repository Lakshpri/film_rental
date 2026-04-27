package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.*;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation.RentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalServiceImplTest {

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Rental rental;
    private Inventory inventory;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Common test data
        inventory = new Inventory();
        inventory.setInventoryId(1);

        customer = new Customer();
        customer.setCustomerId(1);

        rental = new Rental();
        rental.setRentalId(1);
        rental.setInventory(inventory);
        rental.setCustomer(customer);
        rental.setRentalDate(LocalDateTime.now());
    }

    // 1. Get all rentals
    @Test
    void testGetAllRentals() {
        when(rentalRepository.findAll()).thenReturn(List.of(rental));

        List<Rental> result = rentalService.getAllRentals();

        assertEquals(1, result.size());
    }

    // 2. Get rental by ID
    @Test
    void testGetRentalById() {
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        Rental result = rentalService.getRentalById(1);

        assertNotNull(result);
    }

    // 3. Create rental
    @Test
    void testCreateRental() {
        when(rentalRepository.findByCustomer_CustomerId(1)).thenReturn(Collections.emptyList());
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());
        when(rentalRepository.save(any())).thenReturn(rental);

        Rental result = rentalService.createRental(rental);

        assertNotNull(result);
    }

    // 4. Update rental (return item)
    @Test
    void testUpdateRental() {
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);

        Rental updated = new Rental();
        updated.setReturnDate(LocalDateTime.now());

        Rental result = rentalService.updateRental(1, updated);

        assertNotNull(result);
    }

    // 5. Delete rental (only if returned)
    @Test
    void testDeleteRental() {
        rental.setReturnDate(LocalDateTime.now());

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        boolean result = rentalService.deleteRental(1);

        assertTrue(result);
    }

    // 6. Get rentals by customer
    @Test
    void testGetRentalsByCustomer() {
        when(customerRepository.existsById(1)).thenReturn(true);
        when(rentalRepository.findByCustomer_CustomerId(1)).thenReturn(List.of(rental));

        List<Rental> result = rentalService.getRentalsByCustomer(1);

        assertEquals(1, result.size());
    }

    // 7. Rental not found
    @Test
    void testRentalNotFound() {
        when(rentalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> rentalService.getRentalById(1));
    }

    // 8. Delete active rental (should fail)
    @Test
    void testDeleteActiveRental() {
        rental.setReturnDate(null);

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        assertThrows(RuntimeException.class,
                () -> rentalService.deleteRental(1));
    }
}