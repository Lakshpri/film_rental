package com.example.film_rental_app.Customer_Inventory_RentalModule.entity;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;
import com.example.film_rental_app.Payment_ReportsModule.entity.Payment;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer", indexes = {
        @Index(name = "idx_fk_store_id",   columnList = "store_id"),
        @Index(name = "idx_fk_address_id", columnList = "address_id"),
        @Index(name = "idx_last_name",     columnList = "last_name")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"store", "address", "rentals", "payments"})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @NotBlank
    @Size(max = 45)
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @NotBlank
    @Size(max = 45)
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Email
    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    // Many Customers -> One Store
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_customer_store"))
    private Store store;

    // Many Customers -> One Address
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_customer_address"))
    private Address address;

    // One Customer -> Many Rentals
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Rental> rentals = new HashSet<>();

    // One Customer -> Many Payments
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();
}
