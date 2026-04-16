package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class ActorNotFoundException extends ResourceNotFoundException {

    public ActorNotFoundException(Integer actorId) {
        super("Actor", actorId);
    }

    public ActorNotFoundException(String message) {
        super(message);
    }
}
