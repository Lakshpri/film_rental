package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class FilmCategoryNotFoundException extends ResourceNotFoundException {
    public FilmCategoryNotFoundException(Integer filmId, Integer categoryId) {
        super("Category " + categoryId + " is not linked to Film " + filmId
                + ". This category may not be assigned to this film.");
    }
}
