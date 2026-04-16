package com.example.film_rental_app.FilmCatalog_ContentModule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class FilmCategoryAssociationException extends DuplicateResourceException {

    public FilmCategoryAssociationException(Integer filmId, Integer categoryId) {
        super("FilmCategory association already exists for filmId: " + filmId + " and categoryId: " + categoryId);
    }

    public FilmCategoryAssociationException(String message) {
        super(message);
    }
}
