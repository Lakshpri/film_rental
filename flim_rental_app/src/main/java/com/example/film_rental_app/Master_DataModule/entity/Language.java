package com.example.film_rental_app.Master_DataModule.entity;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Film;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "language")
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

    public Language(Integer languageId, String name, LocalDateTime lastUpdate, Set<Film> films, Set<Film> originalLanguageFilms) {
        this.languageId = languageId;
        this.name = name;
        this.lastUpdate = lastUpdate;
        this.films = films;
        this.originalLanguageFilms = originalLanguageFilms;
    }
    public Language(){
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<Film> getFilms() {
        return films;
    }

    public void setFilms(Set<Film> films) {
        this.films = films;
    }

    public Set<Film> getOriginalLanguageFilms() {
        return originalLanguageFilms;
    }

    public void setOriginalLanguageFilms(Set<Film> originalLanguageFilms) {
        this.originalLanguageFilms = originalLanguageFilms;
    }

    @Override
    public String toString() {
        return "Language{" +
                "languageId=" + languageId +
                ", name='" + name + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", films=" + films +
                ", originalLanguageFilms=" + originalLanguageFilms +
                '}';
    }
}