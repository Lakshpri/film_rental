package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class FilmCategoryAssociationException extends DuplicateResourceException {
    public FilmCategoryAssociationException(Integer filmId, Integer categoryId) {
        super("Category " + categoryId + " is already assigned to Film " + filmId + ". This category cannot be added again.");
    }
    public FilmCategoryAssociationException(String message) {
        super(message);
    }
}
