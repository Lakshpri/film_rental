package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/** Extends ResourceNotFoundException (HTTP 404).
 *  Thrown when the user tries to remove a Category from a Film
 *  but that link does not exist. */
public class FilmCategoryNotFoundException extends ResourceNotFoundException {
    public FilmCategoryNotFoundException(Integer filmId, Integer categoryId) {
        super("No FilmCategory link exists for filmId=" + filmId + " and categoryId=" + categoryId
                + ". The category may not be associated with this film. "
                + "Use GET /api/films/" + filmId + "/categories to see current associations.");
    }
}
