package com.example.film_rental_app.filmcatalog_contentmodule.repository;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId> {

    // JOIN FETCH actor to avoid LazyInitializationException in controller
    @Query("SELECT fa FROM FilmActor fa JOIN FETCH fa.actor WHERE fa.id.filmId = :filmId")
    List<FilmActor> findById_FilmId(@Param("filmId") Integer filmId);

    List<FilmActor> findById_ActorId(Integer actorId);
    boolean existsById_FilmIdAndId_ActorId(Integer filmId, Integer actorId);
}
