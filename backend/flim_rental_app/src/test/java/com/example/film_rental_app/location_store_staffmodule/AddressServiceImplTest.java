package com.example.film_rental_app.location_store_staffmodule;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.exception.AddressNotFoundException;
import com.example.film_rental_app.location_store_staffmodule.repository.AddressRepository;
import com.example.film_rental_app.location_store_staffmodule.service.implementation.AddressServiceImpl;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.common.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl service;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CityRepository cityRepository;

    private Address address;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        address = new Address();
        address.setAddressId(1);
        address.setAddress("Test Address");
    }

    // ---------- POSITIVE TESTS (8) ----------

    @Test
    void testGetAllAddresses() {
        when(addressRepository.findAll()).thenReturn(List.of(address));
        assertEquals(1, service.getAllAddresses().size());
    }

    @Test
    void testGetAddressById() {
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        assertEquals("Test Address", service.getAddressById(1).getAddress());
    }

    @Test
    void testCreateAddress() {
        when(addressRepository.save(address)).thenReturn(address);
        assertNotNull(service.createAddress(address));
    }

    @Test
    void testUpdateAddress() {
        Address updated = new Address();
        updated.setAddress("Updated");

        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        when(addressRepository.save(any())).thenReturn(address);

        Address result = service.updateAddress(1, updated);
        assertEquals("Updated", result.getAddress());
    }

    @Test
    void testDeleteAddress() {
        when(addressRepository.existsById(1)).thenReturn(true);
        assertTrue(service.deleteAddress(1));
    }

    @Test
    void testGetAddressesByCity() {
        when(cityRepository.existsById(1)).thenReturn(true);
        when(addressRepository.findByCity_CityId(1)).thenReturn(List.of(address));

        assertEquals(1, service.getAddressesByCity(1).size());
    }

    @Test
    void testUpdatePartialFields() {
        Address updated = new Address();
        updated.setPhone("123");

        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        when(addressRepository.save(any())).thenReturn(address);

        service.updateAddress(1, updated);
        assertEquals("123", address.getPhone());
    }

    @Test
    void testCreateAddressNullSafe() {
        when(addressRepository.save(any())).thenReturn(address);
        assertNotNull(service.createAddress(new Address()));
    }

    // ---------- NEGATIVE TESTS (7) ----------

    @Test
    void testGetAddressById_NotFound() {
        when(addressRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class, () -> service.getAddressById(1));
    }

    @Test
    void testUpdateAddress_NotFound() {
        when(addressRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(AddressNotFoundException.class, () -> service.updateAddress(1, new Address()));
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressRepository.existsById(1)).thenReturn(false);
        assertThrows(AddressNotFoundException.class, () -> service.deleteAddress(1));
    }

    @Test
    void testGetAddressesByCity_NotFound() {
        when(cityRepository.existsById(1)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.getAddressesByCity(1));
    }

    @Test
    void testUpdateAddress_NullInput() {
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        assertThrows(NullPointerException.class, () -> service.updateAddress(1, null));
    }

    @Test
    void testCreateAddress_Exception() {
        when(addressRepository.save(any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> service.createAddress(address));
    }

    @Test
    void testDeleteAddress_Exception() {
        when(addressRepository.existsById(1)).thenReturn(true);
        doThrow(RuntimeException.class).when(addressRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> service.deleteAddress(1));
    }
}