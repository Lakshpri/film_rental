package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.*;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.InventoryRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation.InventoryServiceImpl;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
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

        inventory = new Inventory();
        inventory.setInventoryId(1);
    }

    // ===== POSITIVE TESTS (8) =====

    @Test
    void testGetAllInventory() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));

        List<Inventory> result = inventoryService.getAllInventory();

        assertEquals(1, result.size());
    }

    @Test
    void testGetInventoryById() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));

        Inventory result = inventoryService.getInventoryById(1);

        assertNotNull(result);
    }

    @Test
    void testCreateInventory() {
        when(inventoryRepository.findByStore_StoreIdAndFilm_FilmId(any(), any()))
                .thenReturn(Collections.emptyList());
        when(inventoryRepository.save(any())).thenReturn(inventory);

        Inventory result = inventoryService.createInventory(inventory);

        assertNotNull(result);
    }

    @Test
    void testUpdateInventory() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());
        when(inventoryRepository.save(any())).thenReturn(inventory);

        Inventory updated = new Inventory();

        Inventory result = inventoryService.updateInventory(1, updated);

        assertNotNull(result);
    }

    @Test
    void testDeleteInventory() {
        when(inventoryRepository.existsById(1)).thenReturn(true);
        when(rentalRepository.findAll()).thenReturn(Collections.emptyList());

        boolean result = inventoryService.deleteInventory(1);

        assertTrue(result);
    }

    @Test
    void testGetInventoryByStore() {
        when(inventoryRepository.findByStore_StoreId(1)).thenReturn(List.of(inventory));

        List<Inventory> result = inventoryService.getInventoryByStore(1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetInventoryByFilm() {
        when(inventoryRepository.findByFilm_FilmId(1)).thenReturn(List.of(inventory));

        List<Inventory> result = inventoryService.getInventoryByFilm(1);

        assertEquals(1, result.size());
    }

    @Test
    void testGetInventoryByStoreAndFilm() {
        when(inventoryRepository.findByStore_StoreIdAndFilm_FilmId(1, 1))
                .thenReturn(List.of(inventory));

        List<Inventory> result = inventoryService.getInventoryByStoreAndFilm(1, 1);

        assertEquals(1, result.size());
    }

    // ===== NEGATIVE TESTS (7) =====

    @Test
    void testGetInventoryById_NotFound() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class,
                () -> inventoryService.getInventoryById(1));
    }

    @Test
    void testDeleteInventory_NotFound() {
        when(inventoryRepository.existsById(1)).thenReturn(false);

        assertThrows(InventoryNotFoundException.class,
                () -> inventoryService.deleteInventory(1));
    }

    @Test
    void testUpdateInventory_NotFound() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class,
                () -> inventoryService.updateInventory(1, new Inventory()));
    }

    @Test
    void testDeleteInventory_WhenRented() {
        when(inventoryRepository.existsById(1)).thenReturn(true);

        Rental rental = new Rental();
        rental.setInventory(inventory);
        rental.setReturnDate(null);

        when(rentalRepository.findAll()).thenReturn(List.of(rental));

        assertThrows(InventoryUnavailableException.class,
                () -> inventoryService.deleteInventory(1));
    }

    @Test
    void testUpdateInventory_WhenRented() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));

        Rental rental = new Rental();
        rental.setInventory(inventory);
        rental.setReturnDate(null);

        when(rentalRepository.findAll()).thenReturn(List.of(rental));

        assertThrows(InventoryUnavailableException.class,
                () -> inventoryService.updateInventory(1, new Inventory()));
    }

    @Test
    void testCreateInventory_NullFilmStore() {
        Inventory inv = new Inventory();

        when(inventoryRepository.save(any())).thenReturn(inv);

        Inventory result = inventoryService.createInventory(inv);

        assertNotNull(result);
    }
}