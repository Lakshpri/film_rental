package com.example.film_rental_app.filmcatalog_contentmodule.service.implementation;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.*;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.*;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.*;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmService;
import com.example.film_rental_app.master_datamodule.entity.Category;
import com.example.film_rental_app.master_datamodule.exception.CategoryNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FilmServiceImpl implements FilmService {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FilmActorRepository filmActorRepository;
    @Autowired
    private FilmCategoryRepository filmCategoryRepository;
    @Autowired
    private FilmTextRepository filmTextRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Film getFilmById(Integer filmId) {
        return filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getTitle() != null && filmRepository.existsByTitle(film.getTitle())) {
            throw new FilmAlreadyExistsException(film.getTitle());
        }
        return filmRepository.save(film);
    }

    @Override
    public Film updateFilm(Integer filmId, Film updated) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        if (updated.getTitle() != null
                && !film.getTitle().equalsIgnoreCase(updated.getTitle())
                && filmRepository.existsByTitle(updated.getTitle())) {
            throw new FilmAlreadyExistsException(updated.getTitle());
        }
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
        return filmRepository.save(film);
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        if (!filmRepository.existsById(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        // BUG FIX: check film_text FK before delete
        if (filmTextRepository.existsByFilmId(filmId)) {
            throw new FilmInvalidOperationException(filmId,
                    "This Film has a FilmText entry linked to it. Delete the FilmText record first.");
        }
        if (!filmActorRepository.findById_FilmId(filmId).isEmpty()) {
            throw new FilmInvalidOperationException(filmId,
                    "This Film still has Actors linked to it. Remove all Actor associations first.");
        }
        if (!filmCategoryRepository.findById_FilmId(filmId).isEmpty()) {
            throw new FilmInvalidOperationException(filmId,
                    "This Film still has Categories linked to it. Remove all Category associations first.");
        }
        filmRepository.deleteById(filmId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilmActor> getActorsByFilm(Integer filmId) {
        if (!filmRepository.existsById(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        // BUG FIX: repository now uses JOIN FETCH actor — no LazyInitializationException
        return filmActorRepository.findById_FilmId(filmId);
    }

    @Override
    public FilmActor addActorToFilm(Integer filmId, Integer actorId) {
        Film  film  = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNotFoundException(actorId));
        FilmActorId id = new FilmActorId(actorId, filmId);
        if (filmActorRepository.existsById(id)) {
            throw new FilmActorAssociationException(filmId, actorId);
        }
        FilmActor filmActor = new FilmActor();
        filmActor.setId(id);
        filmActor.setFilm(film);
        filmActor.setActor(actor);
        return filmActorRepository.save(filmActor);
    }

    @Override
    public boolean removeActorFromFilm(Integer filmId, Integer actorId) {
        FilmActorId id = new FilmActorId(actorId, filmId);
        if (!filmActorRepository.existsById(id)) {
            throw new FilmActorNotFoundException(filmId, actorId);
        }
        filmActorRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilmCategory> getCategoriesByFilm(Integer filmId) {
        if (!filmRepository.existsById(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        return filmCategoryRepository.findById_FilmId(filmId);
    }

    @Override
    public FilmCategory addCategoryToFilm(Integer filmId, Integer categoryId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        FilmCategoryId id = new FilmCategoryId(filmId, categoryId);
        if (filmCategoryRepository.existsById(id)) {
            throw new FilmCategoryAssociationException(filmId, categoryId);
        }
        FilmCategory filmCategory = new FilmCategory();
        filmCategory.setId(id);
        filmCategory.setFilm(film);
        filmCategory.setCategory(category);
        return filmCategoryRepository.save(filmCategory);
    }

    @Override
    public boolean removeCategoryFromFilm(Integer filmId, Integer categoryId) {
        FilmCategoryId id = new FilmCategoryId(filmId, categoryId);
        if (!filmCategoryRepository.existsById(id)) {
            throw new FilmCategoryNotFoundException(filmId, categoryId);
        }
        filmCategoryRepository.deleteById(id);
        return true;
    }
}
