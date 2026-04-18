package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.dto.request.FilmRequestDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmActorResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmCategoryResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.FilmMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.service.LanguageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
public class FilmController {
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
    public ResponseEntity<FilmResponseDTO> getFilmById(@PathVariable Integer filmId) {
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
    public ResponseEntity<FilmResponseDTO> updateFilm(@PathVariable Integer filmId,
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
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer filmId) {
        filmService.deleteFilm(filmId);
        return ResponseEntity.noContent().build();
    }

    // Film-Actor sub-resources
    @GetMapping("/{filmId}/actors")
    public ResponseEntity<List<FilmActorResponseDTO>> getActorsByFilm(@PathVariable Integer filmId) {
        List<FilmActorResponseDTO> result = filmService.getActorsByFilm(filmId).stream()
                .map(filmMapper::toFilmActorResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<FilmActorResponseDTO> addActorToFilm(@PathVariable Integer filmId,
                                                               @PathVariable Integer actorId) {
        return ResponseEntity.status(201)
                .body(filmMapper.toFilmActorResponseDTO(filmService.addActorToFilm(filmId, actorId)));
    }

    @DeleteMapping("/{filmId}/actors/{actorId}")
    public ResponseEntity<Void> removeActorFromFilm(@PathVariable Integer filmId,
                                                    @PathVariable Integer actorId) {
        filmService.removeActorFromFilm(filmId, actorId);
        return ResponseEntity.noContent().build();
    }

    // Film-Category sub-resources
    @GetMapping("/{filmId}/categories")
    public ResponseEntity<List<FilmCategoryResponseDTO>> getCategoriesByFilm(@PathVariable Integer filmId) {
        List<FilmCategoryResponseDTO> result = filmService.getCategoriesByFilm(filmId).stream()
                .map(filmMapper::toFilmCategoryResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<FilmCategoryResponseDTO> addCategoryToFilm(@PathVariable Integer filmId,
                                                                     @PathVariable Integer categoryId) {
        return ResponseEntity.status(201)
                .body(filmMapper.toFilmCategoryResponseDTO(filmService.addCategoryToFilm(filmId, categoryId)));
    }

    @DeleteMapping("/{filmId}/categories/{categoryId}")
    public ResponseEntity<Void> removeCategoryFromFilm(@PathVariable Integer filmId,
                                                       @PathVariable Integer categoryId) {
        filmService.removeCategoryFromFilm(filmId, categoryId);
        return ResponseEntity.noContent().build();
    }
}
