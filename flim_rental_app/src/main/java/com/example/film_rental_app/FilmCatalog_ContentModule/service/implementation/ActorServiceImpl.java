package com.example.film_rental_app.FilmCatalog_ContentModule.service.implementation;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Actor;
import com.example.film_rental_app.FilmCatalog_ContentModule.exception.ActorNotFoundException;
import com.example.film_rental_app.FilmCatalog_ContentModule.repository.ActorRepository;
import com.example.film_rental_app.FilmCatalog_ContentModule.service.ActorService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Actor getActorById(Integer actorId) {
        return actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNotFoundException(actorId));
    }

    @Override
    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

    @Override
    public Actor updateActor(Integer actorId, Actor updated) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNotFoundException(actorId));
        actor.setFirstName(updated.getFirstName());
        actor.setLastName(updated.getLastName());
        return actorRepository.save(actor);
    }

    @Override
    public void deleteActor(Integer actorId) {
        if (!actorRepository.existsById(actorId)) {
            throw new ActorNotFoundException(actorId);
        }
        actorRepository.deleteById(actorId);
    }
}
