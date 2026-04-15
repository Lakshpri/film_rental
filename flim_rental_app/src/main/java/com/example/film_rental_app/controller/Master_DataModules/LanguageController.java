package com.example.film_rental_app.controller.Master_DataModules;

import com.example.film_rental_app.entity.Master_DataModules.Language;
import com.example.film_rental_app.repository.Master_DataModules.LanguageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageRepository languageRepository;

    public LanguageController(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @GetMapping
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @GetMapping("/{languageId}")
    public ResponseEntity<Language> getLanguageById(@PathVariable Integer languageId) {
        return languageRepository.findById(languageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Language createLanguage(@RequestBody Language language) {
        return languageRepository.save(language);
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<Language> updateLanguage(@PathVariable Integer languageId, @RequestBody Language updated) {
        return languageRepository.findById(languageId).map(lang -> {
            lang.setName(updated.getName());
            return ResponseEntity.ok(languageRepository.save(lang));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Integer languageId) {
        if (!languageRepository.existsById(languageId)) return ResponseEntity.notFound().build();
        languageRepository.deleteById(languageId);
        return ResponseEntity.noContent().build();
    }
}
