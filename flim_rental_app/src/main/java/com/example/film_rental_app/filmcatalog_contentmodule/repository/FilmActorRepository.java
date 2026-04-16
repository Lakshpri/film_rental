package com.example.film_rental_app.filmcatalog_contentmodule.repository;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId> {
    List<FilmActor> findById_FilmId(Integer filmId);
    boolean existsById_FilmIdAndId_ActorId(Integer filmId, Integer actorId);
}
