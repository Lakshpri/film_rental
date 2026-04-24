package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/** Extends ResourceNotFoundException (HTTP 404).
 *  Thrown when a Film ID does not exist. */
public class FilmNotFoundException extends ResourceNotFoundException {
    public FilmNotFoundException(Integer filmId) {
        super("Film", filmId);
    }
}
