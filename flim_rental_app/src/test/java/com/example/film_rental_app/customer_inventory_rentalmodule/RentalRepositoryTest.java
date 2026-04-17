package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.*;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.RentalRepository;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;



    @Test
    void testFindByCustomerId() {
        Integer customerId = 1;

        var rentals = rentalRepository.findByCustomer_CustomerId(customerId);

        assertNotNull(rentals);
        System.out.println("Rentals: " + rentals.size());
    }
}