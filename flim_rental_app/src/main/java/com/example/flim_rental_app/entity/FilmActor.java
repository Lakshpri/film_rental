package com.example.flim_rental_app.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_actor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = {"actor", "film"})
public class FilmActor {

    @EmbeddedId
    private FilmActorId id;

    private LocalDateTime lastUpdate;

    private Actor actor;

    private Film film;
}

