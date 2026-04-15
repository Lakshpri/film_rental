package com.example.film_rental_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment", indexes = {
        @Index(name = "idx_fk_staff_id",    columnList = "staff_id"),
        @Index(name = "idx_fk_customer_id", columnList = "customer_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"customer", "staff", "rental"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "amount", nullable = false, precision = 5, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @UpdateTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_payment_customer"))
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_payment_staff"))
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id",
            foreignKey = @ForeignKey(name = "fk_payment_rental"))
    private Rental rental;
}
