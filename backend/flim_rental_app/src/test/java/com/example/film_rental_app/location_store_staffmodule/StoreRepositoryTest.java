package com.example.film_rental_app.location_store_staffmodule;

import com.example.film_rental_app.location_store_staffmodule.entity.Address;
import com.example.film_rental_app.location_store_staffmodule.entity.Store;
import com.example.film_rental_app.location_store_staffmodule.repository.AddressRepository;
import com.example.film_rental_app.location_store_staffmodule.repository.StaffRepository;
import com.example.film_rental_app.location_store_staffmodule.repository.StoreRepository;
import com.example.film_rental_app.master_datamodule.entity.City;
import com.example.film_rental_app.master_datamodule.entity.Country;
import com.example.film_rental_app.master_datamodule.repository.CityRepository;
import com.example.film_rental_app.master_datamodule.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private StoreRepository storeRepository;
    @Autowired private StaffRepository staffRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private CountryRepository countryRepository;

    private Address address;

    @BeforeEach
    void setup() {
        Country country = new Country();
        country.setCountry("India");
        country = countryRepository.save(country);

        City city = new City();
        city.setCity("Chennai");
        city.setCountry(country);
        city = cityRepository.save(city);

        address = new Address();
        address.setAddress("Street 1");
        address.setDistrict("TN");
        address.setPhone("1234567890");
        address.setCity(city);
        address = addressRepository.save(address);

        em.flush();
    }


    private Store createStore() {
        em.getEntityManager()
                .createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE")
                .executeUpdate();

        em.getEntityManager()
                .createNativeQuery(
                        "INSERT INTO store (address_id, manager_staff_id, last_update) "
                                + "VALUES (?, 0, NOW())")
                .setParameter(1, address.getAddressId())
                .executeUpdate();

        Integer storeId = (Integer) em.getEntityManager()
                .createNativeQuery("SELECT MAX(store_id) FROM store")
                .getSingleResult();

        em.getEntityManager()
                .createNativeQuery(
                        "INSERT INTO staff (first_name, last_name, username, address_id, "
                                + "store_id, active, last_update) VALUES ('Manager', 'One', 'mgr1', ?, ?, TRUE, NOW())")
                .setParameter(1, address.getAddressId())
                .setParameter(2, storeId)
                .executeUpdate();

        Integer staffId = (Integer) em.getEntityManager()
                .createNativeQuery("SELECT MAX(staff_id) FROM staff")
                .getSingleResult();

        em.getEntityManager()
                .createNativeQuery("UPDATE store SET manager_staff_id = ? WHERE store_id = ?")
                .setParameter(1, staffId)
                .setParameter(2, storeId)
                .executeUpdate();

        em.getEntityManager()
                .createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE")
                .executeUpdate();

        em.flush();
        em.clear();

        return storeRepository.findById(storeId).orElseThrow();
    }

    // ── CRUD Tests ────────────────────────────────────────────────────────────

    @Test
    void testSave() {
        Store store = createStore();
        assertThat(store.getStoreId()).isNotNull();
    }

    @Test
    void testFindById() {
        Store store = createStore();
        assertThat(storeRepository.findById(store.getStoreId())).isPresent();
    }

    @Test
    void testUpdate() {
        Store store = createStore();

        Address newAddress = new Address();
        newAddress.setAddress("New Street");
        newAddress.setDistrict("TN");
        newAddress.setPhone("9999999999");
        newAddress.setCity(address.getCity());
        newAddress = addressRepository.save(newAddress);
        em.flush();

        store.setAddress(newAddress);
        storeRepository.save(store);
        em.flush();
        em.clear();

        assertThat(storeRepository.findById(store.getStoreId())
                .orElseThrow().getAddress().getAddress())
                .isEqualTo("New Street");
    }

    @Test
    void testDelete() {
        Store store = createStore();
        Integer storeId = store.getStoreId();

        // Must remove the FK reference from staff before deleting store
        // (staff.store_id → store, so delete staff first)
        em.getEntityManager()
                .createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE")
                .executeUpdate();

        em.getEntityManager()
                .createNativeQuery("DELETE FROM staff WHERE store_id = ?")
                .setParameter(1, storeId)
                .executeUpdate();

        em.getEntityManager()
                .createNativeQuery("DELETE FROM store WHERE store_id = ?")
                .setParameter(1, storeId)
                .executeUpdate();

        em.getEntityManager()
                .createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE")
                .executeUpdate();

        em.flush();
        em.clear();

        assertThat(storeRepository.findById(storeId)).isEmpty();
    }
}