package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.InventoryRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private RentalRepository rentalRepository;

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Common test data
        inventory = new Inventory();
        inventory.setInventoryId(1);
    }

    // 1. Get all inventory
    @Test
    void testGetAllInventory() {
        // Mock repository response
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));

        // Call service
        List<Inventory> result = inventoryService.getAllInventory();

        // Verify result size
        assertEquals(1, result.size());
    }

    // 2. Get inventory by ID
    @Test
    void testGetInventoryById() {
        // Mock repository response
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));

        // Call service
        Inventory result = inventoryService.getInventoryById(1);

        // Verify result is not null
        assertNotNull(result);
    }

    // 3. Create inventory
    @Test
    void testCreateInventory() {
        // Mock save operation
        when(inventoryRepository.save(any())).thenReturn(inventory);

        // Call service
        Inventory result = inventoryService.createInventory(inventory);

        // Verify creation
        assertNotNull(result);
    }

    // 4. Update inventory
    @Test
    void testUpdateInventory() {
        // Mock existing inventory and no active rentals
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());
        when(inventoryRepository.save(any())).thenReturn(inventory);

        // Call service
        Inventory result = inventoryService.updateInventory(1, new Inventory());

        // Verify update
        assertNotNull(result);
    }

    // 5. Delete inventory
    @Test
    void testDeleteInventory() {
        // Mock inventory exists and no active rentals
        when(inventoryRepository.existsById(1)).thenReturn(true);
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());

        // Call service
        boolean result = inventoryService.deleteInventory(1);

        // Verify deletion success
        assertTrue(result);
    }

    // 6. Get inventory by store
    @Test
    void testGetInventoryByStore() {
        // Mock repository response
        when(inventoryRepository.findByStore_StoreId(1)).thenReturn(List.of(inventory));

        // Call service
        List<Inventory> result = inventoryService.getInventoryByStore(1);

        // Verify result
        assertEquals(1, result.size());
    }

    // 7. Get inventory by film
    @Test
    void testGetInventoryByFilm() {
        // Mock repository response
        when(inventoryRepository.findByFilm_FilmId(1)).thenReturn(List.of(inventory));

        // Call service
        List<Inventory> result = inventoryService.getInventoryByFilm(1);

        // Verify result
        assertEquals(1, result.size());
    }

    // 8. Get inventory by store and film
    @Test
    void testGetInventoryByStoreAndFilm() {
        // Mock repository response
        when(inventoryRepository.findByStore_StoreIdAndFilm_FilmId(1, 1))
                .thenReturn(List.of(inventory));

        // Call service
        List<Inventory> result = inventoryService.getInventoryByStoreAndFilm(1, 1);

        // Verify result
        assertEquals(1, result.size());
    }
}