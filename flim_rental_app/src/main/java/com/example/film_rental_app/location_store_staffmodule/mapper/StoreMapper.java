package com.example.film_rental_app.location_store_staffmodule.mapper;

import com.example.film_rental_app.location_store_staffmodule.dto.request.StoreRequestDTO;
import com.example.film_rental_app.location_store_staffmodule.dto.response.StoreResponseDTO;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public Store toEntity(StoreRequestDTO dto) {
        Store store = new Store();
        Staff manager = new Staff();
        manager.setStaffId(dto.getManagerStaffId());
        store.setManagerStaff(manager);
        Address address = new Address();
        address.setAddressId(dto.getAddressId());
        store.setAddress(address);
        return store;
    }

    public void updateEntity(Store store, StoreRequestDTO dto) {
        if (dto.getManagerStaffId() != null) {
            Staff manager = new Staff();
            manager.setStaffId(dto.getManagerStaffId());
            store.setManagerStaff(manager);
        }
        if (dto.getAddressId() != null) {
            Address address = new Address();
            address.setAddressId(dto.getAddressId());
            store.setAddress(address);
        }
    }

    public StoreResponseDTO toResponseDTO(Store store) {
        StoreResponseDTO dto = new StoreResponseDTO();
        dto.setStoreId(store.getStoreId());
        dto.setLastUpdate(store.getLastUpdate());
        if (store.getManagerStaff() != null) {
            dto.setManagerStaffId(store.getManagerStaff().getStaffId());
            dto.setManagerFullName(store.getManagerStaff().getFirstName() + " " + store.getManagerStaff().getLastName());
        }
        if (store.getAddress() != null) {
            dto.setAddressId(store.getAddress().getAddressId());
            dto.setAddressLine(store.getAddress().getAddress());
            if (store.getAddress().getCity() != null) {
                dto.setCity(store.getAddress().getCity().getCity());
                if (store.getAddress().getCity().getCountry() != null) {
                    dto.setCountry(store.getAddress().getCity().getCountry().getCountry());
                }
            }
        }
        return dto;
    }
}

