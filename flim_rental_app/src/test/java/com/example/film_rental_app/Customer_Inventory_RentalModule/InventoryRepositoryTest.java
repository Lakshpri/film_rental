package com.example.film_rental_app.Customer_Inventory_RentalModule;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Inventory;
import com.example.film_rental_app.Customer_Inventory_RentalModule.repository.InventoryRepository;
import com.example.film_rental_app.FilmCatalog_ContentModule.entity.Film;
import com.example.film_rental_app.FilmCatalog_ContentModule.repository.FilmRepository;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Store;
import com.example.film_rental_app.Location_Store_StaffModule.repository.AddressRepository;
import com.example.film_rental_app.Location_Store_StaffModule.repository.StoreRepository;
import com.example.film_rental_app.Master_DataModule.entity.City;
import com.example.film_rental_app.Master_DataModule.entity.Country;
import com.example.film_rental_app.Master_DataModule.entity.Language;
import com.example.film_rental_app.Master_DataModule.repository.CityRepository;
import com.example.film_rental_app.Master_DataModule.repository.CountryRepository;
import com.example.film_rental_app.Master_DataModule.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class InventoryRepositoryTest {

    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private FilmRepository filmRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private CountryRepository countryRepository;
    @Autowired private LanguageRepository languageRepository;

    private Store store1, store2;
    private Film film1, film2;

    @BeforeEach
    void setUp() {
        Country country = new Country();
        country.setCountry("India");
        country = countryRepository.save(country);

        City city = new City();
        city.setCity("Bangalore");
        city.setCountry(country);
        city = cityRepository.save(city);

        Address addr = new Address();
        addr.setAddress("1 MG Road");
        addr.setDistrict("Karnataka");
        addr.setPhone("8001234567");
        addr.setCity(city);
        addr = addressRepository.save(addr);

        store1 = new Store();
        store1.setAddress(addr);
        store1 = storeRepository.save(store1);

        store2 = new Store();
        store2.setAddress(addr);
        store2 = storeRepository.save(store2);

        Language lang = new Language();
        lang.setName("English");
        lang = languageRepository.save(lang);

        film1 = new Film();
        film1.setTitle("Inception");
        film1.setLanguage(lang);
        film1 = filmRepository.save(film1);

        film2 = new Film();
        film2.setTitle("Interstellar");
        film2.setLanguage(lang);
        film2 = filmRepository.save(film2);
    }

    private Inventory buildInventory(Store store, Film film) {
        Inventory inv = new Inventory();
        inv.setStore(store);
        inv.setFilm(film);
        return inv;
    }

    @Test
    @DisplayName("Save inventory item with valid data should persist")
    void saveInventory_withValidData_shouldPersist() {
        Inventory saved = inventoryRepository.saveAndFlush(buildInventory(store1, film1));

        assertThat(saved.getInventoryId()).isNotNull();
    }

    @Test
    @DisplayName("findByStore_StoreId should return items for that store")
    void findByStoreId_shouldReturnMatchingInventory() {
        inventoryRepository.save(buildInventory(store1, film1));
        inventoryRepository.save(buildInventory(store1, film2));
        inventoryRepository.save(buildInventory(store2, film1));
        inventoryRepository.flush();

        List<Inventory> result = inventoryRepository.findByStore_StoreId(store1.getStoreId());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findByFilm_FilmId should return items for that film")
    void findByFilmId_shouldReturnMatchingInventory() {
        inventoryRepository.save(buildInventory(store1, film1));
        inventoryRepository.save(buildInventory(store2, film1));
        inventoryRepository.save(buildInventory(store1, film2));
        inventoryRepository.flush();

        List<Inventory> result = inventoryRepository.findByFilm_FilmId(film1.getFilmId());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("findByStore_StoreIdAndFilm_FilmId should return exact matches")
    void findByStoreIdAndFilmId_shouldReturnExactMatches() {
        inventoryRepository.save(buildInventory(store1, film1));
        inventoryRepository.save(buildInventory(store2, film1));
        inventoryRepository.save(buildInventory(store1, film2));
        inventoryRepository.flush();

        List<Inventory> result = inventoryRepository.findByStore_StoreIdAndFilm_FilmId(
                store1.getStoreId(), film1.getFilmId());

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("findByStore_StoreIdAndFilm_FilmId with no match returns empty list")
    void findByStoreIdAndFilmId_withNoMatch_shouldReturnEmpty() {
        inventoryRepository.flush();

        List<Inventory> result = inventoryRepository.findByStore_StoreIdAndFilm_FilmId(9999, 9999);

        assertThat(result).isEmpty();
    }
}
