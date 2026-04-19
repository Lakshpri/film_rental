package com.example.film_rental_app.filmcatalog_contentmodule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FilmTextRequestDTO {

    @NotNull(message = "filmId is required")
    private Integer filmId;

    @NotBlank(message = "title is required")
    @Size(max = 255)
    private String title;

    private String description;

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
