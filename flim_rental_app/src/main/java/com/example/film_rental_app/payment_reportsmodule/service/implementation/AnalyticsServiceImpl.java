package com.example.film_rental_app.payment_reportsmodule.service.implementation;
import com.example.film_rental_app.payment_reportsmodule.service.PaymentService;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Inventory;
import com.example.film_rental_app.customer_inventory_rentalmodule.entity.Rental;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.CustomerService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.InventoryService;
import com.example.film_rental_app.customer_inventory_rentalmodule.service.RentalService;
import com.example.film_rental_app.payment_reportsmodule.entity.Payment;
import com.example.film_rental_app.payment_reportsmodule.service.AnalyticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RentalService rentalService;
    @Autowired
    private InventoryService inventoryService;

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

        List<Map<String, Object>> list = new ArrayList<>();

        customerService.getAllCustomers().forEach(c -> {
            int count = rentalService.getRentalsByCustomer(c.getCustomerId()).size();

            Map<String, Object> map = new HashMap<>();
            map.put("customerId", c.getCustomerId());
            map.put("firstName", c.getFirstName());
            map.put("lastName", c.getLastName());
            map.put("email", c.getEmail());
            map.put("rentalCount", count);

            list.add(map);
        });

        double avg = list.stream()
                .mapToInt(x -> (Integer) x.get("rentalCount"))
                .average()
                .orElse(0);

        List<Map<String, Object>> eligible = list.stream()
                .filter(x -> (Integer) x.get("rentalCount") > avg)
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("averageRentals", avg);
        result.put("rewardEligibleCustomers", eligible);

        return result;
    }

    @Override
    public Map<String, Object> getFilmInStock(Integer filmId, Integer storeId) {

        List<Inventory> items = inventoryService.getInventoryByStoreAndFilm(storeId, filmId);
        List<Rental> rentals = rentalService.getAllRentals();

        List<Inventory> inStock = items.stream()
                .filter(inv -> rentals.stream()
                        .noneMatch(r -> r.getInventory().getInventoryId().equals(inv.getInventoryId())
                                && r.getReturnDate() == null))
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("filmId", filmId);
        result.put("storeId", storeId);
        result.put("inStockCount", inStock.size());
        result.put("inventoryItems", inStock);

        return result;
    }

    @Override
    public Map<String, Object> getFilmNotInStock(Integer filmId, Integer storeId) {

        List<Inventory> items = inventoryService.getInventoryByStoreAndFilm(storeId, filmId);
        List<Rental> rentals = rentalService.getAllRentals();

        List<Inventory> notInStock = items.stream()
                .filter(inv -> rentals.stream()
                        .anyMatch(r -> r.getInventory().getInventoryId().equals(inv.getInventoryId())
                                && r.getReturnDate() == null))
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("filmId", filmId);
        result.put("storeId", storeId);
        result.put("notInStockCount", notInStock.size());
        result.put("inventoryItems", notInStock);

        return result;
    }
}