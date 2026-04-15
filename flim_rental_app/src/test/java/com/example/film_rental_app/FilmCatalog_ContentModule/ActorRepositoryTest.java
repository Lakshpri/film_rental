package com.example.film_rental_app.FilmCatalog_ContentModule;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Actor;
import com.example.film_rental_app.FilmCatalog_ContentModule.repository.ActorRepository;
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
}
