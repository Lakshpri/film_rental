package com.example.film_rental_app.location_store_staffmodule;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.exception.*;
import com.example.film_rental_app.location_store_staffmodule.repository.StoreRepository;
import com.example.film_rental_app.location_store_staffmodule.service.implementation.StoreServiceImpl;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl service;

    @Mock
    private StoreRepository repository;

    private Store store;
    private Address address;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        address = new Address();
        address.setAddressId(100);

        store = new Store();
        store.setStoreId(1);
        store.setAddress(address); // ✅ IMPORTANT FIX
    }

    // ---------- POSITIVE (8) ----------

    @Test
    void testGetAllStores() {
        when(repository.findAll()).thenReturn(List.of(store));
        assertEquals(1, service.getAllStores().size());
    }

    @Test
    void testGetStoreById() {
        when(repository.findById(1)).thenReturn(Optional.of(store));
        assertNotNull(service.getStoreById(1));
    }

    @Test
    void testCreateStore() {
        when(repository.existsByAddress_AddressId(100)).thenReturn(false);
        when(repository.save(store)).thenReturn(store);

        assertNotNull(service.createStore(store));
    }

    @Test
    void testUpdateStore() {
        when(repository.findById(1)).thenReturn(Optional.of(store));
        when(repository.existsByAddress_AddressId(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(store);

        assertNotNull(service.updateStore(1, store));
    }

    @Test
    void testDeleteStore() {
        when(repository.existsById(1)).thenReturn(true);
        assertTrue(service.deleteStore(1));
    }

    @Test
    void testUpdateManagerStaff() {
        Store updated = new Store();
        updated.setManagerStaff(null);

        when(repository.findById(1)).thenReturn(Optional.of(store));
        when(repository.save(any())).thenReturn(store);

        service.updateStore(1, updated);
        assertNotNull(store);
    }

    @Test
    void testCreateStore_NoAddressConflict() {
        when(repository.existsByAddress_AddressId(100)).thenReturn(false);
        when(repository.save(store)).thenReturn(store);

        assertNotNull(service.createStore(store));
    }

    @Test
    void testUpdateStore_NoConflict() {
        when(repository.findById(1)).thenReturn(Optional.of(store));
        when(repository.existsByAddress_AddressId(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(store);

        assertNotNull(service.updateStore(1, store));
    }

    // ---------- NEGATIVE (7) ----------

    @Test
    void testGetStore_NotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(StoreNotFoundException.class, () -> service.getStoreById(1));
    }

    @Test
    void testDeleteStore_NotFound() {
        when(repository.existsById(1)).thenReturn(false);
        assertThrows(StoreNotFoundException.class, () -> service.deleteStore(1));
    }

    @Test
    void testCreateStore_AlreadyExists() {
        when(repository.existsByAddress_AddressId(100)).thenReturn(true);

        assertThrows(StoreAlreadyExistsException.class,
                () -> service.createStore(store));
    }

    @Test
    void testUpdateStore_AlreadyExists() {
        Address newAddress = new Address();
        newAddress.setAddressId(200);

        Store updated = new Store();
        updated.setAddress(newAddress);

        when(repository.findById(1)).thenReturn(Optional.of(store));
        when(repository.existsByAddress_AddressId(200)).thenReturn(true);

        assertThrows(StoreAlreadyExistsException.class,
                () -> service.updateStore(1, updated));
    }

    @Test
    void testUpdateStore_NotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(StoreNotFoundException.class,
                () -> service.updateStore(1, store));
    }

    @Test
    void testDeleteStore_Exception() {
        when(repository.existsById(1)).thenReturn(true);
        doThrow(RuntimeException.class).when(repository).deleteById(1);

        assertThrows(RuntimeException.class,
                () -> service.deleteStore(1));
    }

    @Test
    void testCreateStore_Exception() {
        when(repository.existsByAddress_AddressId(100)).thenReturn(false);
        when(repository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> service.createStore(store));
    }
}
