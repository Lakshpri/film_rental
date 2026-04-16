package com.example.film_rental_app.location_store_staffmodule.entity;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staff")
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
    @JoinColumn(name = "store_id",nullable = false,
            foreignKey = @ForeignKey(name = "fk_staff_store"))
    private Store store;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private Set<Rental> rentals = new HashSet<>();

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private Set<Payment> payments = new HashSet<>();


    public Staff() {}

    //  AllArgsConstructor
    public Staff(Integer staffId, String firstName, String lastName, String email,
                 boolean active, String username, String password, byte[] picture,
                 LocalDateTime lastUpdate, Address address, Store store,
                 Set<Rental> rentals, Set<Payment> payments) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.username = username;
        this.password = password;
        this.picture = picture;
        this.lastUpdate = lastUpdate;
        this.address = address;
        this.store = store;
        this.rentals = rentals;
        this.payments = payments;
    }

    //  Getters & Setters

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public byte[] getPicture() { return picture; }
    public void setPicture(byte[] picture) { this.picture = picture; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Set<Rental> getRentals() { return rentals; }
    public void setRentals(Set<Rental> rentals) { this.rentals = rentals; }

    public Set<Payment> getPayments() { return payments; }
    public void setPayments(Set<Payment> payments) { this.payments = payments; }

    //  toString (exclude relations)
    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", username='" + username + '\'' +
                '}';
    }
}