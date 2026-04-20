package com.example.film_rental_app.filmcatalog_contentmodule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "film_text")
public class FilmText {
    @Id
    @Column(name = "film_id")
    private Integer filmId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Integer getFilmId() {
        return filmId;
    }

    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public FilmText(){

    }
    public FilmText(Integer filmId, String title, String description) {
        this.filmId = filmId;
        this.title = title;
        this.description = description;
    }
}