package com.example.film_rental_app.filmcatalog_contentmodule.service.implementation;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmText;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmTextAlreadyExistsException;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmTextNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmTextRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.service.FilmTextService;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FilmTextServiceImpl implements FilmTextService {

    @Autowired
    private FilmTextRepository filmTextRepository;
    @Autowired
    private FilmRepository filmRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FilmText> getAllFilmTexts() {
        return filmTextRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FilmText getFilmTextById(Integer filmId) {
        return filmTextRepository.findById(filmId)
                .orElseThrow(() -> new FilmTextNotFoundException(filmId));
    }

    @Override
    public FilmText createFilmText(FilmText filmText) {
        // Film must exist
        if (!filmRepository.existsById(filmText.getFilmId())) {
            throw new FilmNotFoundException(filmText.getFilmId());
        }
        // One FilmText per Film
        if (filmTextRepository.existsByFilmId(filmText.getFilmId())) {
            throw new FilmTextAlreadyExistsException(filmText.getFilmId());
        }
        return filmTextRepository.save(filmText);
    }

    @Override
    public FilmText updateFilmText(Integer filmId, FilmText updated) {
        FilmText filmText = filmTextRepository.findById(filmId)
                .orElseThrow(() -> new FilmTextNotFoundException(filmId));
        filmText.setTitle(updated.getTitle());
        filmText.setDescription(updated.getDescription());
        return filmTextRepository.save(filmText);
    }

    @Override
    public boolean deleteFilmText(Integer filmId) {
        if (!filmTextRepository.existsById(filmId)) {
            throw new FilmTextNotFoundException(filmId);
        }
        filmTextRepository.deleteById(filmId);
        return true;
    }
}
