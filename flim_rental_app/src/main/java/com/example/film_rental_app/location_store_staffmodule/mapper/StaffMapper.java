package com.example.film_rental_app.location_store_staffmodule.mapper;


import com.example.film_rental_app.location_store_staffmodule.dto.request.StaffRequestDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StaffResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {

    public Staff toEntity(StaffRequestDTO dto) {
        Staff staff = new Staff();
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setEmail(dto.getEmail());
        staff.setActive(dto.isActive());
        staff.setUsername(dto.getUsername());
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
        staff.setEmail(dto.getEmail());
        staff.setActive(dto.isActive());
        staff.setUsername(dto.getUsername());
        if (dto.getPassword() != null) staff.setPassword(dto.getPassword());
    }
}

