package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to create a Film with a title that already exists. */
public class FilmAlreadyExistsException extends DuplicateResourceException {
    public FilmAlreadyExistsException(String title) {
        super("Film", "title", title);
    }
    public FilmAlreadyExistsException(String field, Object value) {
        super("Film", field, value);
    }
}
