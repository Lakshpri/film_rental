package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class FilmActorAssociationException extends DuplicateResourceException {
    public FilmActorAssociationException(Integer filmId, Integer actorId) {
        super("Actor " + actorId + " is already part of the cast for Film " + filmId + ". This actor cannot be added again.");
    }
    public FilmActorAssociationException(String message) {
        super(message);
    }
}
