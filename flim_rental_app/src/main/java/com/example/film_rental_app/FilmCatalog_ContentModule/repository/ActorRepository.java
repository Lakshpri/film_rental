package com.example.film_rental_app.FilmCatalog_ContentModule.repository;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
}
