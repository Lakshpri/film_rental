package com.example.film_rental_app.location_store_staffmodule.service.implementation;

import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
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
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Integer staffId, Staff updated) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException(staffId));
        staff.setFirstName(updated.getFirstName());
        staff.setLastName(updated.getLastName());
        staff.setEmail(updated.getEmail());
        staff.setActive(updated.isActive());
        staff.setUsername(updated.getUsername());
        if (updated.getPassword() != null) staff.setPassword(updated.getPassword());
        if (updated.getPicture() != null) staff.setPicture(updated.getPicture());
        if (updated.getAddress() != null) staff.setAddress(updated.getAddress());
        if (updated.getStore() != null) staff.setStore(updated.getStore());
        return staffRepository.save(staff);
    }

    @Override
    public void deleteStaff(Integer staffId) {
        if (!staffRepository.existsById(staffId)) {
            throw new StaffNotFoundException(staffId);
        }
        staffRepository.deleteById(staffId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getStaffByStore(Integer storeId) {
        return staffRepository.findByStore_StoreId(storeId);
    }
}

