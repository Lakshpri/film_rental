package com.example.film_rental_app.FilmCatalog_ContentModule;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Film;
import com.example.film_rental_app.FilmCatalog_ContentModule.repository.FilmRepository;
import com.example.film_rental_app.Master_DataModule.entity.Language;
import jakarta.validation.ConstraintViolationException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class FilmRepositoryTest {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private EntityManager entityManager;

    // Helper Method

    private Film createValidFilm() {
        Language language = new Language();
        language.setName("English");
        entityManager.persist(language);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setRentalDuration((short) 3);
        film.setRentalRate(new BigDecimal("4.99"));
        film.setReplacementCost(new BigDecimal("19.99"));
        film.setLanguage(language);

        return film;
    }


    // CREATE


    @Test
    @DisplayName("Save film with valid data should persist successfully")
    void saveFilm_withValidData_shouldPersist() {
        Film film = createValidFilm();

        Film saved = filmRepository.saveAndFlush(film);

        assertThat(saved.getFilmId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Film");
    }

    //  VALIDATION


    @Test
    @DisplayName("Save film with blank title should throw ConstraintViolationException")
    void saveFilm_withBlankTitle_shouldThrow() {
        Film film = createValidFilm();
        film.setTitle(" ");

        assertThatThrownBy(() -> filmRepository.saveAndFlush(film))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save film with null title should throw ConstraintViolationException")
    void saveFilm_withNullTitle_shouldThrow() {
        Film film = createValidFilm();
        film.setTitle(null);

        assertThatThrownBy(() -> filmRepository.saveAndFlush(film))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save film without language should throw exception")
    void saveFilm_withoutLanguage_shouldThrow() {
        Film film = new Film();
        film.setTitle("Test Film");
        film.setRentalDuration((short) 3);
        film.setRentalRate(new BigDecimal("4.99"));
        film.setReplacementCost(new BigDecimal("19.99"));

        assertThatThrownBy(() -> filmRepository.saveAndFlush(film))
                .isInstanceOf(Exception.class);
    }
    // READ

    @Test
    @DisplayName("Find film by ID should return film")
    void findById_shouldReturnFilm() {
        Film film = createValidFilm();
        Film saved = filmRepository.saveAndFlush(film);

        Film found = filmRepository.findById(saved.getFilmId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Test Film");
    }

    @Test
    @DisplayName("Find all films should return list")
    void findAll_shouldReturnFilms() {
        filmRepository.save(createValidFilm());
        filmRepository.save(createValidFilm());

        var films = filmRepository.findAll();

        assertThat(films).hasSizeGreaterThanOrEqualTo(2);
    }

    // UPDATE

    @Test
    @DisplayName("Update film should modify existing record")
    void updateFilm_shouldUpdateFields() {
        Film film = createValidFilm();
        Film saved = filmRepository.saveAndFlush(film);

        saved.setTitle("Updated Film");
        Film updated = filmRepository.saveAndFlush(saved);

        assertThat(updated.getTitle()).isEqualTo("Updated Film");
    }


    // DELETE


    @Test
    @DisplayName("Delete film should remove record")
    void deleteFilm_shouldRemove() {
        Film film = createValidFilm();
        Film saved = filmRepository.saveAndFlush(film);

        filmRepository.deleteById(saved.getFilmId());
        filmRepository.flush();

        boolean exists = filmRepository.existsById(saved.getFilmId());

        assertThat(exists).isFalse();
    }


    // EXISTS


    @Test
    @DisplayName("ExistsById should return true if film exists")
    void existsById_shouldReturnTrue() {
        Film film = createValidFilm();
        Film saved = filmRepository.saveAndFlush(film);

        boolean exists = filmRepository.existsById(saved.getFilmId());

        assertThat(exists).isTrue();
    }
}