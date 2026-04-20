package com.example.film_rental_app.filmcatalog_contentmodule;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmText;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmTextRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class FilmTextRepositoryTest {

    @Autowired
    private FilmTextRepository filmTextRepository;

    @Test
    @DisplayName("Save FilmText with valid data should persist successfully")
    void saveFilmText_withValidData_shouldPersist() {
        FilmText filmText = new FilmText(1, "Inception", "Sci-fi movie");

        FilmText saved = filmTextRepository.save(filmText);

        assertThat(saved.getFilmId()).isEqualTo(1);
        assertThat(saved.getTitle()).isEqualTo("Inception");
    }

    @Test
    @DisplayName("Save FilmText with blank title should throw ConstraintViolationException")
    void saveFilmText_withBlankTitle_shouldThrow() {
        FilmText filmText = new FilmText(2, "  ", "Test");

        assertThatThrownBy(() -> filmTextRepository.saveAndFlush(filmText))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save FilmText with null title should throw ConstraintViolationException")
    void saveFilmText_withNullTitle_shouldThrow() {
        FilmText filmText = new FilmText(3, null, "Test");

        assertThatThrownBy(() -> filmTextRepository.saveAndFlush(filmText))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save FilmText with title exceeding 255 chars should throw ConstraintViolationException")
    void saveFilmText_withTooLongTitle_shouldThrow() {
        FilmText filmText = new FilmText(4, "A".repeat(256), "Test");

        assertThatThrownBy(() -> filmTextRepository.saveAndFlush(filmText))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Find FilmText by ID should return FilmText")
    void findById_shouldReturnFilmText() {
        FilmText filmText = new FilmText(5, "Avatar", "Fantasy");

        FilmText saved = filmTextRepository.saveAndFlush(filmText);

        FilmText found = filmTextRepository.findById(saved.getFilmId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Avatar");
    }

    @Test
    @DisplayName("Find all FilmTexts should return list")
    void findAll_shouldReturnFilmTexts() {
        FilmText f1 = new FilmText(6, "Movie1", "Desc1");
        FilmText f2 = new FilmText(7, "Movie2", "Desc2");

        filmTextRepository.save(f1);
        filmTextRepository.save(f2);

        var list = filmTextRepository.findAll();

        assertThat(list).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Update FilmText should modify existing record")
    void updateFilmText_shouldUpdateFields() {
        FilmText filmText = new FilmText(8, "Old Title", "Old Desc");

        FilmText saved = filmTextRepository.saveAndFlush(filmText);

        saved.setTitle("New Title");
        FilmText updated = filmTextRepository.saveAndFlush(saved);

        assertThat(updated.getTitle()).isEqualTo("New Title");
    }

    @Test
    @DisplayName("Delete FilmText should remove record")
    void deleteFilmText_shouldRemove() {
        FilmText filmText = new FilmText(9, "Delete Me", "Desc");

        FilmText saved = filmTextRepository.saveAndFlush(filmText);

        filmTextRepository.deleteById(saved.getFilmId());
        filmTextRepository.flush();

        boolean exists = filmTextRepository.existsById(saved.getFilmId());

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("existsByFilmId should return true if FilmText exists")
    void existsByFilmId_shouldReturnTrue() {
        FilmText filmText = new FilmText(10, "Check Exists", "Desc");

        FilmText saved = filmTextRepository.saveAndFlush(filmText);

        boolean exists = filmTextRepository.existsByFilmId(saved.getFilmId());

        assertThat(exists).isTrue();
    }
}