package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.exception.LanguageAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.LanguageNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.LanguageRepository;
import com.example.film_rental_app.master_datamodule.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {
    @Autowired
    private LanguageRepository languageRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Language getLanguageById(Integer languageId) {
        // ResourceNotFoundException → HTTP 404
        return languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageNotFoundException(languageId));
    }

    @Override
    public Language createLanguage(Language language) {
        // DuplicateResourceException → HTTP 409
        if (languageRepository.existsByName(language.getName())) {
            throw new LanguageAlreadyExistsException(language.getName());
        }
        return languageRepository.save(language);
    }

    @Override
    public Language updateLanguage(Integer languageId, Language updated) {
        // ResourceNotFoundException → HTTP 404
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageNotFoundException(languageId));
        // DuplicateResourceException → HTTP 409
        if (!language.getName().equalsIgnoreCase(updated.getName())
                && languageRepository.existsByName(updated.getName())) {
            throw new LanguageAlreadyExistsException(updated.getName());
        }
        language.setName(updated.getName());
        return languageRepository.save(language);
    }

    @Override
    public boolean deleteLanguage(Integer languageId) {
        // ResourceNotFoundException → HTTP 404
        if (!languageRepository.existsById(languageId)) {
            throw new LanguageNotFoundException(languageId);
        }
        // InvalidOperationException → HTTP 400
        // Guard: cannot delete a Language that is still in use by Films
        // (add film language check here if FilmRepository is injected in future)
        languageRepository.deleteById(languageId);
        return true;
    }
}
