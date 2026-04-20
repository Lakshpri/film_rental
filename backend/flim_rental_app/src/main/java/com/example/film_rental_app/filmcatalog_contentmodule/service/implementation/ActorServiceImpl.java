package com.example.film_rental_app.filmcatalog_contentmodule.service.implementation;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.ActorAlreadyExistsException;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.ActorInvalidOperationException;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.ActorNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.ActorRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmActorRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActorServiceImpl implements ActorService {
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private FilmActorRepository filmActorRepository;



    @Override
    @Transactional(readOnly = true)
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Actor getActorById(Integer actorId) {
        // ResourceNotFoundException → HTTP 404
        return actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNotFoundException(actorId));
    }

    @Override
    public Actor createActor(Actor actor) {
        // DuplicateResourceException → HTTP 409
        if (actorRepository.existsByFirstNameAndLastName(actor.getFirstName(), actor.getLastName())) {
            throw new ActorAlreadyExistsException(actor.getFirstName(), actor.getLastName());
        }
        return actorRepository.saveAndFlush(actor);
    }

    @Override
    public Actor updateActor(Integer actorId, Actor updated) {
        // ResourceNotFoundException → HTTP 404
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNotFoundException(actorId));
        // DuplicateResourceException → HTTP 409
        if ((!actor.getFirstName().equalsIgnoreCase(updated.getFirstName())
                || !actor.getLastName().equalsIgnoreCase(updated.getLastName()))
                && actorRepository.existsByFirstNameAndLastName(updated.getFirstName(), updated.getLastName())) {
            throw new ActorAlreadyExistsException(updated.getFirstName(), updated.getLastName());
        }
        actor.setFirstName(updated.getFirstName());
        actor.setLastName(updated.getLastName());
        return actorRepository.saveAndFlush(actor);
    }

    @Override
    public boolean deleteActor(Integer actorId) {
        // ResourceNotFoundException → HTTP 404
        if (!actorRepository.existsById(actorId)) {
            throw new ActorNotFoundException(actorId);
        }
        // InvalidOperationException → HTTP 400
        if (!filmActorRepository.findById_ActorId(actorId).isEmpty()) {
            throw new ActorInvalidOperationException(actorId,
                    "This Actor is still linked to one or more Films. "
                            + "Remove all Film-Actor associations first before deleting the Actor.");
        }
        actorRepository.deleteById(actorId);
        return true;
    }
}
