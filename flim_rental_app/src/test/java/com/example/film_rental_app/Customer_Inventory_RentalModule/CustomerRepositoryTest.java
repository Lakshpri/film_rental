package com.example.film_rental_app.Customer_Inventory_RentalModule;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Customer;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerRepositoryTest {

    @Test
    void customer_gettersAndSetters_shouldWorkCorrectly() {

        Customer customer = new Customer();
        customer.setFirstName("Alice");
        customer.setLastName("Smith");
        customer.setEmail("alice@example.com");
        customer.setActive(true);

        assertThat(customer.getFirstName()).isEqualTo("Alice");
        assertThat(customer.getLastName()).isEqualTo("Smith");
        assertThat(customer.getEmail()).isEqualTo("alice@example.com");
        assertThat(customer.isActive()).isTrue();
    }
}