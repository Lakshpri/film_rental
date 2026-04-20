package com.example.film_rental_app.master_datamodule.mapper;

import com.example.film_rental_app.master_datamodule.dto.request.LanguageRequestDTO;
import com.example.film_rental_app.master_datamodule.dto.response.LanguageResponseDTO;
import com.example.film_rental_app.master_datamodule.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public Language toEntity(LanguageRequestDTO dto) {
        Language language = new Language();
        language.setName(dto.getName());
        return language;
    }

    public LanguageResponseDTO toResponseDTO(Language language) {
        return new LanguageResponseDTO(
                language.getLanguageId(),
                language.getName(),
                language.getLastUpdate()
        );
    }

    public void updateEntity(Language language, LanguageRequestDTO dto) {
        language.setName(dto.getName());
    }
}
