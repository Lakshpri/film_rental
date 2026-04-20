package com.example.film_rental_app.filmcatalog_contentmodule;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.*;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmCategoryRepository;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.entity.Language;
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
class FilmCategoryRepositoryTest {

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;

    @Autowired
    private EntityManager entityManager;


    //  Helper Methods


    private Film createFilm() {
        Language language = new Language();
        language.setName("English");
        entityManager.persist(language);

        Film film = new Film();
        film.setTitle("Test Film");
        film.setRentalDuration((short) 3);
        film.setRentalRate(new java.math.BigDecimal("4.99"));
        film.setReplacementCost(new java.math.BigDecimal("19.99"));
        film.setLanguage(language);

        entityManager.persist(film);
        return film;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setName("Action");
        entityManager.persist(category);
        return category;
    }

    private FilmCategory createFilmCategory(Film film, Category category) {
        FilmCategoryId id = new FilmCategoryId(film.getFilmId(), category.getCategoryId());

        FilmCategory fc = new FilmCategory();
        fc.setId(id);
        fc.setFilm(film);
        fc.setCategory(category);

        return filmCategoryRepository.saveAndFlush(fc);
    }


    // CREATE


    @Test
    @DisplayName("Save FilmCategory with valid data should persist successfully")
    void saveFilmCategory_withValidData_shouldPersist() {
        Film film = createFilm();
        Category category = createCategory();

        FilmCategory saved = createFilmCategory(film, category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFilm()).isNotNull();
        assertThat(saved.getCategory()).isNotNull();
    }


    //  VALIDATION

    @Test
    @DisplayName("Save FilmCategory without film should throw exception")
    void saveFilmCategory_withoutFilm_shouldThrow() {
        Category category = createCategory();

        FilmCategory fc = new FilmCategory();
        fc.setCategory(category);
        fc.setId(new FilmCategoryId(null, category.getCategoryId()));

        assertThatThrownBy(() -> filmCategoryRepository.saveAndFlush(fc))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Save FilmCategory without category should throw exception")
    void saveFilmCategory_withoutCategory_shouldThrow() {
        Film film = createFilm();

        FilmCategory fc = new FilmCategory();
        fc.setFilm(film);
        fc.setId(new FilmCategoryId(film.getFilmId(), null));

        assertThatThrownBy(() -> filmCategoryRepository.saveAndFlush(fc))
                .isInstanceOf(Exception.class);
    }




    // READ


    @Test
    @DisplayName("Find FilmCategory by ID should return record")
    void findById_shouldReturnFilmCategory() {
        Film film = createFilm();
        Category category = createCategory();

        FilmCategory saved = createFilmCategory(film, category);

        FilmCategory found = filmCategoryRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId().getFilmId()).isEqualTo(film.getFilmId());
    }

    @Test
    @DisplayName("Find FilmCategory by filmId should return list")
    void findByFilmId_shouldReturnList() {
        Film film = createFilm();
        Category c1 = createCategory();
        Category c2 = createCategory();

        createFilmCategory(film, c1);
        createFilmCategory(film, c2);

        List<FilmCategory> list = filmCategoryRepository.findById_FilmId(film.getFilmId());

        assertThat(list).hasSize(2);
    }


    // UPDATE (Not allowed)


    @Test
    @DisplayName("Update FilmCategory should fail when changing ID")
    void updateFilmCategory_shouldFail() {
        Film film = createFilm();
        Category c1 = createCategory();
        Category c2 = createCategory();

        FilmCategory saved = createFilmCategory(film, c1);

        saved.setCategory(c2);
        saved.setId(new FilmCategoryId(film.getFilmId(), c2.getCategoryId()));

        assertThatThrownBy(() -> filmCategoryRepository.saveAndFlush(saved))
                .isInstanceOf(Exception.class);
    }

    // ======================
    // DELETE
    // ======================

    @Test
    @DisplayName("Delete FilmCategory should remove record")
    void deleteFilmCategory_shouldRemove() {
        Film film = createFilm();
        Category category = createCategory();

        FilmCategory saved = createFilmCategory(film, category);

        filmCategoryRepository.deleteById(saved.getId());
        filmCategoryRepository.flush();

        boolean exists = filmCategoryRepository.existsById(saved.getId());

        assertThat(exists).isFalse();
    }
}