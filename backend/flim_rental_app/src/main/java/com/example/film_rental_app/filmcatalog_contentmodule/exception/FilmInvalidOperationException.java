package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class FilmInvalidOperationException extends InvalidOperationException {
    public FilmInvalidOperationException(Integer filmId, String reason) {
        super(reason);
    }
    public FilmInvalidOperationException(String message) {
        super(message);
    }
}
