package com.example.film_rental_app.filmcatalog_contentmodule.service;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmText;

import java.util.List;

public interface FilmTextService {
    List<FilmText> getAllFilmTexts();
    FilmText getFilmTextById(Integer filmId);
    FilmText createFilmText(FilmText filmText);
    FilmText updateFilmText(Integer filmId, FilmText updated);
    boolean deleteFilmText(Integer filmId);
}
