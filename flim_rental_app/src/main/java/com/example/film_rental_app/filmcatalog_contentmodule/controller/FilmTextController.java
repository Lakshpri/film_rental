package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.dto.request.FilmTextRequestDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmTextResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmText;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.FilmTextMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmTextService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/film-texts")
@Validated
public class FilmTextController {

    @Autowired
    private FilmTextService filmTextService;
    @Autowired
    private FilmTextMapper filmTextMapper;

    @GetMapping
    public ResponseEntity<List<FilmTextResponseDTO>> getAllFilmTexts() {
        List<FilmTextResponseDTO> result = filmTextService.getAllFilmTexts().stream()
                .map(filmTextMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{filmId}")
    public ResponseEntity<FilmTextResponseDTO> getFilmTextById(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId) {
        return ResponseEntity.ok(filmTextMapper.toResponseDTO(filmTextService.getFilmTextById(filmId)));
    }

    @PostMapping
    public ResponseEntity<FilmTextResponseDTO> createFilmText(@Valid @RequestBody FilmTextRequestDTO dto) {
        FilmText filmText = filmTextMapper.toEntity(dto);
        return ResponseEntity.status(201).body(filmTextMapper.toResponseDTO(filmTextService.createFilmText(filmText)));
    }

    @PutMapping("/{filmId}")
    public ResponseEntity<FilmTextResponseDTO> updateFilmText(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId,
            @Valid @RequestBody FilmTextRequestDTO dto) {
        FilmText existing = filmTextService.getFilmTextById(filmId);
        filmTextMapper.updateEntity(existing, dto);
        return ResponseEntity.ok(filmTextMapper.toResponseDTO(filmTextService.updateFilmText(filmId, existing)));
    }

    @DeleteMapping("/{filmId}")
    public ResponseEntity<Void> deleteFilmText(
            @PathVariable @Positive(message = "Film ID must be a positive number") Integer filmId) {
        filmTextService.deleteFilmText(filmId);
        return ResponseEntity.noContent().build();
    }
}
