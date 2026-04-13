package com.example.flim_rental_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Film {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer filmId;

    private String title;

    private String description;

    private Short releaseYear;

    private Short rentalDuration = 3;

    private BigDecimal rentalRate = new BigDecimal("4.99");

    private Short length;

    private BigDecimal replacementCost = new BigDecimal("19.99");



    private String specialFeatures;

    private LocalDateTime lastUpdate;



}
