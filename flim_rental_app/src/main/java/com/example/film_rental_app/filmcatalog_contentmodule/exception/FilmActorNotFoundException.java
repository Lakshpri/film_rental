package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/** Extends ResourceNotFoundException (HTTP 404).
 *  Thrown when the user tries to remove an Actor from a Film
 *  but that link does not exist. */
public class FilmActorNotFoundException extends ResourceNotFoundException {
    public FilmActorNotFoundException(Integer filmId, Integer actorId) {
        super("No FilmActor link exists for filmId=" + filmId + " and actorId=" + actorId
                + ". The actor may not be associated with this film. "
                + "Use GET /api/films/" + filmId + "/actors to see current associations.");
    }
}
