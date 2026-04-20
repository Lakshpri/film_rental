package com.example.film_rental_app.payment_reportsmodule.controller;

import com.example.film_rental_app.payment_reportsmodule.service.AnalyticsService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@Validated
public class AnalyticsController {

    @Autowired private AnalyticsService analyticsService;

    // GET /api/analytics/customer-balance/{customerId}
    @GetMapping("/customer-balance/{customerId}")
    public ResponseEntity<Map<String, Object>> getCustomerBalance(
            @PathVariable @Positive(message = "Customer ID must be a number greater than zero (e.g. 1, 2, 3)") Integer customerId) {
        return ResponseEntity.ok(analyticsService.getCustomerBalance(customerId));
    }

    // GET /api/analytics/rewards-report
    @GetMapping("/rewards-report")
    public ResponseEntity<Map<String, Object>> getRewardsReport() {
        return ResponseEntity.ok(analyticsService.getRewardsReport());
    }

    // GET /api/analytics/film-in-stock?filmId=1&storeId=1
    @GetMapping("/film-in-stock")
    public ResponseEntity<Map<String, Object>> getFilmInStock(
            @RequestParam @Positive(message = "Film ID must be a number greater than zero (e.g. 1, 2, 3)") Integer filmId,
            @RequestParam @Positive(message = "Store ID must be a number greater than zero (e.g. 1, 2, 3)") Integer storeId) {
        return ResponseEntity.ok(analyticsService.getFilmInStock(filmId, storeId));
    }

    // GET /api/analytics/film-not-in-stock?filmId=1&storeId=1
    @GetMapping("/film-not-in-stock")
    public ResponseEntity<Map<String, Object>> getFilmNotInStock(
            @RequestParam @Positive(message = "Film ID must be a number greater than zero (e.g. 1, 2, 3)") Integer filmId,
            @RequestParam @Positive(message = "Store ID must be a number greater than zero (e.g. 1, 2, 3)") Integer storeId) {
        return ResponseEntity.ok(analyticsService.getFilmNotInStock(filmId, storeId));
    }
}
