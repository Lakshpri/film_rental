package com.example.film_rental_app.payment_reportsmodule.service.implementation;

import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Customer;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.repository.CustomerRepository;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import com.example.film_rental_app.filmcatalog_contentmodule.exception.FilmNotFoundException;
import com.example.film_rental_app.filmcatalog_contentmodule.repository.FilmRepository;
import com.example.film_rental_app.location_store_staffmodule.exception.StoreNotFoundException;
import com.example.film_rental_app.location_store_staffmodule.repository.StoreRepository;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.service.AnalyticsService;
import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired private PaymentService     paymentService;
    @Autowired private CustomerService    customerService;
    @Autowired private RentalService      rentalService;
    @Autowired private InventoryService   inventoryService;
    @Autowired private FilmRepository     filmRepository;
    @Autowired private StoreRepository    storeRepository;
    @Autowired private CustomerRepository customerRepository;

    @Override
    public Map<String, Object> getCustomerBalance(Integer customerId) {

        customerService.getCustomerById(customerId);

        List<Payment> payments = paymentService.getPaymentsByCustomer(customerId);

        BigDecimal total = BigDecimal.ZERO;
        for (Payment p : payments) {
            total = total.add(p.getAmount());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("customerId", customerId);
        result.put("totalPayments", payments.size());
        result.put("totalAmountPaid", total);
        return result;
    }

    @Override
    public Map<String, Object> getRewardsReport() {

        List<Customer> customers = customerRepository.findAll();
        List<Map<String, Object>> list = new ArrayList<>();

        for (Customer c : customers) {
            int rentalCount = rentalService.getRentalsByCustomer(c.getCustomerId()).size();

            Map<String, Object> row = new HashMap<>();
            row.put("customerId",  c.getCustomerId());
            row.put("firstName",   c.getFirstName());
            row.put("lastName",    c.getLastName());
            row.put("email",       c.getEmail());
            row.put("rentalCount", rentalCount);
            list.add(row);
        }

        // Calculate average rentals
        int totalRentals = 0;
        for (Map<String, Object> row : list) {
            totalRentals += (Integer) row.get("rentalCount");
        }
        double avg = list.isEmpty() ? 0 : (double) totalRentals / list.size();

        // Customers above average are eligible for rewards
        List<Map<String, Object>> eligible = new ArrayList<>();
        for (Map<String, Object> row : list) {
            if ((Integer) row.get("rentalCount") > avg) {
                eligible.add(row);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("averageRentals", avg);
        result.put("rewardEligibleCustomers", eligible);
        return result;
    }

    @Override
    public Map<String, Object> getFilmInStock(Integer filmId, Integer storeId) {

        if (!filmRepository.existsById(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }

        List<Inventory> allInventory = inventoryService.getInventoryByStoreAndFilm(storeId, filmId);
        List<Rental>    allRentals   = rentalService.getAllRentals();

        // An inventory item is IN stock if no active rental exists for it
        List<Integer> inStockIds = new ArrayList<>();
        for (Inventory inv : allInventory) {
            boolean isRentedOut = false;
            for (Rental r : allRentals) {
                if (r.getInventory().getInventoryId().equals(inv.getInventoryId())
                        && r.getReturnDate() == null) {
                    isRentedOut = true;
                    break;
                }
            }
            if (!isRentedOut) {
                inStockIds.add(inv.getInventoryId());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("filmId",       filmId);
        result.put("storeId",      storeId);
        result.put("inStockCount", inStockIds.size());
        result.put("isAvailable",  !inStockIds.isEmpty());
        result.put("message",      inStockIds.isEmpty()
                ? "This film is not available at this store right now."
                : inStockIds.size() + " copy/copies of this film are available.");
        result.put("inventoryIds", inStockIds);
        return result;
    }

    @Override
    public Map<String, Object> getFilmNotInStock(Integer filmId, Integer storeId) {

        if (!filmRepository.existsById(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }

        List<Inventory> allInventory = inventoryService.getInventoryByStoreAndFilm(storeId, filmId);
        List<Rental>    allRentals   = rentalService.getAllRentals();

        // An inventory item is NOT in stock if it has an active (unreturned) rental
        List<Integer> rentedOutIds = new ArrayList<>();
        for (Inventory inv : allInventory) {
            for (Rental r : allRentals) {
                if (r.getInventory().getInventoryId().equals(inv.getInventoryId())
                        && r.getReturnDate() == null) {
                    rentedOutIds.add(inv.getInventoryId());
                    break;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("filmId",          filmId);
        result.put("storeId",         storeId);
        result.put("notInStockCount", rentedOutIds.size());
        result.put("message",         rentedOutIds.isEmpty()
                ? "All copies of this film are currently available (none are rented out)."
                : rentedOutIds.size() + " copy/copies of this film are currently rented out.");
        result.put("inventoryIds",    rentedOutIds);
        return result;
    }
}