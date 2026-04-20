package com.example.film_rental_app.customer_inventory_rentalmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/** Extends ResourceNotFoundException (HTTP 404).
 *  Thrown when a Rental ID does not exist. */
public class RentalNotFoundException extends ResourceNotFoundException {
  public RentalNotFoundException(Integer rentalId) {
    super("Rental", rentalId);
  }
  public RentalNotFoundException(String message) {
    super(message);
  }
}
