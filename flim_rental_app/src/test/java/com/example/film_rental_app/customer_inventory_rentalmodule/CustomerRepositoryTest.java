package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;



    // UPDATE (PUT LOGIC)
    @Test
    void shouldUpdateCustomer() {

        Optional<Customer> optionalCustomer = customerRepository.findById(1);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();

            customer.setFirstName("UpdatedName");
            customer.setEmail("updated@test.com");

            Customer updated = customerRepository.save(customer);

            assertThat(updated.getFirstName()).isEqualTo("UpdatedName");
        }
    }



    // ✅ CUSTOM QUERY TEST
    @Test
    void shouldFindByStoreAndActive() {
        List<Customer> customers =
                customerRepository.findByStoreIdAndActiveStatus(1, true);

        assertThat(customers).isNotNull();
    }

    @Test
    void shouldSearchByName() {
        List<Customer> customers =
                customerRepository.searchByName("john");

        assertThat(customers).isNotNull();
    }
}