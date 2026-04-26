package com.example.film_rental_app.location_store_staffmodule.service.implementation;

import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.exception.*;
import com.example.film_rental_app.location_store_staffmodule.repository.AddressRepository;
import com.example.film_rental_app.location_store_staffmodule.repository.StaffRepository;
import com.example.film_rental_app.location_store_staffmodule.repository.StoreRepository;
import com.example.film_rental_app.location_store_staffmodule.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private StoreRepository storeRepository;


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
        if (staffRepository.existsByUsernameIgnoreCase(staff.getUsername().trim())) {
            throw new StaffAlreadyExistsException(staff.getUsername());
        }
        if (staff.getEmail() != null && staffRepository.existsByEmailIgnoreCase(staff.getEmail().trim())) {
            throw new StaffAlreadyExistsException("email", staff.getEmail());
        }
        if (!addressRepository.existsById(staff.getAddress().getAddressId())) {
            throw new AddressNotFoundException(staff.getAddress().getAddressId());
        }

        if (!storeRepository.existsById(staff.getStore().getStoreId())) {
            throw new StoreNotFoundException(staff.getStore().getStoreId());
        }
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Integer staffId, Staff updated) {

        // 1. Fetch existing staff
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException(staffId));

        // 2. Validate foreign keys (NEW FIX)
        if (updated.getAddress() != null &&
                !addressRepository.existsById(updated.getAddress().getAddressId())) {
            throw new AddressNotFoundException(updated.getAddress().getAddressId());
        }

        if (updated.getStore() != null &&
                !storeRepository.existsById(updated.getStore().getStoreId())) {
            throw new StoreNotFoundException(updated.getStore().getStoreId());
        }

        // 3. Duplicate checks

        if (updated.getUsername() != null &&
                !staff.getUsername().equalsIgnoreCase(updated.getUsername()) &&
                staffRepository.existsByUsernameIgnoreCase(updated.getUsername().trim())) {
            throw new StaffAlreadyExistsException(updated.getUsername());
        }

        if (updated.getEmail() != null &&
                (staff.getEmail() == null || !staff.getEmail().equalsIgnoreCase(updated.getEmail())) &&
                staffRepository.existsByEmailIgnoreCase(updated.getEmail().trim())) {
            throw new StaffAlreadyExistsException("email", updated.getEmail());
        }

        // 4. Update fields
        staff.setFirstName(updated.getFirstName());
        staff.setLastName(updated.getLastName());
        staff.setEmail(updated.getEmail() != null ? updated.getEmail().trim() : null);
        staff.setUsername(updated.getUsername().trim());

        staff.setActive(updated.isActive());

        if (updated.getPassword() != null) staff.setPassword(updated.getPassword());
        if (updated.getPicture() != null) staff.setPicture(updated.getPicture());
        if (updated.getAddress() != null) staff.setAddress(updated.getAddress());
        if (updated.getStore() != null) staff.setStore(updated.getStore());

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
