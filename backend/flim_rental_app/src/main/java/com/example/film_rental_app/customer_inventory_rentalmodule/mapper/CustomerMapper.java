package com.example.film_rental_app.customer_inventory_rentalmodule.mapper;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.CustomerRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.CustomerResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.stereotype.Component;

// Marks this class as Spring-managed component
@Component
public class CustomerMapper {

    // Converts RequestDTO to Entity
    public Customer toEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();

        // Copy basic fields
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setActive(dto.isActive());

        // Set Store using only ID
        Store store = new Store();
        store.setStoreId(dto.getStoreId());
        customer.setStore(store);

        // Set Address using only ID
        Address address = new Address();
        address.setAddressId(dto.getAddressId());
        customer.setAddress(address);

        return customer;
    }

    // Converts Entity to ResponseDTO
    public CustomerResponseDTO toResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();

        // Copy basic fields
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setActive(customer.isActive());

        // Copy timestamps
        dto.setCreateDate(customer.getCreateDate());
        dto.setLastUpdate(customer.getLastUpdate());

        // Extract Store ID safely
        if (customer.getStore() != null) {
            dto.setStoreId(customer.getStore().getStoreId());
        }

        // Extract Address details safely
        if (customer.getAddress() != null) {
            dto.setAddressId(customer.getAddress().getAddressId());
            dto.setAddressLine(customer.getAddress().getAddress());
        }

        return dto;
    }

    // Updates existing entity using DTO
    public void updateEntity(Customer customer, CustomerRequestDTO dto) {
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setActive(dto.isActive());
    }
}