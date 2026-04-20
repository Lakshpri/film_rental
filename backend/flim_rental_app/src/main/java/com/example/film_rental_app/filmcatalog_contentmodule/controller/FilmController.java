package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.dto.request.FilmRequestDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmActorResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmCategoryResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.FilmMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.service.CategoryService;
import com.example.film_rental_app.master_datamodule.service.LanguageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
@Validated
public class FilmController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private FilmService filmService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private FilmMapper filmMapper;

    @GetMapping
    public ResponseEntity<List<FilmResponseDTO>> getAllFilms() {
        List<FilmResponseDTO> result = filmService.getAllFilms().stream()
                .map(filmMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<FilmResponseDTO> getFilmById(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId) {
        return ResponseEntity.ok(filmMapper.toResponseDTO(filmService.getFilmById(filmId)));
    }

    @PostMapping
    public ResponseEntity<FilmResponseDTO> createFilm(@Valid @RequestBody FilmRequestDTO dto) {
        Film film = filmMapper.toEntity(dto);
        Language language = languageService.getLanguageById(dto.getLanguageId());
        film.setLanguage(language);
        if (dto.getOriginalLanguageId() != null) {
            film.setOriginalLanguage(languageService.getLanguageById(dto.getOriginalLanguageId()));
        }
        return ResponseEntity.status(201).body(filmMapper.toResponseDTO(filmService.createFilm(film)));
    }

    @PutMapping("/{filmId}")
    public ResponseEntity<FilmResponseDTO> updateFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId,
            @Valid @RequestBody FilmRequestDTO dto) {
        Film existing = filmService.getFilmById(filmId);
        filmMapper.updateEntity(existing, dto);
        Language language = languageService.getLanguageById(dto.getLanguageId());
        existing.setLanguage(language);
        if (dto.getOriginalLanguageId() != null) {
            existing.setOriginalLanguage(languageService.getLanguageById(dto.getOriginalLanguageId()));
        }
        return ResponseEntity.ok(filmMapper.toResponseDTO(filmService.updateFilm(filmId, existing)));
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<Map<String, Object>> deleteFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId) {

        filmService.getFilmById(filmId); // throws FilmNotFoundException (404) if not found
        filmService.deleteFilm(filmId);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Film with ID " + filmId + " has been successfully deleted."
        ));
    }

    @GetMapping("/{filmId}/actors")
    public ResponseEntity<List<FilmActorResponseDTO>> getActorsByFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId) {
        List<FilmActorResponseDTO> result = filmService.getActorsByFilm(filmId).stream()
                .map(filmMapper::toFilmActorResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<FilmActorResponseDTO> addActorToFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId,
            @PathVariable @Positive(message = "Actor ID must be a positive number") Integer actorId) {
        return ResponseEntity.status(201)
                .body(filmMapper.toFilmActorResponseDTO(filmService.addActorToFilm(filmId, actorId)));
    }

    @DeleteMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<Map<String, Object>> removeActorFromFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId,
            @PathVariable @Positive(message = "Actor ID must be a positive number") Integer actorId) {

        filmService.getFilmById(filmId);   // throws FilmNotFoundException (404)
        actorService.getActorById(actorId); // throws ActorNotFoundException (404)

        filmService.removeActorFromFilm(filmId, actorId);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Actor with ID " + actorId +
                        " has been successfully removed from Film with ID " + filmId + "."
        ));
    }

    @GetMapping("/{filmId}/categories")
    public ResponseEntity<List<FilmCategoryResponseDTO>> getCategoriesByFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId) {
        List<FilmCategoryResponseDTO> result = filmService.getCategoriesByFilm(filmId).stream()
                .map(filmMapper::toFilmCategoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<FilmCategoryResponseDTO> addCategoryToFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId,
            @PathVariable @Positive(message = "Category ID must be a positive number") Integer categoryId) {
        return ResponseEntity.status(201)
                .body(filmMapper.toFilmCategoryResponseDTO(filmService.addCategoryToFilm(filmId, categoryId)));
    }

    @DeleteMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<Map<String, Object>> removeCategoryFromFilm(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId,
            @PathVariable @Positive(message = "Category ID must be a positive number") Integer categoryId) {

        filmService.getFilmById(filmId);       // throws FilmNotFoundException (404)
        categoryService.getCategoryById(categoryId); // throws CategoryNotFoundException (404)

        filmService.removeCategoryFromFilm(filmId, categoryId);

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Category with ID " + categoryId +
                        " has been successfully removed from Film with ID " + filmId + "."
        ));
    }
}
