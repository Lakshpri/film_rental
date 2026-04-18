package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to link an Actor to a Film that is already linked. */
public class FilmActorAssociationException extends DuplicateResourceException {
    public FilmActorAssociationException(Integer filmId, Integer actorId) {
        super("FilmActor", "filmId + actorId", "filmId=" + filmId + ", actorId=" + actorId);
    }
    public FilmActorAssociationException(String message) {
        super(message);
    }
}
