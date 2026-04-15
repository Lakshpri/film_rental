package com.example.film_rental_app.entity.Master_DataModules;
import com.example.film_rental_app.entity.Location_Store_StaffModule.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "city", indexes = {
        @Index(name = "idx_fk_country_id", columnList = "country_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = {"country", "addresses"})
public class
City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Integer cityId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_city_country"))
    private Country country;


    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();
}
