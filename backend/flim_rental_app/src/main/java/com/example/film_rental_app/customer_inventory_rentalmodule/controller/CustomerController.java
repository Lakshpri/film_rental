package com.example.film_rental_app.customer_inventory_rentalmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.dto.request.CustomerRequestDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.CustomerResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.dto.response.RentalResponseDTO;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.CustomerMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.mapper.RentalMapper;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import com.example.film_rental_app.location_store_staffmodule.service.AddressService;
import com.example.film_rental_app.location_store_staffmodule.service.StoreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    @Autowired private CustomerService customerService;
    @Autowired private StoreService storeService;
    @Autowired private AddressService addressService;
    @Autowired private CustomerMapper customerMapper;
    @Autowired private RentalService rentalService;
    @Autowired private RentalMapper rentalMapper;

    // GET all customers
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> result = customerService.getAllCustomers().stream()
                .map(customerMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(result);
    }

    // GET customer by ID
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(
            @PathVariable @Positive Integer customerId) {

        return ResponseEntity.ok(
                customerMapper.toResponseDTO(customerService.getCustomerById(customerId))
        );
    }

    // GET rentals of customer
    @GetMapping("/{customerId}/rentals")
    public ResponseEntity<List<RentalResponseDTO>> getCustomerRentals(
            @PathVariable @Positive Integer customerId) {

        customerService.getCustomerById(customerId); // validation

        List<RentalResponseDTO> result =
                rentalService.getRentalsByCustomer(customerId).stream()
                        .map(rentalMapper::toResponseDTO)
                        .toList();

        return ResponseEntity.ok(result);
    }

    // CREATE customer
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Valid @RequestBody CustomerRequestDTO dto) {

        Customer customer = customerMapper.toEntity(dto);

        customer.setStore(storeService.getStoreById(dto.getStoreId()));
        customer.setAddress(addressService.getAddressById(dto.getAddressId()));

        CustomerResponseDTO response =
                customerMapper.toResponseDTO(customerService.createCustomer(customer));

        response.setMessage("Customer created successfully.");

        return ResponseEntity.status(201).body(response);
    }

    // UPDATE customer
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable @Positive Integer customerId,
            @Valid @RequestBody CustomerRequestDTO dto) {

        Customer existing = customerService.getCustomerById(customerId);

        customerMapper.updateEntity(existing, dto);

        if (dto.getStoreId() != null)
            existing.setStore(storeService.getStoreById(dto.getStoreId()));

        if (dto.getAddressId() != null)
            existing.setAddress(addressService.getAddressById(dto.getAddressId()));

        CustomerResponseDTO response =
                customerMapper.toResponseDTO(customerService.updateCustomer(customerId, existing));

        response.setMessage("Customer updated successfully.");

        return ResponseEntity.ok(response);
    }

    // DELETE customer
    @DeleteMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> deleteCustomer(
            @PathVariable @Positive Integer customerId) {

        customerService.deleteCustomer(customerId);

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setMessage("Customer deleted successfully.");

        return ResponseEntity.ok(response);
    }
}