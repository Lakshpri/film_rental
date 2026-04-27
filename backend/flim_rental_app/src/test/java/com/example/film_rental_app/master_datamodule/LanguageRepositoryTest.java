package com.example.film_rental_app.master_datamodule;

import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.repository.LanguageRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LanguageRepositoryTest {

    @Autowired private LanguageRepository languageRepository;

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
    void testUpdateLanguage() {
        Language lang = new Language();
        lang.setName("Spanish");
        Language saved = languageRepository.save(lang);

        saved.setName("Spanish Updated");
        Language updated = languageRepository.save(saved);

        assertEquals("Spanish Updated", updated.getName());
    }

    // Custom  @Query
    @Test
    void testFindByNameNative() {
        languageRepository.save(new Language(null, "Tamil", null, null, null));
        languageRepository.save(new Language(null, "English", null, null, null));

        List<Language> result = languageRepository.findByNameNative("Tam");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tamil", result.get(0).getName());
        System.out.println("Found: " + result.get(0).getName());
    }
}