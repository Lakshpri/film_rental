package com.example.film_rental_app.FilmCatalog_ContentModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class FilmNotFoundException extends ResourceNotFoundException {

    public FilmNotFoundException(Integer filmId) {
        super("Film", filmId);
    }

    public FilmNotFoundException(String message) {
        super(message);
    }
}
