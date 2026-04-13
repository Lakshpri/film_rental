package com.example.flim_rental_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class FilmActor {

    private LocalDateTime lastUpdate;

    private Film film;

}
