package com.example.film_rental_app.repository.Master_DataModules;

import com.example.film_rental_app.entity.Master_DataModules.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findByCountry_CountryId(Integer countryId);
}