package com.example.film_rental_app.controller.FilmCatalog_ContentModule;

import com.example.film_rental_app.entity.FilmCatalog_ContentModule.Actor;
import com.example.film_rental_app.repository.FilmCatalog_ContentModule.ActorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorRepository actorRepository;

    public ActorController(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @GetMapping
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @GetMapping("/{actorId}")
    public ResponseEntity<Actor> getActorById(@PathVariable Integer actorId) {
        return actorRepository.findById(actorId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Actor createActor(@RequestBody Actor actor) {
        return actorRepository.save(actor);
    }

    @PutMapping("/{actorId}")
    public ResponseEntity<Actor> updateActor(@PathVariable Integer actorId, @RequestBody Actor updated) {
        return actorRepository.findById(actorId).map(actor -> {
            actor.setFirstName(updated.getFirstName());
            actor.setLastName(updated.getLastName());
            return ResponseEntity.ok(actorRepository.save(actor));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{actorId}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer actorId) {
        if (!actorRepository.existsById(actorId)) return ResponseEntity.notFound().build();
        actorRepository.deleteById(actorId);
        return ResponseEntity.noContent().build();
    }
}
