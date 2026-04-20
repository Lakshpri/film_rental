package com.example.film_rental_app.filmcatalog_contentmodule.dto.response;

import java.time.LocalDateTime;

/** Returned for GET /films/{filmId}/categories */
public class FilmCategoryResponseDTO {

    private Integer filmId;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime lastUpdate;

    public FilmCategoryResponseDTO() {}

    public FilmCategoryResponseDTO(Integer filmId, Integer categoryId,
                                   String categoryName, LocalDateTime lastUpdate) {
        this.filmId = filmId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.lastUpdate = lastUpdate;
    }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
