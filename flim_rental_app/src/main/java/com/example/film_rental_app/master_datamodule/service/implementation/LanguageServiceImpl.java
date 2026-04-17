package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.exception.LanguageNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.LanguageRepository;
import com.example.film_rental_app.master_datamodule.service.LanguageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Language getLanguageById(Integer languageId) {
        return languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageNotFoundException(languageId));
    }

    @Override
    public Language createLanguage(Language language) {
        return languageRepository.save(language);
    }

    @Override
    public Language updateLanguage(Integer languageId, Language updated) {
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageNotFoundException(languageId));
        language.setName(updated.getName());
        return languageRepository.save(language);
    }

    @Override
    public boolean deleteLanguage(Integer languageId) {
        if (!languageRepository.existsById(languageId)) {
            throw new LanguageNotFoundException(languageId);
        }
        languageRepository.deleteById(languageId);
        return true;
    }
}
