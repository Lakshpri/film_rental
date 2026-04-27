package com.example.film_rental_app.master_datamodule.repository;

import com.example.film_rental_app.master_datamodule.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
    boolean existsByName(String name);

    @Query(value = "SELECT * FROM language WHERE name LIKE %:name%", nativeQuery = true)
    List<Language> findByNameNative(@Param("name") String name);
}