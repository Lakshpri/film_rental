package com.example.film_rental_app.filmcatalog_contentmodule;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActor;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.ActorAlreadyExistsException;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.ActorNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.ActorRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmActorRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.service.implementation.ActorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorServiceImplTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private FilmActorRepository filmActorRepository;

    @InjectMocks
    private ActorServiceImpl actorService;

    // ---------------------- POSITIVE TEST CASES (8) ----------------------

    @Test
    void testGetAllActors_Success() {
        List<Actor> actors = List.of(new Actor(), new Actor());
        when(actorRepository.findAll()).thenReturn(actors);

        List<Actor> result = actorService.getAllActors();

        assertEquals(2, result.size());
    }

    @Test
    void testGetActorById_Success() {
        Actor actor = new Actor();
        actor.setActorId(1);

        when(actorRepository.findById(1)).thenReturn(Optional.of(actor));

        Actor result = actorService.getActorById(1);

        assertNotNull(result);
        assertEquals(1, result.getActorId());
    }
    @Test
    void testDeleteActor_Success() {
        when(actorRepository.existsById(1)).thenReturn(true);
        when(filmActorRepository.findById_ActorId(1)).thenReturn(Collections.emptyList());

        boolean result = actorService.deleteActor(1);

        assertTrue(result);
        verify(actorRepository).deleteById(1);
    }

    @Test
    void testGetAllActors_EmptyList() {
        when(actorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Actor> result = actorService.getAllActors();

        assertTrue(result.isEmpty());
    }

    // ---------------------- NEGATIVE TEST CASES (7) ----------------------

    @Test
    void testGetActorById_NotFound() {
        when(actorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ActorNotFoundException.class,
                () -> actorService.getActorById(1));
    }

    @Test
    void testCreateActor_Duplicate() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");

        when(actorRepository.existsByFirstNameAndLastName("Tom", "Hanks")).thenReturn(true);

        assertThrows(ActorAlreadyExistsException.class,
                () -> actorService.createActor(actor));
    }

    @Test
    void testUpdateActor_NotFound() {
        when(actorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ActorNotFoundException.class,
                () -> actorService.updateActor(1, new Actor()));
    }

    @Test
    void testUpdateActor_DuplicateName() {
        Actor existing = new Actor();
        existing.setActorId(1);
        existing.setFirstName("Old");
        existing.setLastName("Name");

        Actor updated = new Actor();
        updated.setFirstName("Tom");
        updated.setLastName("Hanks");

        when(actorRepository.findById(1)).thenReturn(Optional.of(existing));
        when(actorRepository.existsByFirstNameAndLastName("Tom", "Hanks")).thenReturn(true);

        assertThrows(ActorAlreadyExistsException.class,
                () -> actorService.updateActor(1, updated));
    }

    @Test
    void testDeleteActor_NotFound() {
        when(actorRepository.existsById(1)).thenReturn(false);

        assertThrows(ActorNotFoundException.class,
                () -> actorService.deleteActor(1));
    }
}