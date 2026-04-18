package com.example.film_rental_app.customer_inventory_rentalmodule.mapper;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.CustomerRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.CustomerResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setActive(dto.isActive());
        Store store = new Store();
        store.setStoreId(dto.getStoreId());
        customer.setStore(store);
        Address address = new Address();
        address.setAddressId(dto.getAddressId());
        customer.setAddress(address);
        return customer;
    }

    public CustomerResponseDTO toResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setActive(customer.isActive());
        dto.setCreateDate(customer.getCreateDate());
        dto.setLastUpdate(customer.getLastUpdate());
        if (customer.getStore() != null) {
            dto.setStoreId(customer.getStore().getStoreId());
        }
        if (customer.getAddress() != null) {
            dto.setAddressId(customer.getAddress().getAddressId());
            dto.setAddressLine(customer.getAddress().getAddress());
        }
        return dto;
    }

    public void updateEntity(Customer customer, CustomerRequestDTO dto) {
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setActive(dto.isActive());
    }
}
