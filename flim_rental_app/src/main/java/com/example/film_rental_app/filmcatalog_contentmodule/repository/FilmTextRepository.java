package com.example.film_rental_app.filmcatalog_contentmodule.repository;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmTextRepository extends JpaRepository<FilmText, Integer> {
    boolean existsByFilmId(Integer filmId);
}
