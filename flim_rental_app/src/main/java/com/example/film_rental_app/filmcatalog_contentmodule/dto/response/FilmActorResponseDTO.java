package com.example.film_rental_app.filmcatalog_contentmodule.dto.response;

import java.time.LocalDateTime;

/** Returned for GET /films/{filmId}/actors */
public class FilmActorResponseDTO {

    private Integer filmId;
    private Integer actorId;
    private String actorFirstName;
    private String actorLastName;
    private LocalDateTime lastUpdate;

    public FilmActorResponseDTO() {}

    public FilmActorResponseDTO(Integer filmId, Integer actorId,
                                String actorFirstName, String actorLastName,
                                LocalDateTime lastUpdate) {
        this.filmId = filmId;
        this.actorId = actorId;
        this.actorFirstName = actorFirstName;
        this.actorLastName = actorLastName;
        this.lastUpdate = lastUpdate;
    }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public Integer getActorId() { return actorId; }
    public void setActorId(Integer actorId) { this.actorId = actorId; }

    public String getActorFirstName() { return actorFirstName; }
    public void setActorFirstName(String actorFirstName) { this.actorFirstName = actorFirstName; }

    public String getActorLastName() { return actorLastName; }
    public void setActorLastName(String actorLastName) { this.actorLastName = actorLastName; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
