package com.example.film_rental_app.Customer_Inventory_RentalModule;

import com.example.film_rental_app.Customer_Inventory_RentalModule.entity.Customer;
import com.example.film_rental_app.Customer_Inventory_RentalModule.repository.CustomerRepository;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Address;
import com.example.film_rental_app.Location_Store_StaffModule.entity.Store;
import com.example.film_rental_app.Location_Store_StaffModule.repository.AddressRepository;
import com.example.film_rental_app.Location_Store_StaffModule.repository.StoreRepository;
import com.example.film_rental_app.Master_DataModule.entity.City;
import com.example.film_rental_app.Master_DataModule.entity.Country;
import com.example.film_rental_app.Master_DataModule.repository.CityRepository;
import com.example.film_rental_app.Master_DataModule.repository.CountryRepository;
import jakarta.validation.ConstraintViolationException;
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
class CustomerRepositoryTest {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private CountryRepository countryRepository;

    private Store store;
    private Address address;

    @BeforeEach
    void setUp() {
        Country country = new Country();
        country.setCountry("India");
        country = countryRepository.save(country);

        City city = new City();
        city.setCity("Chennai");
        city.setCountry(country);
        city = cityRepository.save(city);

        address = new Address();
        address.setAddress("123 Main St");
        address.setDistrict("Tamil Nadu");
        address.setPhone("9876543210");
        address.setCity(city);
        address = addressRepository.save(address);

        store = new Store();
        store.setAddress(address);
        store = storeRepository.save(store);
    }

    private Customer buildValid(String firstName, String lastName) {
        Customer c = new Customer();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setEmail("test@example.com");
        c.setActive(true);
        c.setStore(store);
        c.setAddress(address);
        return c;
    }

    @Test
    @DisplayName("Save customer with valid data should persist")
    void saveCustomer_withValidData_shouldPersist() {
        Customer saved = customerRepository.saveAndFlush(buildValid("Alice", "Smith"));

        assertThat(saved.getCustomerId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Alice");
        assertThat(saved.getLastName()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("Blank firstName should throw ConstraintViolationException")
    void saveCustomer_withBlankFirstName_shouldThrow() {
        Customer c = buildValid("   ", "Smith");

        assertThatThrownBy(() -> customerRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Null firstName should throw ConstraintViolationException")
    void saveCustomer_withNullFirstName_shouldThrow() {
        Customer c = buildValid(null, "Smith");

        assertThatThrownBy(() -> customerRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Blank lastName should throw ConstraintViolationException")
    void saveCustomer_withBlankLastName_shouldThrow() {
        Customer c = buildValid("Alice", "");

        assertThatThrownBy(() -> customerRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Null lastName should throw ConstraintViolationException")
    void saveCustomer_withNullLastName_shouldThrow() {
        Customer c = buildValid("Alice", null);

        assertThatThrownBy(() -> customerRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("firstName exceeding 45 chars should throw ConstraintViolationException")
    void saveCustomer_withTooLongFirstName_shouldThrow() {
        Customer c = buildValid("A".repeat(46), "Smith");

        assertThatThrownBy(() -> customerRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("lastName exactly 45 chars should persist successfully")
    void saveCustomer_withLastNameAtMaxLength_shouldPersist() {
        Customer c = buildValid("Alice", "S".repeat(45));

        Customer saved = customerRepository.saveAndFlush(c);
        assertThat(saved.getCustomerId()).isNotNull();
    }

    @Test
    @DisplayName("Invalid email format should throw ConstraintViolationException")
    void saveCustomer_withInvalidEmail_shouldThrow() {
        Customer c = buildValid("Alice", "Smith");
        c.setEmail("not-an-email");

        assertThatThrownBy(() -> customerRepository.saveAndFlush(c))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Null email (optional field) should persist successfully")
    void saveCustomer_withNullEmail_shouldPersist() {
        Customer c = buildValid("Alice", "Smith");
        c.setEmail(null);

        Customer saved = customerRepository.saveAndFlush(c);
        assertThat(saved.getCustomerId()).isNotNull();
    }

    @Test
    @DisplayName("findByStoreIdAndActiveStatus should return only active customers for given store")
    void findByStoreIdAndActiveStatus_shouldReturnActiveCustomers() {
        Customer active1 = buildValid("Alice", "Smith");
        active1.setActive(true);
        customerRepository.save(active1);

        Customer active2 = buildValid("Bob", "Jones");
        active2.setActive(true);
        customerRepository.save(active2);

        Customer inactive = buildValid("Charlie", "Brown");
        inactive.setActive(false);
        customerRepository.save(inactive);

        customerRepository.flush();

        List<Customer> result = customerRepository.findByStoreIdAndActiveStatus(store.getStoreId(), true);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Customer::getFirstName).containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    @DisplayName("findByStoreIdAndActiveStatus should return only inactive customers")
    void findByStoreIdAndActiveStatus_shouldReturnInactiveCustomers() {
        Customer active = buildValid("Alice", "Smith");
        active.setActive(true);
        customerRepository.save(active);

        Customer inactive = buildValid("Charlie", "Brown");
        inactive.setActive(false);
        customerRepository.save(inactive);

        customerRepository.flush();

        List<Customer> result = customerRepository.findByStoreIdAndActiveStatus(store.getStoreId(), false);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Charlie");
    }

    @Test
    @DisplayName("findByStoreIdAndActiveStatus should return empty list for unknown storeId")
    void findByStoreIdAndActiveStatus_withUnknownStore_shouldReturnEmpty() {
        customerRepository.save(buildValid("Alice", "Smith"));
        customerRepository.flush();

        List<Customer> result = customerRepository.findByStoreIdAndActiveStatus(9999, true);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("searchByName should match on firstName case-insensitively")
    void searchByName_shouldMatchFirstName() {
        customerRepository.save(buildValid("Alice", "Smith"));
        customerRepository.save(buildValid("Bob", "Jones"));
        customerRepository.flush();

        List<Customer> result = customerRepository.searchByName("alic");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("searchByName should match on lastName case-insensitively")
    void searchByName_shouldMatchLastName() {
        customerRepository.save(buildValid("Alice", "Smith"));
        customerRepository.save(buildValid("Bob", "Jones"));
        customerRepository.flush();

        List<Customer> result = customerRepository.searchByName("JONES");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo("Jones");
    }

    @Test
    @DisplayName("searchByName should return multiple matches")
    void searchByName_shouldReturnMultipleMatches() {
        customerRepository.save(buildValid("Smith", "Alice"));
        customerRepository.save(buildValid("Bob", "Smithson"));
        customerRepository.save(buildValid("Charlie", "Brown"));
        customerRepository.flush();

        List<Customer> result = customerRepository.searchByName("smith");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("searchByName with no match should return empty list")
    void searchByName_withNoMatch_shouldReturnEmpty() {
        customerRepository.save(buildValid("Alice", "Smith"));
        customerRepository.flush();

        List<Customer> result = customerRepository.searchByName("xyz123");

        assertThat(result).isEmpty();
    }
}
