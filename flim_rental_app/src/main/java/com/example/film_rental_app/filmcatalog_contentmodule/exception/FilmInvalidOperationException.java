package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/** Extends InvalidOperationException (HTTP 400).
 *  Thrown when a business rule is violated on a Film
 *  (e.g. deleting a Film that still has active Inventory or Rentals). */
public class FilmInvalidOperationException extends InvalidOperationException {
    public FilmInvalidOperationException(Integer filmId, String reason) {
        super("Cannot perform this operation on Film with ID = " + filmId + ". Reason: " + reason);
    }
    public FilmInvalidOperationException(String message) {
        super(message);
    }
}
