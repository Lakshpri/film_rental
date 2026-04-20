package com.example.film_rental_app.location_store_staffmodule;

import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.exception.*;
import com.example.film_rental_app.location_store_staffmodule.repository.StaffRepository;
import com.example.film_rental_app.location_store_staffmodule.service.implementation.StaffServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffServiceImplTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private StaffRepository staffRepository;

    private Staff staff;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        staff = new Staff();
        staff.setStaffId(1);
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setUsername("john123");  // ✅ REQUIRED
        staff.setActive(false);        // ✅ REQUIRED for delete
    }

    // =========================
    // ✅ POSITIVE TESTS (8)
    // =========================

    @Test
    void testGetAllStaff() {
        when(staffRepository.findAll()).thenReturn(List.of(staff));
        assertEquals(1, staffService.getAllStaff().size());
    }

    @Test
    void testGetStaffById() {
        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        assertEquals("John", staffService.getStaffById(1).getFirstName());
    }

    @Test
    void testCreateStaff_Success() {
        when(staffRepository.existsByUsername("john123")).thenReturn(false);
        when(staffRepository.save(staff)).thenReturn(staff);

        assertNotNull(staffService.createStaff(staff));
    }

    @Test
    void testUpdateStaff_Success() {
        Staff updated = new Staff();
        updated.setUsername("john123"); // same username → no duplicate check
        updated.setFirstName("Updated");

        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        when(staffRepository.save(any())).thenReturn(staff);

        Staff result = staffService.updateStaff(1, updated);
        assertEquals("Updated", result.getFirstName());
    }

    @Test
    void testDeleteStaff_Success() {
        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        doNothing().when(staffRepository).deleteById(1);

        assertTrue(staffService.deleteStaff(1));
    }

    @Test
    void testGetStaffByStore() {
        when(staffRepository.findByStore_StoreId(1)).thenReturn(List.of(staff));
        assertEquals(1, staffService.getStaffByStore(1).size());
    }

    @Test
    void testGetAllStaff_EmptyList() {
        when(staffRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(staffService.getAllStaff().isEmpty());
    }

    @Test
    void testCreateStaff_DifferentUser() {
        Staff newStaff = new Staff();
        newStaff.setUsername("newuser");

        when(staffRepository.existsByUsername("newuser")).thenReturn(false);
        when(staffRepository.save(any())).thenReturn(newStaff);

        assertNotNull(staffService.createStaff(newStaff));
    }

    // =========================
    // ❌ NEGATIVE TESTS (7)
    // =========================

    @Test
    void testGetStaffById_NotFound() {
        when(staffRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(StaffNotFoundException.class,
                () -> staffService.getStaffById(1));
    }

    @Test
    void testCreateStaff_DuplicateUsername() {
        when(staffRepository.existsByUsername("john123")).thenReturn(true);

        assertThrows(StaffAlreadyExistsException.class,
                () -> staffService.createStaff(staff));
    }

    @Test
    void testUpdateStaff_NotFound() {
        when(staffRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class,
                () -> staffService.updateStaff(1, staff));
    }

    @Test
    void testUpdateStaff_DuplicateUsername() {
        Staff updated = new Staff();
        updated.setUsername("newuser");

        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        when(staffRepository.existsByUsername("newuser")).thenReturn(true);

        assertThrows(StaffAlreadyExistsException.class,
                () -> staffService.updateStaff(1, updated));
    }

    @Test
    void testDeleteStaff_NotFound() {
        when(staffRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class,
                () -> staffService.deleteStaff(1));
    }

    @Test
    void testDeleteStaff_ActiveUser() {
        staff.setActive(true);

        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));

        assertThrows(StaffInvalidOperationException.class,
                () -> staffService.deleteStaff(1));
    }

    @Test
    void testCreateStaff_SaveThrowsException() {
        when(staffRepository.existsByUsername("john123")).thenReturn(false);
        when(staffRepository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> staffService.createStaff(staff));
    }
}