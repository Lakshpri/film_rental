package com.example.film_rental_app.Location_Store_StaffModule.entity;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Rental;
import com.example.film_rental_app.Payment_ReportsModule.entity.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staff", indexes = {
        @Index(name = "idx_fk_store_id", columnList = "store_id"),
        @Index(name = "idx_fk_address_id", columnList = "address_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"address", "store", "rentals", "payments"})
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Integer staffId;

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

    @NotBlank
    @Size(max = 16)
    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Size(max = 40)
    @Column(name = "password", length = 40)
    private String password;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_staff_address"))
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_staff_store"))
    private Store store;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private Set<Rental> rentals = new HashSet<>();

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();
}
