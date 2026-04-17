package com.example.film_rental_app.customer_inventory_rentalmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.exception.CustomerNotFoundException;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

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
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Integer customerId, Customer updated) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        customer.setFirstName(updated.getFirstName());
        customer.setLastName(updated.getLastName());
        customer.setEmail(updated.getEmail());
        customer.setActive(updated.isActive());
        if (updated.getStore() != null) customer.setStore(updated.getStore());
        if (updated.getAddress() != null) customer.setAddress(updated.getAddress());
        return customerRepository.save(customer);
    }

    @Override
    public boolean deleteCustomer(Integer customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
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
