package com.example.film_rental_app.filmcatalog_contentmodule.controller;

import com.example.film_rental_app.filmcatalog_contentmodule.dto.request.ActorRequestDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.ActorResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import com.example.film_rental_app.filmcatalog_contentmodule.mapper.ActorMapper;
import com.example.film_rental_app.filmcatalog_contentmodule.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
    @Autowired
    private ActorService actorService;
    @Autowired
    private ActorMapper actorMapper;


    @GetMapping
    public ResponseEntity<List<ActorResponseDTO>> getAllActors() {
        List<ActorResponseDTO> result = actorService.getAllActors().stream()
                .map(actorMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{actorId}")
    public ResponseEntity<ActorResponseDTO> getActorById(@PathVariable Integer actorId) {
        return ResponseEntity.ok(actorMapper.toResponseDTO(actorService.getActorById(actorId)));
    }

    @PostMapping
    public ResponseEntity<ActorResponseDTO> createActor(@Valid @RequestBody ActorRequestDTO dto) {
        Actor actor = actorMapper.toEntity(dto);
        return ResponseEntity.status(201).body(actorMapper.toResponseDTO(actorService.createActor(actor)));
    }

    @PutMapping("/{actorId}")
    public ResponseEntity<ActorResponseDTO> updateActor(@PathVariable Integer actorId,
                                                        @Valid @RequestBody ActorRequestDTO dto) {
        Actor existing = actorService.getActorById(actorId);
        actorMapper.updateEntity(existing, dto);
        return ResponseEntity.ok(actorMapper.toResponseDTO(actorService.updateActor(actorId, existing)));
    }

    @DeleteMapping("/{actorId}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer actorId) {
        actorService.deleteActor(actorId);
        return ResponseEntity.noContent().build();
    }
}
