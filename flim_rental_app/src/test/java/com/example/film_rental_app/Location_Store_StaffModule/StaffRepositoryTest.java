package com.example.film_rental_app.Location_Store_StaffModule;

import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Staff;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Store;
import com.example.film_rental_app.Location_Store_StaffModule.repository.AddressRepository;
import com.example.film_rental_app.Location_Store_StaffModule.repository.StaffRepository;
import com.example.film_rental_app.Location_Store_StaffModule.repository.StoreRepository;
import com.example.film_rental_app.Master_DataModule.entity.City;
import com.example.film_rental_app.Master_DataModule.entity.Country;
import com.example.film_rental_app.Master_DataModule.repository.CityRepository;
import com.example.film_rental_app.Master_DataModule.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StaffRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private StaffRepository staffRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private CountryRepository countryRepository;

    private Address address;
    private Store store;


    private Store createStore() {
        em.getEntityManager()
                .createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE")
                .executeUpdate();

        // Insert Store with dummy manager_staff_id = 0 (bypasses NOT NULL)
        em.getEntityManager()
                .createNativeQuery(
                        "INSERT INTO store (address_id, manager_staff_id, last_update) "
                                + "VALUES (?, 0, NOW())")
                .setParameter(1, address.getAddressId())
                .executeUpdate();

        Integer storeId = (Integer) em.getEntityManager()
                .createNativeQuery("SELECT MAX(store_id) FROM store")
                .getSingleResult();

        // Insert Staff with the real store_id
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

        // Update Store with the real manager_staff_id
        em.getEntityManager()
                .createNativeQuery("UPDATE store SET manager_staff_id = ? WHERE store_id = ?")
                .setParameter(1, staffId)
                .setParameter(2, storeId)
                .executeUpdate();

        // Re-enable all constraints
        em.getEntityManager()
                .createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE")
                .executeUpdate();

        em.flush();
        em.clear();

        return storeRepository.findById(storeId).orElseThrow();
    }

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

        store = createStore();
    }

    private Staff buildStaff(String username) {
        Staff s = new Staff();
        s.setFirstName("John");
        s.setLastName("Doe");
        s.setUsername(username);
        s.setAddress(address);
        s.setStore(store);
        return s;
    }

    //  CRUD Tests

    @Test
    void testSave() {
        Staff saved = staffRepository.save(buildStaff("john1"));
        em.flush();
        assertThat(saved.getStaffId()).isNotNull();
    }

    @Test
    void testFindById() {
        Staff saved = staffRepository.save(buildStaff("john2"));
        em.flush();
        em.clear();
        assertThat(staffRepository.findById(saved.getStaffId())).isPresent();
    }

    @Test
    void testUpdate() {
        Staff saved = staffRepository.save(buildStaff("john3"));
        em.flush();
        em.clear();

        Staff fetched = staffRepository.findById(saved.getStaffId()).orElseThrow();
        fetched.setFirstName("Updated");
        staffRepository.save(fetched);
        em.flush();
        em.clear();

        assertThat(staffRepository.findById(saved.getStaffId())
                .orElseThrow().getFirstName())
                .isEqualTo("Updated");
    }

    @Test
    void testDelete() {
        Staff saved = staffRepository.save(buildStaff("john4"));
        em.flush();
        em.clear();

        staffRepository.deleteById(saved.getStaffId());
        em.flush();

        assertThat(staffRepository.findById(saved.getStaffId())).isEmpty();
    }

    @Test
    void testFindByStoreId() {
        staffRepository.save(buildStaff("john5"));
        staffRepository.save(buildStaff("john6"));
        em.flush();
        em.clear();

        // manager from createStore() + 2 extra staff → at least 2
        List<Staff> result = staffRepository.findByStore_StoreId(store.getStoreId());
        assertThat(result.size()).isGreaterThanOrEqualTo(2);
    }
}