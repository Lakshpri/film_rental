package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }
    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }
    @GetMapping("/{actorId}")
    public ResponseEntity<Actor> getActorById(@PathVariable Integer actorId) {
        return ResponseEntity.ok(actorService.getActorById(actorId));
    }
    @PostMapping
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) {
        Actor saved = actorService.createActor(actor);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @PutMapping("/{actorId}")
    public ResponseEntity<Actor> updateActor(
            @PathVariable Integer actorId,
            @Valid @RequestBody Actor updated) {

        Actor updatedActor = actorService.updateActor(actorId, updated);
        return ResponseEntity.ok(updatedActor);
    }
    @DeleteMapping("/{actorId}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer actorId) {
        actorService.deleteActor(actorId);
        return ResponseEntity.noContent().build();
    }
}