package com.example.film_rental_app.master_datamodule.service.implementation;

import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmRepository;
import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.exception.LanguageAlreadyExistsException;
import com.example.film_rental_app.master_datamodule.exception.LanguageInvalidOperationException;
import com.example.film_rental_app.master_datamodule.exception.LanguageNotFoundException;
import com.example.film_rental_app.master_datamodule.repository.LanguageRepository;
import com.example.film_rental_app.master_datamodule.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {

    @Autowired private LanguageRepository languageRepository;
    @Autowired private FilmRepository filmRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Language> getAllLanguages() {
        return languageRepository.findAll(Sort.by("languageId"));
    }

    @Override
    @Transactional(readOnly = true)
    public Language getLanguageById(Integer languageId) {
        return languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageNotFoundException(languageId));
    }

    @Override
    public Language createLanguage(Language language) {
        if (languageRepository.existsByName(language.getName())) {
            throw new LanguageAlreadyExistsException(language.getName());
        }
        return languageRepository.save(language);
    }

    @Override
    public Language updateLanguage(Integer languageId, Language updated) {
        Language language = languageRepository.findById(languageId)
                .orElseThrow(() -> new LanguageNotFoundException(languageId));
        if (!language.getName().equalsIgnoreCase(updated.getName())
                && languageRepository.existsByName(updated.getName())) {
            throw new LanguageAlreadyExistsException(updated.getName());
        }
        language.setName(updated.getName());
        return languageRepository.save(language);
    }

    @Override
    public boolean deleteLanguage(Integer languageId) {
        if (!languageRepository.existsById(languageId)) {
            throw new LanguageNotFoundException(languageId);
        }
        if (filmRepository.existsByLanguage_LanguageId(languageId)) {
            throw new LanguageInvalidOperationException(languageId,
                    "This language is still being used by one or more films. "
                            + "Please update those films to use a different language before deleting this one.");
        }
        languageRepository.deleteById(languageId);
        return true;
    }
}
