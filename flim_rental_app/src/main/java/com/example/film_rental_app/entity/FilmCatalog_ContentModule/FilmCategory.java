package com.example.film_rental_app.entity.FilmCatalog_ContentModule;


import com.example.film_rental_app.entity.Master_DataModules.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_category")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = {"film", "category"})
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
}
