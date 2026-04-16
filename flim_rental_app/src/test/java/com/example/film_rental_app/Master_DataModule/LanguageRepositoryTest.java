package com.example.film_rental_app.Master_DataModule;

import com.example.film_rental_app.Master_DataModule.entity.Language;
import com.example.film_rental_app.Master_DataModule.repository.LanguageRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LanguageRepositoryTest {

    @Autowired private LanguageRepository languageRepository;

    @Test
    @DisplayName("Save language with valid name should persist")
    void saveLanguage_withValidData_shouldPersist() {
        Language lang = new Language();
        lang.setName("French");

        Language saved = languageRepository.saveAndFlush(lang);
        assertThat(saved.getLanguageId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("French");
    }

    @Test
    @DisplayName("Blank language name should throw ConstraintViolationException")
    void saveLanguage_withBlankName_shouldThrow() {
        Language lang = new Language();
        lang.setName("   ");

        assertThatThrownBy(() -> languageRepository.saveAndFlush(lang))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Null language name should throw ConstraintViolationException")
    void saveLanguage_withNullName_shouldThrow() {
        Language lang = new Language();
        lang.setName(null);

        assertThatThrownBy(() -> languageRepository.saveAndFlush(lang))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Language name exactly 20 chars should persist (boundary)")
    void saveLanguage_withNameAtMaxSize_shouldPersist() {
        Language lang = new Language();
        lang.setName("L".repeat(20));

        Language saved = languageRepository.saveAndFlush(lang);
        assertThat(saved.getLanguageId()).isNotNull();
    }

    @Test
    @DisplayName("Language name exceeding 20 chars should throw ConstraintViolationException")
    void saveLanguage_withTooLongName_shouldThrow() {
        Language lang = new Language();
        lang.setName("L".repeat(21));

        assertThatThrownBy(() -> languageRepository.saveAndFlush(lang))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
