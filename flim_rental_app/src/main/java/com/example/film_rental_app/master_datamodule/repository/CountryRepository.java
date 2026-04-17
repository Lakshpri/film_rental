package com.example.film_rental_app.master_datamodule.repository;

import com.example.film_rental_app.master_datamodule.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    boolean existsByCountry(String country);
}
