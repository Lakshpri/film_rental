package com.example.flim_rental_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actor", indexes = {
        @Index(name = "idx_actor_last_name", columnList = "last_name")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString(exclude = "filmActors")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private Integer actorId;

    @NotBlank
    @Size(max = 45)
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @NotBlank
    @Size(max = 45)
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // One Actor -> Many FilmActor join records
//    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<FilmActor> filmActors = new HashSet<>();
}
