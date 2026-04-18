package com.example.film_rental_app.filmcatalog_contentmodule.mapper;

import com.example.film_rental_app.filmcatalog_contentmodule.dto.request.ActorRequestDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.dto.response.ActorResponseDTO;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.Actor;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    public Actor toEntity(ActorRequestDTO dto) {
        Actor actor = new Actor();
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        return actor;
    }

    public ActorResponseDTO toResponseDTO(Actor actor) {
        return new ActorResponseDTO(
                actor.getActorId(),
                actor.getFirstName(),
                actor.getLastName(),
                actor.getLastUpdate()
        );
    }

    public void updateEntity(Actor actor, ActorRequestDTO dto) {
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
    }
}
