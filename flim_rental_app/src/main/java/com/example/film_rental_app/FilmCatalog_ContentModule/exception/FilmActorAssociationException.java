package com.example.film_rental_app.FilmCatalog_ContentModule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

public class FilmActorAssociationException extends DuplicateResourceException {

    public FilmActorAssociationException(Integer filmId, Integer actorId) {
        super("FilmActor association already exists for filmId: " + filmId + " and actorId: " + actorId);
    }

    public FilmActorAssociationException(String message) {
        super(message);
    }
}
