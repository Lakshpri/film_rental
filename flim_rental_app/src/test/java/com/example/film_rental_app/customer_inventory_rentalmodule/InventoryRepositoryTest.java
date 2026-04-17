package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.InventoryRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;



    // READ ALL
    @Test
    @DisplayName("Find all")
    void findAll() {
        assertThat(inventoryRepository.findAll()).isNotNull();
    }

    // DELETE
    @Test
    @DisplayName("Delete inventory")
    void delete() {

        inventoryRepository.deleteById(1);

        assertThat(inventoryRepository.existsById(1)).isFalse();
    }
}