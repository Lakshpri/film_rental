package com.example.film_rental_app.master_datamodule.repository;

import com.example.film_rental_app.master_datamodule.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findByCountry_CountryId(Integer countryId);
    boolean existsByCountry_CountryId(Integer countryId);
    boolean existsByCityAndCountry_CountryId(String city, Integer countryId);

    @Query("SELECT c FROM City c WHERE c.country.countryId = :countryId ORDER BY c.city ASC")
    List<City> findCitiesByCountrySorted(@Param("countryId") Integer countryId);
}