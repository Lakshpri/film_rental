package com.example.film_rental_app.FilmCatalog_ContentModule.service;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Film;
import com.example.film_rental_app.FilmCatalog_ContentModule.entity.FilmActor;
import com.example.film_rental_app.FilmCatalog_ContentModule.entity.FilmCategory;

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
