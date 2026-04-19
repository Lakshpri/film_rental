package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class FilmTextAlreadyExistsException extends DuplicateResourceException {
    public FilmTextAlreadyExistsException(Integer filmId) {
        super("FilmText already exists for filmId: " + filmId);
    }
}
