package com.example.film_rental_app.customer_inventory_rentalmodule.entity;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Marks this class as a database entity
@Entity
// Maps this entity to "customer" table
@Table(name = "customer")
public class Customer {

    // Primary key
    @Id
    // Auto-increment ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Column name in DB
    @Column(name = "customer_id")
    private Integer customerId;

    // Cannot be empty
    @NotBlank
    // Max length 45
    @Size(max = 45)
    // Column mapping
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    // Cannot be empty
    @NotBlank
    // Max length 45
    @Size(max = 45)
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    // Must be valid email
    @Email
    // Max length 50
    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    // Active status (default true)
    @Column(name = "active", nullable = false)
    private boolean active = true;

    // Automatically sets created date
    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    // Automatically updates on change
    @UpdateTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    // Many customers belong to one store
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_customer_store"))
    private Store store;

    // Many customers belong to one address
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_customer_address"))
    private Address address;

    // One customer can have many rentals
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Rental> rentals = new HashSet<>();

    // One customer can have many payments
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();

    // Default constructor (required for JPA)
    public Customer() {}

    // Constructor with only ID
    public Customer(Integer customerId) {
        this.customerId = customerId;
    }

    // Full constructor
    public Customer(Integer customerId, String firstName, String lastName, String email,
                    boolean active, LocalDateTime createDate, LocalDateTime lastUpdate,
                    Store store, Address address, Set<Rental> rentals, Set<Payment> payments) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.store = store;
        this.address = address;
        this.rentals = rentals;
        this.payments = payments;
    }

    // Getters and Setters

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public Set<Rental> getRentals() { return rentals; }
    public void setRentals(Set<Rental> rentals) { this.rentals = rentals; }

    public Set<Payment> getPayments() { return payments; }
    public void setPayments(Set<Payment> payments) { this.payments = payments; }

    // toString without relationships to avoid infinite loop
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}