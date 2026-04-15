package com.example.film_rental_app.repository.FilmCatalog_ContentModule;

import com.example.film_rental_app.entity.FilmCatalog_ContentModule.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
}
