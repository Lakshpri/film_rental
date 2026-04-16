package com.example.film_rental_app.filmcatalog_contentmodule.repository;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
}
