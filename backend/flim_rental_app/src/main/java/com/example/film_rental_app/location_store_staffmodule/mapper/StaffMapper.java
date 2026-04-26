package com.example.film_rental_app.location_store_staffmodule.mapper;

import com.example.film_rental_app.location_store_staffmodule.dto.request.StaffRequestDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StaffResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class StaffMapper {

    public Staff toEntity(StaffRequestDTO dto) {
        Staff staff = new Staff();
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setActive(dto.isActive());
        staff.setUsername(dto.getUsername().trim());
        staff.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        if (dto.getPicture() != null && !dto.getPicture().isEmpty()) {
            try {
                staff.setPicture(dto.getPicture().getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Error processing image");
            }
        }
        staff.setPassword(dto.getPassword());
        Address address = new Address();
        address.setAddressId(dto.getAddressId());
        staff.setAddress(address);
        Store store = new Store();
        store.setStoreId(dto.getStoreId());
        staff.setStore(store);
        return staff;
    }

    public StaffResponseDTO toResponseDTO(Staff staff) {
        StaffResponseDTO dto = new StaffResponseDTO();
        dto.setStaffId(staff.getStaffId());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setEmail(staff.getEmail());
        if (staff.getPicture() != null) {
            dto.setPicture(Base64.getEncoder().encodeToString(staff.getPicture()));
        }
        dto.setActive(staff.isActive());
        dto.setUsername(staff.getUsername());
        dto.setLastUpdate(staff.getLastUpdate());
        if (staff.getAddress() != null) {
            dto.setAddressId(staff.getAddress().getAddressId());
            dto.setAddressLine(staff.getAddress().getAddress());
        }
        if (staff.getStore() != null) {
            dto.setStoreId(staff.getStore().getStoreId());
        }
        return dto;
    }

    public void updateEntity(Staff staff, StaffRequestDTO dto) {
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setUsername(dto.getUsername().trim());
        staff.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        staff.setActive(dto.isActive());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            staff.setPassword(dto.getPassword());
        }
        // Only overwrite the stored picture when a new file is actually uploaded
        if (dto.getPicture() != null && !dto.getPicture().isEmpty()) {
            try {
                staff.setPicture(dto.getPicture().getBytes());
            } catch (Exception e) {
                throw new RuntimeException("Error processing image");
            }
        }
    }
}