package com.example.film_rental_app.filmcatalog_contentmodule.dto.request;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class FilmRequestDTO {

    @NotBlank(message = "Film title is required")
    @Size(max = 128, message = "Film title must not exceed 128 characters")
    private String title;

    private String description;

    @Min(value = 1888, message = "Release year cannot be before 1888 (the first film ever made)")
    @Max(value = 2100, message = "Release year seems too far in the future. Please enter a valid year")
    private Short releaseYear;

    @NotNull(message = "Language ID is required")
    @Positive(message = "Language ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer languageId;

    @Positive(message = "Original Language ID must be a number greater than zero (e.g. 1, 2, 3)")
    private Integer originalLanguageId;

    @Min(value = 1, message = "Rental duration must be at least 1 day")
    private Short rentalDuration = 3;

    @DecimalMin(value = "0.00", message = "Rental rate must be zero or more")
    private BigDecimal rentalRate = new BigDecimal("4.99");

    private Short length;

    @DecimalMin(value = "0.00", message = "Replacement cost must be zero or more")
    private BigDecimal replacementCost = new BigDecimal("19.99");

    private Film.Rating rating = Film.Rating.G;

    private String specialFeatures;

    public FilmRequestDTO() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Short getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Short releaseYear) { this.releaseYear = releaseYear; }

    public Integer getLanguageId() { return languageId; }
    public void setLanguageId(Integer languageId) { this.languageId = languageId; }

    public Integer getOriginalLanguageId() { return originalLanguageId; }
    public void setOriginalLanguageId(Integer originalLanguageId) { this.originalLanguageId = originalLanguageId; }

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
}
