package com.example.film_rental_app.filmcatalog_contentmodule.repository;

import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmCategory;
import com.example.film_rental_app.filmcatalog_contentmodule.entity.FilmCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId> {
    List<FilmCategory> findById_FilmId(Integer filmId);
    List<FilmCategory> findById_CategoryId(Integer categoryId);

    @Modifying
    @Query("DELETE FROM FilmCategory fc WHERE fc.id.filmId = :filmId")
    void deleteByFilmId(@Param("filmId") Integer filmId);
}
