package com.example.film_rental_app.repository.Master_DataModules;


import com.example.film_rental_app.entity.Master_DataModules.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
}

