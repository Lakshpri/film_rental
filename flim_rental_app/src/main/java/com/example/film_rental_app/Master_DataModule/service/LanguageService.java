package com.example.film_rental_app.Master_DataModule.service;

import com.example.film_rental_app.Master_DataModule.entity.Language;

import java.util.List;

public interface LanguageService {

    List<Language> getAllLanguages();

    Language getLanguageById(Integer languageId);

    Language createLanguage(Language language);

    Language updateLanguage(Integer languageId, Language updated);

    void deleteLanguage(Integer languageId);
}
