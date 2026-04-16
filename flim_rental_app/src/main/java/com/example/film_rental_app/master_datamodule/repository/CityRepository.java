package com.example.film_rental_app.master_datamodule.repository;

import com.example.film_rental_app.master_datamodule.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findByCountry_CountryId(Integer countryId);
}