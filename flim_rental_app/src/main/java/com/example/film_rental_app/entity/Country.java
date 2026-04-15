package com.example.film_rental_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

    @Entity
    @Table(name = "country")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString(exclude = "cities")
    public class Country {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "country_id")
        private Integer countryId;

        @NotBlank
        @Size(max = 50)
        @Column(name = "country", nullable = false, length = 50)
        private String country;

        @UpdateTimestamp
        @Column(name = "last_update", nullable = false)
        private LocalDateTime lastUpdate;

        // One Country -> Many Cities
        @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<City> cities = new HashSet<>();
    }

