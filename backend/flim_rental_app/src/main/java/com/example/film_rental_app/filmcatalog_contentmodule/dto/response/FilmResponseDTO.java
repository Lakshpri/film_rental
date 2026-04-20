package com.example.film_rental_app.filmcatalog_contentmodule.dto.response;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FilmResponseDTO {

    private Integer filmId;
    private String title;
    private String description;
    private Short releaseYear;
    private Integer languageId;
    private String languageName;
    private Integer originalLanguageId;
    private String originalLanguageName;
    private Short rentalDuration;
    private BigDecimal rentalRate;
    private Short length;
    private BigDecimal replacementCost;
    private Film.Rating rating;
    private String specialFeatures;
    private LocalDateTime lastUpdate;

    public FilmResponseDTO() {}

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Short getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Short releaseYear) { this.releaseYear = releaseYear; }

    public Integer getLanguageId() { return languageId; }
    public void setLanguageId(Integer languageId) { this.languageId = languageId; }

    public String getLanguageName() { return languageName; }
    public void setLanguageName(String languageName) { this.languageName = languageName; }

    public Integer getOriginalLanguageId() { return originalLanguageId; }
    public void setOriginalLanguageId(Integer originalLanguageId) { this.originalLanguageId = originalLanguageId; }

    public String getOriginalLanguageName() { return originalLanguageName; }
    public void setOriginalLanguageName(String originalLanguageName) { this.originalLanguageName = originalLanguageName; }

    public Short getRentalDuration() { return rentalDuration; }
    public void setRentalDuration(Short rentalDuration) { this.rentalDuration = rentalDuration; }

    public BigDecimal getRentalRate() { return rentalRate; }
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }

    public Short getLength() { return length; }
    public void setLength(Short length) { this.length = length; }

    public BigDecimal getReplacementCost() { return replacementCost; }
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }

    public Film.Rating getRating() { return rating; }
    public void setRating(Film.Rating rating) { this.rating = rating; }

    public String getSpecialFeatures() { return specialFeatures; }
    public void setSpecialFeatures(String specialFeatures) { this.specialFeatures = specialFeatures; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
