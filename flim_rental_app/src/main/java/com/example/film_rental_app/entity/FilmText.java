package com.example.film_rental_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "film_text")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
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
}