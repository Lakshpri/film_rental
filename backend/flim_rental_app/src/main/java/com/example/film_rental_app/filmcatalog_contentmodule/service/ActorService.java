package com.example.film_rental_app.filmcatalog_contentmodule.service;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;

import java.util.List;

public interface ActorService {

    List<Actor> getAllActors();

    Actor getActorById(Integer actorId);

    Actor createActor(Actor actor);

    Actor updateActor(Integer actorId, Actor updated);

    boolean deleteActor(Integer actorId);
}