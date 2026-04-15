package com.example.film_rental_app.Master_DataModule.repository;


import com.example.film_rental_app.Master_DataModule.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
}

