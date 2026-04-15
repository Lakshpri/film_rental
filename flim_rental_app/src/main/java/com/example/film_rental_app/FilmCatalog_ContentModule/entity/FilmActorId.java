package com.example.film_rental_app.FilmCatalog_ContentModule.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class FilmActorId implements Serializable {

    private Integer actorId;
    private Integer filmId;
}

