package com.example.film_rental_app.filmcatalog_contentmodule;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmText;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmTextAlreadyExistsException;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmTextNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmTextRepository;
import com.example.film_rental_app.filmcatalog_contentmodule.service.implementation.FilmTextServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmTextServiceImplTest {

    @Mock
    private FilmTextRepository filmTextRepository;

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmTextServiceImpl filmTextService;

    // ---------------------- POSITIVE TEST CASES (5) ----------------------

    @Test
    void testGetAllFilmTexts_Success() {
        when(filmTextRepository.findAll()).thenReturn(List.of(new FilmText(), new FilmText()));

        List<FilmText> result = filmTextService.getAllFilmTexts();

        assertEquals(2, result.size());
    }

    @Test
    void testGetFilmTextById_Success() {
        FilmText filmText = new FilmText(1, "Title", "Desc");

        when(filmTextRepository.findById(1)).thenReturn(Optional.of(filmText));

        FilmText result = filmTextService.getFilmTextById(1);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    void testCreateFilmText_Success() {
        FilmText filmText = new FilmText(1, "Title", "Desc");

        when(filmRepository.existsById(1)).thenReturn(true);
        when(filmTextRepository.existsByFilmId(1)).thenReturn(false);
        when(filmTextRepository.save(filmText)).thenReturn(filmText);

        FilmText result = filmTextService.createFilmText(filmText);

        assertNotNull(result);
        assertEquals(1, result.getFilmId());
    }

    @Test
    void testUpdateFilmText_Success() {
        FilmText existing = new FilmText(1, "Old", "Old Desc");
        FilmText updated = new FilmText(1, "New", "New Desc");

        when(filmTextRepository.findById(1)).thenReturn(Optional.of(existing));
        when(filmTextRepository.save(any())).thenReturn(existing);

        FilmText result = filmTextService.updateFilmText(1, updated);

        assertEquals("New", result.getTitle());
    }

    @Test
    void testDeleteFilmText_Success() {
        when(filmTextRepository.existsById(1)).thenReturn(true);

        boolean result = filmTextService.deleteFilmText(1);

        assertTrue(result);
        verify(filmTextRepository).deleteById(1);
    }

    // ---------------------- NEGATIVE TEST CASES (5) ----------------------

    @Test
    void testGetFilmTextById_NotFound() {
        when(filmTextRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(FilmTextNotFoundException.class,
                () -> filmTextService.getFilmTextById(1));
    }

    @Test
    void testCreateFilmText_FilmNotFound() {
        FilmText filmText = new FilmText(1, "Title", "Desc");

        when(filmRepository.existsById(1)).thenReturn(false);

        assertThrows(FilmNotFoundException.class,
                () -> filmTextService.createFilmText(filmText));
    }

    @Test
    void testCreateFilmText_AlreadyExists() {
        FilmText filmText = new FilmText(1, "Title", "Desc");

        when(filmRepository.existsById(1)).thenReturn(true);
        when(filmTextRepository.existsByFilmId(1)).thenReturn(true);

        assertThrows(FilmTextAlreadyExistsException.class,
                () -> filmTextService.createFilmText(filmText));
    }

    @Test
    void testUpdateFilmText_NotFound() {
        when(filmTextRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(FilmTextNotFoundException.class,
                () -> filmTextService.updateFilmText(1, new FilmText()));
    }

    @Test
    void testDeleteFilmText_NotFound() {
        when(filmTextRepository.existsById(1)).thenReturn(false);

        assertThrows(FilmTextNotFoundException.class,
                () -> filmTextService.deleteFilmText(1));
    }
}