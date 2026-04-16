package com.example.film_rental_app.filmcatalog_contentmodule;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.ActorRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class ActorRepositoryTest {

    @Autowired
    private ActorRepository actorRepository;
    @Test
    @DisplayName("Save actor with valid data should persist successfully")
    void saveActor_withValidData_shouldPersist() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");

        Actor saved = actorRepository.save(actor);

        assertThat(saved.getActorId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Tom");
        assertThat(saved.getLastName()).isEqualTo("Hanks");
    }

    @Test
    @DisplayName("Save actor with blank firstName should throw ConstraintViolationException")
    void saveActor_withBlankFirstName_shouldThrow() {
        Actor actor = new Actor();
        actor.setFirstName("  "); // blank
        actor.setLastName("Hanks");

        assertThatThrownBy(() -> actorRepository.saveAndFlush(actor))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save actor with null firstName should throw ConstraintViolationException")
    void saveActor_withNullFirstName_shouldThrow() {
        Actor actor = new Actor();
        actor.setFirstName(null);
        actor.setLastName("Hanks");

        assertThatThrownBy(() -> actorRepository.saveAndFlush(actor))
                .isInstanceOf(ConstraintViolationException.class);
    }



    @Test
    @DisplayName("Save actor with blank lastName should throw ConstraintViolationException")
    void saveActor_withBlankLastName_shouldThrow() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("");

        assertThatThrownBy(() -> actorRepository.saveAndFlush(actor))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save actor with null lastName should throw ConstraintViolationException")
    void saveActor_withNullLastName_shouldThrow() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName(null);

        assertThatThrownBy(() -> actorRepository.saveAndFlush(actor))
                .isInstanceOf(ConstraintViolationException.class);
    }



    @Test
    @DisplayName("Save actor with firstName exceeding 45 chars should throw ConstraintViolationException")
    void saveActor_withTooLongFirstName_shouldThrow() {
        Actor actor = new Actor();
        actor.setFirstName("A".repeat(46)); // 46 chars
        actor.setLastName("Hanks");

        assertThatThrownBy(() -> actorRepository.saveAndFlush(actor))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Save actor with lastName exactly 45 chars should persist")
    void saveActor_withLastNameAtMaxSize_shouldPersist() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("H".repeat(45));

        Actor saved = actorRepository.saveAndFlush(actor);
        assertThat(saved.getActorId()).isNotNull();
    }
    @Test
    @DisplayName("Find actor by ID should return actor")
    void findById_shouldReturnActor() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");

        Actor saved = actorRepository.saveAndFlush(actor);

        Actor found = actorRepository.findById(saved.getActorId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("Tom");
    }
    @Test
    @DisplayName("Find all actors should return list")
    void findAll_shouldReturnActors() {
        Actor a1 = new Actor(null, "Tom", "Hanks", null, null);
        Actor a2 = new Actor(null, "Leonardo", "DiCaprio", null, null);

        actorRepository.save(a1);
        actorRepository.save(a2);

        var actors = actorRepository.findAll();

        assertThat(actors).hasSizeGreaterThanOrEqualTo(2);
    }
    @Test
    @DisplayName("Update actor should modify existing record")
    void updateActor_shouldUpdateFields() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");

        Actor saved = actorRepository.saveAndFlush(actor);

        // Update
        saved.setFirstName("Thomas");
        Actor updated = actorRepository.saveAndFlush(saved);

        assertThat(updated.getFirstName()).isEqualTo("Thomas");
    }
    @Test
    @DisplayName("Delete actor should remove record")
    void deleteActor_shouldRemove() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");

        Actor saved = actorRepository.saveAndFlush(actor);

        actorRepository.deleteById(saved.getActorId());
        actorRepository.flush();

        boolean exists = actorRepository.existsById(saved.getActorId());

        assertThat(exists).isFalse();
    }
    @Test
    @DisplayName("ExistsById should return true if actor exists")
    void existsById_shouldReturnTrue() {
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");

        Actor saved = actorRepository.saveAndFlush(actor);

        boolean exists = actorRepository.existsById(saved.getActorId());

        assertThat(exists).isTrue();
    }
}
