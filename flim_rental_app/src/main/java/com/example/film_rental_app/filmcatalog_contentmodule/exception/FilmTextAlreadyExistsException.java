package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class FilmTextAlreadyExistsException extends DuplicateResourceException {
    public FilmTextAlreadyExistsException(Integer filmId) {
        super("A description entry already exists for Film ID " + filmId + ". Each film can only have one description entry.");
    }
}
