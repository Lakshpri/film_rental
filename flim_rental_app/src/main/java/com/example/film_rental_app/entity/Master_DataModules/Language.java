package com.example.film_rental_app.entity.Master_DataModules;

import com.example.film_rental_app.entity.FilmCatalog_ContentModule.Film;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "language")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = {"films", "originalLanguageFilms"})
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Integer languageId;

    @NotBlank
    @Size(max = 20)
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // One Language -> Many Films (primary language)
    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    private Set<Film> films = new HashSet<>();

    // One Language -> Many Films (original language)
    @OneToMany(mappedBy = "originalLanguage", fetch = FetchType.LAZY)
    private Set<Film> originalLanguageFilms = new HashSet<>();
}