package com.example.film_rental_app.Customer_Inventory_RentalModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public CustomerNotFoundException(Integer customerId) {
        super("Customer", customerId);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
