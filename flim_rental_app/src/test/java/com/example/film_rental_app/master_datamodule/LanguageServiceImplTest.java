package com.example.film_rental_app.master_datamodule;

import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.exception.LanguageAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.LanguageNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.LanguageRepository;
import com.example.film_rental_app.master_datamodule.service.implementation.LanguageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LanguageServiceImplTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageServiceImpl languageService;

    private Language language;

    @BeforeEach
    void setUp() {
        language = new Language();
        language.setLanguageId(1);
        language.setName("English");
    }


    // POSITIVE TEST CASES


    @Test
    void testGetAllLanguages_Success() {
        when(languageRepository.findAll()).thenReturn(Arrays.asList(language));

        List<Language> result = languageService.getAllLanguages();

        assertEquals(1, result.size());
        verify(languageRepository).findAll();
    }

    @Test
    void testGetLanguageById_Success() {
        when(languageRepository.findById(1)).thenReturn(Optional.of(language));

        Language result = languageService.getLanguageById(1);

        assertNotNull(result);
        assertEquals("English", result.getName());
    }

    @Test
    void testCreateLanguage_Success() {
        when(languageRepository.existsByName("English")).thenReturn(false);
        when(languageRepository.save(language)).thenReturn(language);

        Language result = languageService.createLanguage(language);

        assertNotNull(result);
        verify(languageRepository).save(language);
    }

    @Test
    void testUpdateLanguage_Success() {
        Language updated = new Language();
        updated.setName("French");

        when(languageRepository.findById(1)).thenReturn(Optional.of(language));
        when(languageRepository.existsByName("French")).thenReturn(false);
        when(languageRepository.save(any(Language.class))).thenReturn(language);

        Language result = languageService.updateLanguage(1, updated);

        assertEquals("French", result.getName());
    }


    //  NEGATIVE TEST CASES


    @Test
    void testGetLanguageById_NotFound() {
        when(languageRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(LanguageNotFoundException.class,
                () -> languageService.getLanguageById(1));
    }

    @Test
    void testCreateLanguage_Duplicate() {
        when(languageRepository.existsByName("English")).thenReturn(true);

        assertThrows(LanguageAlreadyExistsException.class,
                () -> languageService.createLanguage(language));
    }

    @Test
    void testUpdateLanguage_NotFound() {
        Language updated = new Language();
        updated.setName("French");

        when(languageRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(LanguageNotFoundException.class,
                () -> languageService.updateLanguage(1, updated));
    }

    @Test
    void testUpdateLanguage_DuplicateName() {
        Language updated = new Language();
        updated.setName("French");

        when(languageRepository.findById(1)).thenReturn(Optional.of(language));
        when(languageRepository.existsByName("French")).thenReturn(true);

        assertThrows(LanguageAlreadyExistsException.class,
                () -> languageService.updateLanguage(1, updated));
    }

    @Test
    void testDeleteLanguage_NotFound() {
        when(languageRepository.existsById(1)).thenReturn(false);

        assertThrows(LanguageNotFoundException.class,
                () -> languageService.deleteLanguage(1));
    }
}