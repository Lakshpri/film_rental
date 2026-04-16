package com.example.film_rental_app.customer_inventory_rentalmodule.entity;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Film;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "inventory")
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

    //  No-Args Constructor (Required by JPA)
    public Inventory() {}

    // Constructor with ID
    public Inventory(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    // Full Constructor
    public Inventory(Integer inventoryId, LocalDateTime lastUpdate,
                     Film film, Store store, Set<Rental> rentals) {
        this.inventoryId = inventoryId;
        this.lastUpdate = lastUpdate;
        this.film = film;
        this.store = store;
        this.rentals = rentals;
    }

    // Getters and Setters

    public Integer getInventoryId() { return inventoryId; }
    public void setInventoryId(Integer inventoryId) { this.inventoryId = inventoryId; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Set<Rental> getRentals() { return rentals; }
    public void setRentals(Set<Rental> rentals) { this.rentals = rentals; }

    // ✅ toString (exclude relationships to avoid recursion)
    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}