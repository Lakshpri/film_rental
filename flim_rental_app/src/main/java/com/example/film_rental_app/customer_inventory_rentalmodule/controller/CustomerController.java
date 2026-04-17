package com.example.film_rental_app.customer_inventory_rentalmodule.controller;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class
CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId) {
        return customerRepository.findById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer customerId, @RequestBody Customer updated) {
        return customerRepository.findById(customerId).map(customer -> {
            customer.setFirstName(updated.getFirstName());
            customer.setLastName(updated.getLastName());
            customer.setEmail(updated.getEmail());
            customer.setActive(updated.isActive());
            if (updated.getStore() != null) customer.setStore(updated.getStore());
            if (updated.getAddress() != null) customer.setAddress(updated.getAddress());
            return ResponseEntity.ok(customerRepository.save(customer));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
        if (!customerRepository.existsById(customerId)) return ResponseEntity.notFound().build();
        customerRepository.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
}

