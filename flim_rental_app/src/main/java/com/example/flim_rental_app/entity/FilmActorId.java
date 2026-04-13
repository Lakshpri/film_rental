package com.example.flim_rental_app.entity;


import lombok.*;

import java.io.Serializable;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class FilmActorId implements Serializable {

    private Integer actorId;
    private Integer filmId;
}
