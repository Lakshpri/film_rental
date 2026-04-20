package com.example.film_rental_app.filmcatalog_contentmodule.dto.response;

import java.time.LocalDateTime;

public class ActorResponseDTO {

    private Integer actorId;
    private String firstName;
    private String lastName;
    private LocalDateTime lastUpdate;

    public ActorResponseDTO() {}

    public ActorResponseDTO(Integer actorId, String firstName, String lastName, LocalDateTime lastUpdate) {
        this.actorId = actorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdate = lastUpdate;
    }

    public Integer getActorId() { return actorId; }
    public void setActorId(Integer actorId) { this.actorId = actorId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
