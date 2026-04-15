package com.example.film_rental_app.repository.FilmCatalog_ContentModule;

import com.example.film_rental_app.entity.FilmCatalog_ContentModule.FilmCategory;
import com.example.film_rental_app.entity.FilmCatalog_ContentModule.FilmCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId> {
    List<FilmCategory> findById_FilmId(Integer filmId);
}
