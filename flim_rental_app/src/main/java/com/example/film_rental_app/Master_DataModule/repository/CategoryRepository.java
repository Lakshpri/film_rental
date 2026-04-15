package com.example.film_rental_app.Master_DataModule.repository;

import com.example.film_rental_app.Master_DataModule.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}