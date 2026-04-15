package com.example.flim_rental_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "film", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_fk_language_id", columnList = "language_id"),
        @Index(name = "idx_fk_original_language_id", columnList = "original_language_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"language", "originalLanguage", "filmActors", "filmCategories", "inventories"})
public class Film {

    public enum Rating {
        G, PG, PG_13, R, NC_17
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Integer filmId;

    @NotBlank
    @Size(max = 128)
    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_year")
    private Short releaseYear;

    @Min(1)
    @Column(name = "rental_duration", nullable = false)
    private Short rentalDuration = 3;

    @DecimalMin("0.00")
    @Column(name = "rental_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal rentalRate = new BigDecimal("4.99");

    @Column(name = "length")
    private Short length;

    @DecimalMin("0.00")
    @Column(name = "replacement_cost", nullable = false, precision = 5, scale = 2)
    private BigDecimal replacementCost = new BigDecimal("19.99");

    @Enumerated(EnumType.STRING)
    @Column(name = "rating", length = 10)
    private Rating rating = Rating.G;

    // Stored as a comma-separated string (PostgreSQL doesn't have SET type)
    @Column(name = "special_features", columnDefinition = "TEXT")
    private String specialFeatures;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Many Films -> One Language (primary)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_film_language"))
    private Language language;

    // Many Films -> One Language (original, nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_language_id",
            foreignKey = @ForeignKey(name = "fk_film_language_original"))
    private Language originalLanguage;

    // One Film -> Many FilmActor join records
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FilmActor> filmActors = new HashSet<>();

    // One Film -> Many FilmCategory join records
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FilmCategory> filmCategories = new HashSet<>();

    // One Film -> Many Inventory items
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Inventory> inventories = new HashSet<>();
}
