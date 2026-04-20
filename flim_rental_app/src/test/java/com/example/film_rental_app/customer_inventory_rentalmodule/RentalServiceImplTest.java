package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.*;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.*;
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

    // ===== POSITIVE TESTS (8) =====

    @Test
    void testGetAllRentals() {
        when(rentalRepository.findAll()).thenReturn(List.of(rental));

        List<Rental> result = rentalService.getAllRentals();

        assertEquals(1, result.size());
    }

    @Test
    void testGetRentalById() {
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        Rental result = rentalService.getRentalById(1);

        assertNotNull(result);
    }

    @Test
    void testCreateRental() {
        when(rentalRepository.findByCustomer_CustomerId(1)).thenReturn(Collections.emptyList());
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());
        when(rentalRepository.save(any())).thenReturn(rental);

        Rental result = rentalService.createRental(rental);

        assertNotNull(result);
    }

    @Test
    void testUpdateRental_ReturnItem() {
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);

        Rental updated = new Rental();
        updated.setReturnDate(LocalDateTime.now());

        Rental result = rentalService.updateRental(1, updated);

        assertNotNull(result);
    }

    @Test
    void testDeleteRental() {
        rental.setReturnDate(LocalDateTime.now());

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        boolean result = rentalService.deleteRental(1);

        assertTrue(result);
    }

    @Test
    void testGetRentalsByCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1);

        Rental rental = new Rental();
        rental.setCustomer(customer);

        when(customerRepository.existsById(1)).thenReturn(true);
        when(rentalRepository.findByCustomer_CustomerId(1)).thenReturn(List.of(rental));

        List<Rental> result = rentalService.getRentalsByCustomer(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateRental_WithNewRentalDate() {
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any())).thenReturn(rental);

        Rental updated = new Rental();
        updated.setRentalDate(LocalDateTime.now().minusDays(1));

        Rental result = rentalService.updateRental(1, updated);

        assertNotNull(result);
    }

    @Test
    void testCreateRental_WithNullCustomerInventory() {
        when(rentalRepository.save(any())).thenReturn(new Rental());

        Rental result = rentalService.createRental(new Rental());

        assertNotNull(result);
    }

    // ===== NEGATIVE TESTS (7) =====

    @Test
    void testGetRentalById_NotFound() {
        when(rentalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.getRentalById(1));
    }

    @Test
    void testUpdateRental_NotFound() {
        when(rentalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.updateRental(1, new Rental()));
    }

    @Test
    void testDeleteRental_NotFound() {
        when(rentalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RentalNotFoundException.class,
                () -> rentalService.deleteRental(1));
    }

    @Test
    void testDeleteRental_ActiveRental() {
        rental.setReturnDate(null);

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        assertThrows(RentalInvalidOperationException.class,
                () -> rentalService.deleteRental(1));
    }

    @Test
    void testCreateRental_InventoryUnavailable() {
        when(rentalRepository.findAll()).thenReturn(List.of(rental));

        Rental newRental = new Rental();
        newRental.setInventory(inventory);
        newRental.setCustomer(customer);

        assertThrows(InventoryUnavailableException.class,
                () -> rentalService.createRental(newRental));
    }

    @Test
    void testCreateRental_DuplicateForCustomer() {
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());
        when(rentalRepository.findByCustomer_CustomerId(1)).thenReturn(List.of(rental));

        Rental duplicate = new Rental();
        duplicate.setInventory(inventory);
        duplicate.setCustomer(customer);

        assertThrows(RentalAlreadyExistsException.class,
                () -> rentalService.createRental(duplicate));
    }

    @Test
    void testUpdateRental_AlreadyReturnedModification() {
        rental.setReturnDate(LocalDateTime.now());

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        Rental updated = new Rental();
        updated.setReturnDate(LocalDateTime.now().plusDays(1));

        assertThrows(RentalInvalidOperationException.class,
                () -> rentalService.updateRental(1, updated));
    }
}