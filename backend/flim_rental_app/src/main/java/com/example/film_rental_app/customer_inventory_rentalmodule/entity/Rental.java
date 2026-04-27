package com.example.film_rental_app.customer_inventory_rentalmodule.entity;

import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.location_store_staffmodule.entity.Staff;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Marks this class as a database entity
@Entity
// Maps to "rental" table with unique constraint
@Table(name = "rental",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_rental_date_inventory_customer",
                        columnNames = {"rental_date", "inventory_id", "customer_id"}
                )
        }
)
public class Rental {

    // Primary key
    @Id
    // Auto-increment ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer rentalId;

    // Rental start date (cannot be null)
    @NotNull
    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDate;

    // Return date (can be null if not returned)
    @Column(name = "return_date")
    private LocalDateTime returnDate; // manually enter user update

    // Automatically updates on record change
    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate; // system updates

    // Many rentals belong to one inventory item
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rental_inventory"))
    private Inventory inventory;

    // Many rentals belong to one customer
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rental_customer"))
    private Customer customer;

    // Many rentals handled by one staff
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rental_staff"))
    private Staff staff;

    // One rental can have multiple payments
    @OneToMany(mappedBy = "rental", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();

    // Default constructor (required by JPA)
    public Rental() {}

    // Constructor with only ID
    public Rental(Integer rentalId) {
        this.rentalId = rentalId;
    }

    // Full constructor
    public Rental(Integer rentalId, LocalDateTime rentalDate, LocalDateTime returnDate,
                  LocalDateTime lastUpdate, Inventory inventory,
                  Customer customer, Staff staff, Set<Payment> payments) {
        this.rentalId = rentalId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.lastUpdate = lastUpdate;
        this.inventory = inventory;
        this.customer = customer;
        this.staff = staff;
        this.payments = payments;
    }

    // Getters and Setters

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) { this.rentalId = rentalId; }

    public LocalDateTime getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDateTime rentalDate) { this.rentalDate = rentalDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }

    public Set<Payment> getPayments() { return payments; }
    public void setPayments(Set<Payment> payments) { this.payments = payments; }

    // toString without relationships to avoid infinite loop
    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}