package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

/** Extends InvalidOperationException (HTTP 400).
 *  Thrown when a business rule is violated on an Actor
 *  (e.g. deleting an Actor who is still linked to active Films). */
public class ActorInvalidOperationException extends InvalidOperationException {
    public ActorInvalidOperationException(Integer actorId, String reason) {
        super("Cannot perform this operation on Actor with ID = " + actorId + ". Reason: " + reason);
    }
    public ActorInvalidOperationException(String message) {
        super(message);
    }
}
