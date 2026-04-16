package com.example.film_rental_app.filmcatalog_contentmodule.service;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmActor;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmCategory;

import java.util.List;

public interface FilmService {

    List<Film> getAllFilms();

    Film getFilmById(Integer filmId);

    Film createFilm(Film film);

    Film updateFilm(Integer filmId, Film updated);

    void deleteFilm(Integer filmId);

    List<FilmActor> getActorsByFilm(Integer filmId);

    FilmActor addActorToFilm(Integer filmId, Integer actorId);

    void removeActorFromFilm(Integer filmId, Integer actorId);

    List<FilmCategory> getCategoriesByFilm(Integer filmId);

    FilmCategory addCategoryToFilm(Integer filmId, Integer categoryId);

    void removeCategoryFromFilm(Integer filmId, Integer categoryId);
}
