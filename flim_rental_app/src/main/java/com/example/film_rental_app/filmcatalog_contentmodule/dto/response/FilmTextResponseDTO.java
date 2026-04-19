package com.example.film_rental_app.filmcatalog_contentmodule.dto.response;

public class FilmTextResponseDTO {

    private Integer filmId;
    private String title;
    private String description;

    public FilmTextResponseDTO() {}

    public FilmTextResponseDTO(Integer filmId, String title, String description) {
        this.filmId = filmId;
        this.title = title;
        this.description = description;
    }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
