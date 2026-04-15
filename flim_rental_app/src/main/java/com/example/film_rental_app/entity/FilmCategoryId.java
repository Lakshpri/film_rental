package com.example.film_rental_app.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class FilmCategoryId implements Serializable {

    private Integer filmId;
    private Integer categoryId;
}