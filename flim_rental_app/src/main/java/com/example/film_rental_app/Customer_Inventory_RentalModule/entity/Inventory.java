package com.example.film_rental_app.Customer_Inventory_RentalModule.entity;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Film;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "inventory", indexes = {
        @Index(name = "idx_fk_film_id",        columnList = "film_id"),
        @Index(name = "idx_store_id_film_id",   columnList = "store_id, film_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"film", "store", "rentals"})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer inventoryId;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Many Inventory items -> One Film
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "film_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_inventory_film"))
    private Film film;

    // Many Inventory items -> One Store
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_inventory_store"))
    private Store store;

    // One Inventory item -> Many Rentals
    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Rental> rentals = new HashSet<>();
}
