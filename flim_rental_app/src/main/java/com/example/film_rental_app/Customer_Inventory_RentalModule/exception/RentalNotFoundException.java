package com.example.film_rental_app.Customer_Inventory_RentalModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class RentalNotFoundException extends ResourceNotFoundException {

  public RentalNotFoundException(Integer rentalId) {
    super("Rental", rentalId);
  }

  public RentalNotFoundException(String message) {
    super(message);
  }
}
