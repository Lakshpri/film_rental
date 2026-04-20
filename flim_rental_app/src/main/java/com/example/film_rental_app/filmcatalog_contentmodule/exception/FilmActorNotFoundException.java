package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class FilmActorNotFoundException extends ResourceNotFoundException {
    public FilmActorNotFoundException(Integer filmId, Integer actorId) {
        super("Actor " + actorId + " is not linked to Film " + filmId
                + ". This actor may not be part of this film's cast.");
    }
}
