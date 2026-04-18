package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.DuplicateResourceException;

/** Extends DuplicateResourceException (HTTP 409).
 *  Thrown when the user tries to create an Actor with a name that already exists. */
public class ActorAlreadyExistsException extends DuplicateResourceException {
    public ActorAlreadyExistsException(String firstName, String lastName) {
        super("Actor", "name", firstName + " " + lastName);
    }
    public ActorAlreadyExistsException(String field, Object value) {
        super("Actor", field, value);
    }
}
