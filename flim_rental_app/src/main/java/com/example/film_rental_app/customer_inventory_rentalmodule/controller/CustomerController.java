package com.example.film_rental_app.customer_inventory_rentalmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.CustomerRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.CustomerResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.CustomerMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> result = customerService.getAllCustomers().stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerMapper.toResponseDTO(customerService.getCustomerById(customerId)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDTO>> searchCustomers(@RequestParam String keyword) {
        List<CustomerResponseDTO> result = customerService.searchCustomersByName(keyword).stream()
                .map(customerMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Store store = storeService.getStoreById(dto.getStoreId());
        customer.setStore(store);
        Address address = addressService.getAddressById(dto.getAddressId());
        customer.setAddress(address);
        return ResponseEntity.status(201).body(customerMapper.toResponseDTO(customerService.createCustomer(customer)));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Integer customerId,
                                                              @Valid @RequestBody CustomerRequestDTO dto) {
        Customer existing = customerService.getCustomerById(customerId);
        customerMapper.updateEntity(existing, dto);
        if (dto.getStoreId() != null) {
            existing.setStore(storeService.getStoreById(dto.getStoreId()));
        }
        if (dto.getAddressId() != null) {
            existing.setAddress(addressService.getAddressById(dto.getAddressId()));
        }
        return ResponseEntity.ok(customerMapper.toResponseDTO(customerService.updateCustomer(customerId, existing)));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
