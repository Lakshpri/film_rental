package com.example.film_rental_app.payment_reportsmodule.service;

import java.util.Map;

public interface AnalyticsService {

    Map<String, Object> getCustomerBalance(Integer customerId);

    Map<String, Object> getRewardsReport();

    Map<String, Object> getFilmInStock(Integer filmId, Integer storeId);

    Map<String, Object> getFilmNotInStock(Integer filmId, Integer storeId);
}