package com.example.film_rental_app.payment_reportsmodule.service;

import java.util.Map;

// Defines all analytics operations available in the system
// AnalyticsServiceImpl provides the actual implementation
public interface AnalyticsService {

    // Returns total payments and amount paid for a customer — throws 404 if customer not found
    Map<String, Object> getCustomerBalance(Integer customerId);

    // Returns all customers with rental counts and flags those above average as reward-eligible
    Map<String, Object> getRewardsReport();

    // Returns available inventory for a film at a store — throws 404 if film or store not found
    Map<String, Object> getFilmInStock(Integer filmId, Integer storeId);

    // Returns rented-out inventory for a film at a store — throws 404 if film or store not found
    Map<String, Object> getFilmNotInStock(Integer filmId, Integer storeId);
}