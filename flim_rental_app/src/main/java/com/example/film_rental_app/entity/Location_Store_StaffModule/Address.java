package com.example.film_rental_app.entity.Location_Store_StaffModule;


import com.example.film_rental_app.entity.Master_DataModules.City;
import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "address", indexes = {
        @Index(name = "idx_fk_city_id", columnList = "city_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = {"city", "customers", "staffList", "stores"})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Size(max = 50)
    @Column(name = "address2", length = 50)
    private String address2;

    @NotBlank
    @Size(max = 20)
    @Column(name = "district", nullable = false, length = 20)
    private String district;

    @Size(max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @NotBlank
    @Size(max = 20)
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_address_city"))
    private City city;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Staff> staffList = new HashSet<>();

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private Set<Store> stores = new HashSet<>();
}
