package com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerAlreadyExistsException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerInvalidOperationException;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Integer customerId) {
        // ResourceNotFoundException → HTTP 404
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Override
    public Customer createCustomer(Customer customer) {
        // DuplicateResourceException → HTTP 409
        if (customer.getEmail() != null && customerRepository.existsByEmail(customer.getEmail())) {
            throw new CustomerAlreadyExistsException(customer.getEmail());
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Integer customerId, Customer updated) {
        // ResourceNotFoundException → HTTP 404
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        // DuplicateResourceException → HTTP 409
        if (updated.getEmail() != null
                && !customer.getEmail().equalsIgnoreCase(updated.getEmail())
                && customerRepository.existsByEmail(updated.getEmail())) {
            throw new CustomerAlreadyExistsException(updated.getEmail());
        }
        customer.setFirstName(updated.getFirstName());
        customer.setLastName(updated.getLastName());
        customer.setEmail(updated.getEmail());
        customer.setActive(updated.isActive());
        if (updated.getStore()   != null) customer.setStore(updated.getStore());
        if (updated.getAddress() != null) customer.setAddress(updated.getAddress());
        return customerRepository.save(customer);
    }

    @Override
    public boolean deleteCustomer(Integer customerId) {
        // ResourceNotFoundException → HTTP 404
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        // InvalidOperationException → HTTP 400
        if (customer.isActive()) {
            throw new CustomerInvalidOperationException(customerId,
                    "You cannot delete an active Customer. Deactivate the customer account first before deleting it.");
        }
        customerRepository.deleteById(customerId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByStoreAndStatus(Integer storeId, boolean active) {
        return customerRepository.findByStoreIdAndActiveStatus(storeId, active);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> searchCustomersByName(String keyword) {
        return customerRepository.searchByName(keyword);
    }
}
