package com.example.film_rental_app.master_datamodule.service;

import com.example.film_rental_app.master_datamodule.entity.Language;

import java.util.List;

public interface LanguageService {

    List<Language> getAllLanguages();

    Language getLanguageById(Integer languageId);

    Language createLanguage(Language language);

    Language updateLanguage(Integer languageId, Language updated);

    boolean deleteLanguage(Integer languageId);
}
