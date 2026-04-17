package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<Language> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/{languageId}")
    public ResponseEntity<Language> getLanguageById(@PathVariable Integer languageId) {
        return ResponseEntity.ok(languageService.getLanguageById(languageId));
    }

    @PostMapping
    public Language createLanguage(@RequestBody Language language) {
        return languageService.createLanguage(language);
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<Language> updateLanguage(@PathVariable Integer languageId,
                                                   @RequestBody Language updated) {
        return ResponseEntity.ok(languageService.updateLanguage(languageId, updated));
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Integer languageId) {
        languageService.deleteLanguage(languageId);
        return ResponseEntity.noContent().build();
    }
}
