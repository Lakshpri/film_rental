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
    // POSITIVE TESTS
    // =========================


    @Test
    void testGetStaffById() {
        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        assertEquals("John", staffService.getStaffById(1).getFirstName());
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



    // =========================
    // NEGATIVE TESTS
    // =========================

    @Test
    void testGetStaffById_NotFound() {
        when(staffRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(StaffNotFoundException.class,
                () -> staffService.getStaffById(1));
    }



    @Test
    void testUpdateStaff_NotFound() {
        when(staffRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(StaffNotFoundException.class,
                () -> staffService.updateStaff(1, staff));
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


}