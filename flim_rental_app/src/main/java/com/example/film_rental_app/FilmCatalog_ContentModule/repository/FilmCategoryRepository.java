package com.example.film_rental_app.FilmCatalog_ContentModule.repository;

import com.example.film_rental_app.FilmCatalog_ContentModule.entity.FilmCategory;
import com.example.film_rental_app.FilmCatalog_ContentModule.entity.FilmCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId> {
    List<FilmCategory> findById_FilmId(Integer filmId);
}
