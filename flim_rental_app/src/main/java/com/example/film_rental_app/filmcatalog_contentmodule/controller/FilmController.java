package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmCategory;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
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
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getFilmById(filmId));
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/{filmId}")
    public ResponseEntity<Film> updateFilm(@PathVariable Integer filmId,
                                           @RequestBody Film updated) {
        return ResponseEntity.ok(filmService.updateFilm(filmId, updated));
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
        return ResponseEntity.noContent().build();
    }

    // Film-Actor relationships
    @GetMapping("/{filmId}/actors")
    public ResponseEntity<List<FilmActor>> getActorsByFilm(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getActorsByFilm(filmId));
    }

    @PostMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<FilmActor> addActorToFilm(@PathVariable Integer filmId,
                                                    @PathVariable Integer actorId) {
        return ResponseEntity.ok(filmService.addActorToFilm(filmId, actorId));
    }

    @DeleteMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<Void> removeActorFromFilm(@PathVariable Integer filmId,
                                                    @PathVariable Integer actorId) {
        filmService.removeActorFromFilm(filmId, actorId);
        return ResponseEntity.noContent().build();
    }

    // Film-Category relationships
    @GetMapping("/{filmId}/categories")
    public ResponseEntity<List<FilmCategory>> getCategoriesByFilm(@PathVariable Integer filmId) {
        return ResponseEntity.ok(filmService.getCategoriesByFilm(filmId));
    }

    @PostMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<FilmCategory> addCategoryToFilm(@PathVariable Integer filmId,
                                                          @PathVariable Integer categoryId) {
        return ResponseEntity.ok(filmService.addCategoryToFilm(filmId, categoryId));
    }

    @DeleteMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromFilm(@PathVariable Integer filmId,
                                                       @PathVariable Integer categoryId) {
        filmService.removeCategoryFromFilm(filmId, categoryId);
        return ResponseEntity.noContent().build();
    }
}
