package com.example.film_rental_app.location_store_staffmodule.service.implementation;

import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.exception.StaffAlreadyExistsException;
import com.example.film_rental_app.location_store_staffmodule.exception.StaffInvalidOperationException;
import com.example.film_rental_app.location_store_staffmodule.exception.StaffNotFoundException;
import com.example.film_rental_app.location_store_staffmodule.repository.StaffRepository;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Staff getStaffById(Integer staffId) {
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException(staffId));
    }

    @Override
    public Staff createStaff(Staff staff) {
        // Check username uniqueness
        if (staffRepository.existsByUsername(staff.getUsername())) {
            throw new StaffAlreadyExistsException(staff.getUsername());
        }
        // FIX 4: Check email uniqueness on create
        if (staff.getEmail() != null && !staff.getEmail().isBlank()
                && staffRepository.existsByEmail(staff.getEmail())) {
            throw new StaffAlreadyExistsException("email", staff.getEmail());
        }
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Integer staffId, Staff updated) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException(staffId));
        if (!staff.getUsername().equalsIgnoreCase(updated.getUsername())
                && staffRepository.existsByUsername(updated.getUsername())) {
            throw new StaffAlreadyExistsException(updated.getUsername());
        }
        // FIX 4: Check email uniqueness on update (exclude current staff)
        if (updated.getEmail() != null && !updated.getEmail().isBlank()
                && staffRepository.existsByEmailAndStaffIdNot(updated.getEmail(), staffId)) {
            throw new StaffAlreadyExistsException("email", updated.getEmail());
        }
        staff.setFirstName(updated.getFirstName());
        staff.setLastName(updated.getLastName());
        staff.setEmail(updated.getEmail());
        staff.setActive(updated.isActive());
        staff.setUsername(updated.getUsername());
        if (updated.getPassword() != null) staff.setPassword(updated.getPassword());
        if (updated.getPicture()  != null) staff.setPicture(updated.getPicture());
        if (updated.getAddress()  != null) staff.setAddress(updated.getAddress());
        if (updated.getStore()    != null) staff.setStore(updated.getStore());
        return staffRepository.save(staff);
    }

    @Override
    public boolean deleteStaff(Integer staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException(staffId));
        if (staff.isActive()) {
            throw new StaffInvalidOperationException(staffId,
                    "You cannot delete an active Staff member. Deactivate them first before deletion.");
        }
        staffRepository.deleteById(staffId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getStaffByStore(Integer storeId) {
        return staffRepository.findByStore_StoreId(storeId);
    }
}
