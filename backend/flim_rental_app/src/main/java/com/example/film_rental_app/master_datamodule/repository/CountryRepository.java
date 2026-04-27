package com.example.film_rental_app.master_datamodule.repository;

import com.example.film_rental_app.master_datamodule.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    boolean existsByCountry(String country);

    @Query("SELECT c FROM Country c WHERE LENGTH(c.country) > :length")
    List<Country> findCountriesWithNameLongerThan(@Param("length") int length);
}
