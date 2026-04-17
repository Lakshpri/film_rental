package com.example.film_rental_app.common.exception;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(String.format(
                "[400 - INVALID OPERATION]\n" +
                        "  Problem  : %s\n" +
                        "  Fix      : Please review the request. Ensure all business rules and pre-conditions are met before retrying.",
                message));
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(String.format(
                "[400 - INVALID OPERATION]\n" +
                        "  Problem  : %s\n" +
                        "  Fix      : Please review the request. Ensure all business rules and pre-conditions are met before retrying.",
                message), cause);
    }
}
