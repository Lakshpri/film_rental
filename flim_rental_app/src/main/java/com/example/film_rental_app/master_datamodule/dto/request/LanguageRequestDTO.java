package com.example.film_rental_app.master_datamodule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LanguageRequestDTO {

    @NotBlank(message = "Language name is required")
    @Size(max = 20, message = "Language name must not exceed 20 characters")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "Language name must contain letters only (e.g. Tamil, Hindi, English)")
    private String name;

    public LanguageRequestDTO() {}
    public LanguageRequestDTO(String name) { this.name = name; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
