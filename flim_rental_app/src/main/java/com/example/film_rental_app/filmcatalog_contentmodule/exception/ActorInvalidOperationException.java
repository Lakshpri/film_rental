package com.example.film_rental_app.filmcatalog_contentmodule.exception;

import com.example.film_rental_app.common.exception.InvalidOperationException;

public class ActorInvalidOperationException extends InvalidOperationException {
    public ActorInvalidOperationException(Integer actorId, String reason) {
        super(reason);
    }
    public ActorInvalidOperationException(String message) {
        super(message);
    }
}
