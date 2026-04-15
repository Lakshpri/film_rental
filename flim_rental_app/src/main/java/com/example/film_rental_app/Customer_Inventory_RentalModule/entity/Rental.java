package com.example.film_rental_app.Customer_Inventory_RentalModule.entity;
import com.example.film_rental_app.Payment_ReportsModule.entity.Payment;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Staff;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rental",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_rental_date_inventory_customer",
                        columnNames = {"rental_date", "inventory_id", "customer_id"}
                )
        },
        indexes = {
                @Index(name = "idx_fk_inventory_id", columnList = "inventory_id"),
                @Index(name = "idx_fk_customer_id",  columnList = "customer_id"),
                @Index(name = "idx_fk_staff_id",     columnList = "staff_id")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"inventory", "customer", "staff", "payments"})
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer rentalId;

    @NotNull
    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rental_inventory"))
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rental_customer"))
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rental_staff"))
    private Staff staff;

    @OneToMany(mappedBy = "rental", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();
}
