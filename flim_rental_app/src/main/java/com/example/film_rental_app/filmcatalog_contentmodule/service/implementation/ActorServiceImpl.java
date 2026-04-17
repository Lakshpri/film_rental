package com.example.film_rental_app.filmcatalog_contentmodule.service.implementation;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.ActorNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.ActorRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
    public boolean deleteActor(Integer actorId) {
        if (!actorRepository.existsById(actorId)) {
            throw new ActorNotFoundException(actorId);
        }
        actorRepository.deleteById(actorId);
        return true;
    }
}
