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
        // ResourceNotFoundException → HTTP 404
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException(staffId));
    }

    @Override
    public Staff createStaff(Staff staff) {
        // DuplicateResourceException → HTTP 409
        if (staffRepository.existsByUsername(staff.getUsername())) {
            throw new StaffAlreadyExistsException(staff.getUsername());
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
        // ResourceNotFoundException → HTTP 404
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException(staffId));
        if (staff.isActive()) {
            throw new StaffInvalidOperationException(staffId,
                    "To delete this staff member, you must first set them as inactive. Go to Edit Staff and turn off the Active status, then try deleting again.");
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
