package com.example.film_rental_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_actor", indexes = {
        @Index(name = "idx_fk_film_id", columnList = "film_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = {"actor", "film"})
public class FilmActor {

    @EmbeddedId
    private FilmActorId id;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Many FilmActor -> One Actor
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("actorId")
    @JoinColumn(name = "actor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_film_actor_actor"))
    private Actor actor;

    // Many FilmActor -> One Film
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("filmId")
    @JoinColumn(name = "film_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_film_actor_film"))
    private Film film;
}


