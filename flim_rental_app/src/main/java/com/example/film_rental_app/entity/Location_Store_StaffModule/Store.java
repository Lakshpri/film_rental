package com.example.film_rental_app.entity.Location_Store_StaffModule;

import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Customer;
import com.example.film_rental_app.entity.Customer_Inventory_RentalModule.Inventory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store", indexes = {
        @Index(name = "idx_unique_manager", columnList = "manager_staff_id", unique = true),
        @Index(name = "idx_fk_address_id", columnList = "address_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"managerStaff", "address", "staffList", "customers", "inventories"})
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Integer storeId;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_staff_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_store_staff"))
    private Staff managerStaff;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_address"))
    private Address address;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private Set<Staff> staffList = new HashSet<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private Set<Inventory> inventories = new HashSet<>();
}
