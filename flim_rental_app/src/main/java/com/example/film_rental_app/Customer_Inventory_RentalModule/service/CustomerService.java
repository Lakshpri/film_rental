package com.example.film_rental_app.Customer_Inventory_RentalModule.service;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    Customer getCustomerById(Integer customerId);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Integer customerId, Customer updated);

    void deleteCustomer(Integer customerId);

    List<Customer> getCustomersByStoreAndStatus(Integer storeId, boolean active);

    List<Customer> searchCustomersByName(String keyword);
}
