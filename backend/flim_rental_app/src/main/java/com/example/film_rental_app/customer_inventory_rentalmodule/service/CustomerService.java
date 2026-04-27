package com.example.film_rental_app.customer_inventory_rentalmodule.service;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;

import java.util.List;

// Service interface defining customer operations
public interface CustomerService {

    // Get all customers
    List<Customer> getAllCustomers();

    // Get customer by ID
    Customer getCustomerById(Integer customerId);

    // Create new customer
    Customer createCustomer(Customer customer);

    // Update existing customer
    Customer updateCustomer(Integer customerId, Customer updated);

    // Delete customer by ID
    boolean deleteCustomer(Integer customerId);

    // Get customers by store and active status
    List<Customer> getCustomersByStoreAndStatus(Integer storeId, boolean active);

    // Search customers by name keyword
    List<Customer> searchCustomersByName(String keyword);
}