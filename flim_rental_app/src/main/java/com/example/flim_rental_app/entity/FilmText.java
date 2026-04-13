package com.example.flim_rental_app.entity;

import jakarta.persistence.*;

@Entity
public class FilmText {
    @Id
    @Column(name = "film_id")
    private Integer filmId;
}
