package com.example.film_rental_app.FilmCatalog_ContentModule.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_actor")
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

    public FilmActorId getId() {
        return id;
    }

    public void setId(FilmActorId id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
    public FilmActor(){

    }

    public FilmActor(FilmActorId id, LocalDateTime lastUpdate, Actor actor, Film film) {
        this.id = id;
        this.lastUpdate = lastUpdate;
        this.actor = actor;
        this.film = film;
    }

    @Override
    public String toString() {
        return "FilmActor{" +
                "id=" + id +
                ", lastUpdate=" + lastUpdate +
                ", actor=" + actor +
                ", film=" + film +
                '}';
    }
}


