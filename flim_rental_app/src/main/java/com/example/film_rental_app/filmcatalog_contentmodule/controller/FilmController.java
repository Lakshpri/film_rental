package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmCategory;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }
    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getFilmById(filmId));
    }
    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        Film saved = filmService.createFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @PutMapping("/{filmId}")
    public ResponseEntity<Film> updateFilm(
            @PathVariable Integer filmId,
            @Valid @RequestBody Film updated) {

        Film updatedFilm = filmService.updateFilm(filmId, updated);
        return ResponseEntity.ok(updatedFilm);
    }
    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{filmId}/actors")
    public ResponseEntity<List<FilmActor>> getActorsByFilm(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getActorsByFilm(filmId));
    }

    @PostMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<FilmActor> addActorToFilm(@PathVariable Integer filmId,
                                                    @PathVariable Integer actorId) {
        FilmActor result = filmService.addActorToFilm(filmId, actorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<Void> removeActorFromFilm(@PathVariable Integer filmId,
                                                    @PathVariable Integer actorId) {
        filmService.removeActorFromFilm(filmId, actorId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{filmId}/categories")
    public ResponseEntity<List<FilmCategory>> getCategoriesByFilm(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getCategoriesByFilm(filmId));
    }

    @PostMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<FilmCategory> addCategoryToFilm(@PathVariable Integer filmId,
                                                          @PathVariable Integer categoryId) {
        FilmCategory result = filmService.addCategoryToFilm(filmId, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromFilm(@PathVariable Integer filmId,
                                                       @PathVariable Integer categoryId) {
        filmService.removeCategoryFromFilm(filmId, categoryId);
        return ResponseEntity.noContent().build();
    }
}