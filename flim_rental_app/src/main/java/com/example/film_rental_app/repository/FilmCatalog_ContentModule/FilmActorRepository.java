package com.example.film_rental_app.repository.FilmCatalog_ContentModule;

import com.example.film_rental_app.entity.FilmCatalog_ContentModule.FilmActor;
import com.example.film_rental_app.entity.FilmCatalog_ContentModule.FilmActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId> {
    List<FilmActor> findById_FilmId(Integer filmId);
    boolean existsById_FilmIdAndId_ActorId(Integer filmId, Integer actorId);
}
