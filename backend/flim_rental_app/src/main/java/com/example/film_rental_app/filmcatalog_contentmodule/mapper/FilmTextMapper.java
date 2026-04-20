package com.example.film_rental_app.filmcatalog_contentmodule.mapper;

import com.example.film_rental_app.filmcatalog_contentmodule.dto.request.FilmTextRequestDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.FilmTextResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmText;
import org.springframework.stereotype.Component;

@Component
public class FilmTextMapper {

    public FilmText toEntity(FilmTextRequestDTO dto) {
        return new FilmText(dto.getFilmId(), dto.getTitle(), dto.getDescription());
    }

    public FilmTextResponseDTO toResponseDTO(FilmText filmText) {
        return new FilmTextResponseDTO(filmText.getFilmId(), filmText.getTitle(), filmText.getDescription());
    }

    public void updateEntity(FilmText filmText, FilmTextRequestDTO dto) {
        filmText.setTitle(dto.getTitle());
        filmText.setDescription(dto.getDescription());
    }
}
