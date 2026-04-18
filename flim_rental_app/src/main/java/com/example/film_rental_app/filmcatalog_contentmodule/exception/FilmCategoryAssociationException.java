package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to link a Category to a Film that is already linked. */
public class FilmCategoryAssociationException extends DuplicateResourceException {
    public FilmCategoryAssociationException(Integer filmId, Integer categoryId) {
        super("FilmCategory", "filmId + categoryId", "filmId=" + filmId + ", categoryId=" + categoryId);
    }
    public FilmCategoryAssociationException(String message) {
        super(message);
    }
}
