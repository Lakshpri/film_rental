package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

/** Extends ResourceNotFoundException (HTTP 404).
 *  Thrown when an Actor ID does not exist. */
public class ActorNotFoundException extends ResourceNotFoundException {
    public ActorNotFoundException(Integer actorId) {
        super("Actor", actorId);
    }
    public ActorNotFoundException(String message) {
        super(message);
    }
}
