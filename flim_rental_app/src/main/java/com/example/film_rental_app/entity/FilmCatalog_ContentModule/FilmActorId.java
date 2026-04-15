package com.example.film_rental_app.entity.FilmCatalog_ContentModule;

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

