package com.example.film_rental_app.filmcatalog_contentmodule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActorRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 45, message = "First name must not exceed 45 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 45, message = "Last name must not exceed 45 characters")
    private String lastName;

    public ActorRequestDTO() {}

    public ActorRequestDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}