package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.*;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.ActorRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmActorRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmCategoryRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmRepository;
import com.example.film_rental_app.master_datamodule.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final CategoryRepository categoryRepository;
    private final FilmActorRepository filmActorRepository;
    private final FilmCategoryRepository filmCategoryRepository;

    public FilmController(FilmRepository filmRepository, ActorRepository actorRepository,
                          CategoryRepository categoryRepository,
                          FilmActorRepository filmActorRepository,
                          FilmCategoryRepository filmCategoryRepository) {
        this.filmRepository = filmRepository;
        this.actorRepository = actorRepository;
        this.categoryRepository = categoryRepository;
        this.filmActorRepository = filmActorRepository;
        this.filmCategoryRepository = filmCategoryRepository;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer filmId) {
        return filmRepository.findById(filmId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmRepository.save(film);
    }

    @PutMapping("/{filmId}")
    public ResponseEntity<Film> updateFilm(@PathVariable Integer filmId, @RequestBody Film updated) {
        return filmRepository.findById(filmId).map(film -> {
            film.setTitle(updated.getTitle());
            film.setDescription(updated.getDescription());
            film.setReleaseYear(updated.getReleaseYear());
            film.setRentalDuration(updated.getRentalDuration());
            film.setRentalRate(updated.getRentalRate());
            film.setLength(updated.getLength());
            film.setReplacementCost(updated.getReplacementCost());
            film.setRating(updated.getRating());
            film.setSpecialFeatures(updated.getSpecialFeatures());
            if (updated.getLanguage() != null) film.setLanguage(updated.getLanguage());
            film.setOriginalLanguage(updated.getOriginalLanguage());
            return ResponseEntity.ok(filmRepository.save(film));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer filmId) {
        if (!filmRepository.existsById(filmId)) return ResponseEntity.notFound().build();
        filmRepository.deleteById(filmId);
        return ResponseEntity.noContent().build();
    }

    // Film-Actor relationships
    @GetMapping("/{filmId}/actors")
    public ResponseEntity<List<FilmActor>> getActorsByFilm(@PathVariable Integer filmId) {
        if (!filmRepository.existsById(filmId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(filmActorRepository.findById_FilmId(filmId));
    }

    @PostMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<FilmActor> addActorToFilm(@PathVariable Integer filmId, @PathVariable Integer actorId) {
        Film film = filmRepository.findById(filmId).orElse(null);
        Actor actor = actorRepository.findById(actorId).orElse(null);
        if (film == null || actor == null) return ResponseEntity.notFound().build();
        FilmActorId id = new FilmActorId(actorId, filmId);
        if (filmActorRepository.existsById(id)) return ResponseEntity.ok().build();
        FilmActor filmActor = new FilmActor();
        filmActor.setId(id);
        filmActor.setFilm(film);
        filmActor.setActor(actor);
        return ResponseEntity.ok(filmActorRepository.save(filmActor));
    }

    @DeleteMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<Void> removeActorFromFilm(@PathVariable Integer filmId, @PathVariable Integer actorId) {
        FilmActorId id = new FilmActorId(actorId, filmId);
        if (!filmActorRepository.existsById(id)) return ResponseEntity.notFound().build();
        filmActorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Film-Category relationships
    @GetMapping("/{filmId}/categories")
    public ResponseEntity<List<FilmCategory>> getCategoriesByFilm(@PathVariable Integer filmId) {
        if (!filmRepository.existsById(filmId)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(filmCategoryRepository.findById_FilmId(filmId));
    }

    @PostMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<FilmCategory> addCategoryToFilm(@PathVariable Integer filmId, @PathVariable Integer categoryId) {
        Film film = filmRepository.findById(filmId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (film == null || category == null) return ResponseEntity.notFound().build();
        FilmCategoryId id = new FilmCategoryId(filmId, categoryId);
        if (filmCategoryRepository.existsById(id)) return ResponseEntity.ok().build();
        FilmCategory filmCategory = new FilmCategory();
        filmCategory.setId(id);
        filmCategory.setFilm(film);
        filmCategory.setCategory(category);
        return ResponseEntity.ok(filmCategoryRepository.save(filmCategory));
    }

    @DeleteMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromFilm(@PathVariable Integer filmId, @PathVariable Integer categoryId) {
        FilmCategoryId id = new FilmCategoryId(filmId, categoryId);
        if (!filmCategoryRepository.existsById(id)) return ResponseEntity.notFound().build();
        filmCategoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
