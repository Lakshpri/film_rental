package com.example.film_rental_app.FilmCatalog_ContentModule;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.*;
import com.example.film_rental_app.FilmCatalog_ContentModule.repository.FilmActorRepository;
import com.example.film_rental_app.Master_DataModule.entity.Language;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class FilmActorRepositoryTest {

    @Autowired
    private FilmActorRepository filmActorRepository;

    @Autowired
    private EntityManager entityManager;


    // Helper Methods


    private Actor createActor() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");
        entityManager.persist(actor);
        return actor;
    }

    private Film createFilm() {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setRentalDuration((short) 3);
        film.setRentalRate(new java.math.BigDecimal("4.99"));
        film.setReplacementCost(new java.math.BigDecimal("19.99"));

        // IMPORTANT: language is mandatory
        Language language = new Language();
        language.setName("English");
        entityManager.persist(language);

        film.setLanguage(language);

        entityManager.persist(film);
        return film;
    }

    private FilmActor createFilmActor(Actor actor, Film film) {
        FilmActorId id = new FilmActorId(actor.getActorId(), film.getFilmId());

        FilmActor fa = new FilmActor();
        fa.setId(id);
        fa.setActor(actor);
        fa.setFilm(film);

        return filmActorRepository.saveAndFlush(fa);
    }


    // CREATE


    @Test
    @DisplayName("Save FilmActor with valid data should persist successfully")
    void saveFilmActor_withValidData_shouldPersist() {
        Actor actor = createActor();
        Film film = createFilm();

        FilmActor saved = createFilmActor(actor, film);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getActor()).isNotNull();
        assertThat(saved.getFilm()).isNotNull();
    }


    // VALIDATION


    @Test
    @DisplayName("Save FilmActor without actor should throw exception")
    void saveFilmActor_withoutActor_shouldThrow() {
        Film film = createFilm();

        FilmActor fa = new FilmActor();
        fa.setFilm(film);
        fa.setId(new FilmActorId(null, film.getFilmId()));

        assertThatThrownBy(() -> filmActorRepository.saveAndFlush(fa))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Save FilmActor without film should throw exception")
    void saveFilmActor_withoutFilm_shouldThrow() {
        Actor actor = createActor();

        FilmActor fa = new FilmActor();
        fa.setActor(actor);
        fa.setId(new FilmActorId(actor.getActorId(), null));

        assertThatThrownBy(() -> filmActorRepository.saveAndFlush(fa))
                .isInstanceOf(Exception.class);
    }


    // READ


    @Test
    @DisplayName("Find FilmActor by ID should return record")
    void findById_shouldReturnFilmActor() {
        Actor actor = createActor();
        Film film = createFilm();

        FilmActor saved = createFilmActor(actor, film);

        FilmActor found = filmActorRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId().getFilmId()).isEqualTo(film.getFilmId());
    }

    @Test
    @DisplayName("Find FilmActor by filmId should return list")
    void findByFilmId_shouldReturnList() {
        Actor a1 = createActor();
        Actor a2 = createActor();
        Film film = createFilm();

        createFilmActor(a1, film);
        createFilmActor(a2, film);

        List<FilmActor> list = filmActorRepository.findById_FilmId(film.getFilmId());

        assertThat(list).hasSize(2);
    }




    // DELETE


    @Test
    @DisplayName("Delete FilmActor should remove record")
    void deleteFilmActor_shouldRemove() {
        Actor actor = createActor();
        Film film = createFilm();

        FilmActor saved = createFilmActor(actor, film);

        filmActorRepository.deleteById(saved.getId());
        filmActorRepository.flush();

        boolean exists = filmActorRepository.existsById(saved.getId());

        assertThat(exists).isFalse();
    }


    // EXISTS


    @Test
    @DisplayName("Exists should return true if FilmActor exists")
    void exists_shouldReturnTrue() {
        Actor actor = createActor();
        Film film = createFilm();

        createFilmActor(actor, film);

        boolean exists = filmActorRepository
                .existsById_FilmIdAndId_ActorId(film.getFilmId(), actor.getActorId());

        assertThat(exists).isTrue();
    }
}