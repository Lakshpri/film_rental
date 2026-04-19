package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class FilmTextNotFoundException extends ResourceNotFoundException {
    public FilmTextNotFoundException(Integer filmId) {
        super("FilmText not found for filmId: " + filmId);
    }
}
