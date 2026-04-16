package com.example.film_rental_app.FilmCatalog_ContentModule.exception;

import com.example.film_rental_app.common.exception.ResourceNotFoundException;

public class ActorNotFoundException extends ResourceNotFoundException {

    public ActorNotFoundException(Integer actorId) {
        super("Actor", actorId);
    }

    public ActorNotFoundException(String message) {
        super(message);
    }
}
