package com.example.film_rental_app.FilmCatalog_ContentModule.entity;


import com.example.film_rental_app.Master_DataModule.entity.Category;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_category")
public class FilmCategory {

    @EmbeddedId
    private FilmCategoryId id;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Many FilmCategory -> One Film
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("filmId")
    @JoinColumn(name = "film_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_film_category_film"))
    private Film film;

    // Many FilmCategory -> One Category
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_film_category_category"))
    private Category category;

    public FilmCategoryId getId() {
        return id;
    }

    public void setId(FilmCategoryId id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public FilmCategory(){

    }

    public FilmCategory(FilmCategoryId id, LocalDateTime lastUpdate, Film film, Category category) {
        this.id = id;
        this.lastUpdate = lastUpdate;
        this.film = film;
        this.category = category;
    }

    @Override
    public String toString() {
        return "FilmCategory{" +
                "id=" + id +
                ", lastUpdate=" + lastUpdate +
                ", film=" + film +
                ", category=" + category +
                '}';
    }
}
