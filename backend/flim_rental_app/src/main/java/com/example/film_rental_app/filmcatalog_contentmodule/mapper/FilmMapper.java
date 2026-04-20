package com.example.film_rental_app.filmcatalog_contentmodule.mapper;

import com.example.film_rental_app.filmcatalog_contentmodule.dto.request.FilmRequestDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmActorResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmCategoryResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmCategory;
import com.example.film_rental_app.master_datamodule.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    public Film toEntity(FilmRequestDTO dto) {
        Film film = new Film();
        film.setTitle(dto.getTitle());
        film.setDescription(dto.getDescription());
        film.setReleaseYear(dto.getReleaseYear());
        film.setRentalDuration(dto.getRentalDuration());
        film.setRentalRate(dto.getRentalRate());
        film.setLength(dto.getLength());
        film.setReplacementCost(dto.getReplacementCost());
        film.setRating(dto.getRating());
        film.setSpecialFeatures(dto.getSpecialFeatures());
        Language lang = new Language();
        lang.setLanguageId(dto.getLanguageId());
        film.setLanguage(lang);
        if (dto.getOriginalLanguageId() != null) {
            Language origLang = new Language();
            origLang.setLanguageId(dto.getOriginalLanguageId());
            film.setOriginalLanguage(origLang);
        }
        return film;
    }

    public FilmResponseDTO toResponseDTO(Film film) {
        FilmResponseDTO dto = new FilmResponseDTO();
        dto.setFilmId(film.getFilmId());
        dto.setTitle(film.getTitle());
        dto.setDescription(film.getDescription());
        dto.setReleaseYear(film.getReleaseYear());
        dto.setRentalDuration(film.getRentalDuration());
        dto.setRentalRate(film.getRentalRate());
        dto.setLength(film.getLength());
        dto.setReplacementCost(film.getReplacementCost());
        dto.setRating(film.getRating());
        dto.setSpecialFeatures(film.getSpecialFeatures());
        dto.setLastUpdate(film.getLastUpdate());
        if (film.getLanguage() != null) {
            dto.setLanguageId(film.getLanguage().getLanguageId());
            dto.setLanguageName(film.getLanguage().getName());
        }
        if (film.getOriginalLanguage() != null) {
            dto.setOriginalLanguageId(film.getOriginalLanguage().getLanguageId());
            dto.setOriginalLanguageName(film.getOriginalLanguage().getName());
        }
        return dto;
    }

    public FilmActorResponseDTO toFilmActorResponseDTO(FilmActor filmActor) {
        return new FilmActorResponseDTO(
                filmActor.getId().getFilmId(),
                filmActor.getId().getActorId(),
                filmActor.getActor().getFirstName(),
                filmActor.getActor().getLastName(),
                filmActor.getLastUpdate()
        );
    }

    public FilmCategoryResponseDTO toFilmCategoryResponseDTO(FilmCategory filmCategory) {
        return new FilmCategoryResponseDTO(
                filmCategory.getId().getFilmId(),
                filmCategory.getId().getCategoryId(),
                filmCategory.getCategory().getName(),
                filmCategory.getLastUpdate()
        );
    }

    public void updateEntity(Film film, FilmRequestDTO dto) {
        film.setTitle(dto.getTitle());
        film.setDescription(dto.getDescription());
        film.setReleaseYear(dto.getReleaseYear());
        film.setRentalDuration(dto.getRentalDuration());
        film.setRentalRate(dto.getRentalRate());
        film.setLength(dto.getLength());
        film.setReplacementCost(dto.getReplacementCost());
        film.setRating(dto.getRating());
        film.setSpecialFeatures(dto.getSpecialFeatures());
    }
}
