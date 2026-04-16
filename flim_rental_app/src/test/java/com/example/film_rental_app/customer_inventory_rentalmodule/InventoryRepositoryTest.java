package com.example.film_rental_app.customer_inventory_rentalmodule;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryRepositoryTest {

    @Test
    void inventory_gettersAndSetters_shouldWorkCorrectly() {

        Inventory inventory = new Inventory();
        inventory.setInventoryId(1);

        assertThat(inventory.getInventoryId()).isEqualTo(1);
    }
}