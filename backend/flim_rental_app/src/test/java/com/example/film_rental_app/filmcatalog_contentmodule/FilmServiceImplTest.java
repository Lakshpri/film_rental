package com.example.film_rental_app.filmcatalog_contentmodule;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.*;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.*;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.*;
import com.example.film_rental_app.filmcatalog_contentmodule.service.implementation.FilmServiceImpl;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.exception.CategoryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceImplTest {

    @Mock private FilmRepository filmRepository;
    @Mock private ActorRepository actorRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private FilmActorRepository filmActorRepository;
    @Mock private FilmCategoryRepository filmCategoryRepository;

    @InjectMocks
    private FilmServiceImpl filmService;

    // ---------------------- POSITIVE TEST CASES----------------------

    @Test
    void testGetFilmById_Success() {
        Film film = new Film();
        film.setFilmId(1);

        when(filmRepository.findById(1)).thenReturn(Optional.of(film));

        Film result = filmService.getFilmById(1);

        assertEquals(1, result.getFilmId());
    }

    @Test
    void testCreateFilm_Success() {
        Film film = new Film();
        film.setTitle("Inception");

        when(filmRepository.existsByTitle("Inception")).thenReturn(false);
        when(filmRepository.save(film)).thenReturn(film);

        Film result = filmService.createFilm(film);

        assertEquals("Inception", result.getTitle());
    }

    @Test
    void testUpdateFilm_Success() {
        Film existing = new Film();
        existing.setFilmId(1);
        existing.setTitle("Old");

        Film updated = new Film();
        updated.setTitle("New");

        when(filmRepository.findById(1)).thenReturn(Optional.of(existing));
        when(filmRepository.existsByTitle("New")).thenReturn(false);
        when(filmRepository.save(any())).thenReturn(existing);

        Film result = filmService.updateFilm(1, updated);

        assertEquals("New", result.getTitle());
    }

    @Test
    void testAddActorToFilm_Success() {
        Film film = new Film();
        Actor actor = new Actor();

        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(actorRepository.findById(2)).thenReturn(Optional.of(actor));
        when(filmActorRepository.existsById(any())).thenReturn(false);
        when(filmActorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        FilmActor result = filmService.addActorToFilm(1, 2);

        assertNotNull(result);
    }

    // ---------------------- NEGATIVE TEST CASES----------------------

    @Test
    void testGetFilmById_NotFound() {
        when(filmRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(FilmNotFoundException.class,
                () -> filmService.getFilmById(1));
    }

    @Test
    void testCreateFilm_Duplicate() {
        Film film = new Film();
        film.setTitle("Inception");

        when(filmRepository.existsByTitle("Inception")).thenReturn(true);

        assertThrows(FilmAlreadyExistsException.class,
                () -> filmService.createFilm(film));
    }

    @Test
    void testUpdateFilm_DuplicateTitle() {
        Film existing = new Film();
        existing.setTitle("Old");

        Film updated = new Film();
        updated.setTitle("New");

        when(filmRepository.findById(1)).thenReturn(Optional.of(existing));
        when(filmRepository.existsByTitle("New")).thenReturn(true);

        assertThrows(FilmAlreadyExistsException.class,
                () -> filmService.updateFilm(1, updated));
    }
    @Test
    void testAddCategoryToFilm_CategoryNotFound() {
        Film film = new Film();

        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        when(categoryRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> filmService.addCategoryToFilm(1, 2));
    }
}