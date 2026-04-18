package com.example.film_rental_app.master_datamodule.controller;

import com.example.film_rental_app.master_datamodule.dto.request.LanguageRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.LanguageResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Language;
import com.example.film_rental_app.master_datamodule.mapper.LanguageMapper;
import com.example.film_rental_app.master_datamodule.service.LanguageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LanguageMapper languageMapper;

    @GetMapping
    public ResponseEntity<List<LanguageResponseDTO>> getAllLanguages() {
        List<LanguageResponseDTO> result = languageService.getAllLanguages().stream()
                .map(languageMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{languageId}")
    public ResponseEntity<LanguageResponseDTO> getLanguageById(@PathVariable Integer languageId) {
        return ResponseEntity.ok(languageMapper.toResponseDTO(languageService.getLanguageById(languageId)));
    }

    @PostMapping
    public ResponseEntity<LanguageResponseDTO> createLanguage(@Valid @RequestBody LanguageRequestDTO dto) {
        Language language = languageMapper.toEntity(dto);
        return ResponseEntity.status(201).body(languageMapper.toResponseDTO(languageService.createLanguage(language)));
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<LanguageResponseDTO> updateLanguage(@PathVariable Integer languageId,
                                                              @Valid @RequestBody LanguageRequestDTO dto) {
        Language existing = languageService.getLanguageById(languageId);
        languageMapper.updateEntity(existing, dto);
        return ResponseEntity.ok(languageMapper.toResponseDTO(languageService.updateLanguage(languageId, existing)));
    }

    @DeleteMapping("/{languageId}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Integer languageId) {
        languageService.deleteLanguage(languageId);
        return ResponseEntity.noContent().build();
    }
}
