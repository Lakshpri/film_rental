package com.example.film_rental_app.payment_reportsmodule.controller;

import com.example.film_rental_app.payment_reportsmodule.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;


    @GetMapping("/customer-balance/{customerId}")
    public ResponseEntity<Map<String, Object>> getCustomerBalance(@PathVariable Integer customerId) {
        return ResponseEntity.ok(analyticsService.getCustomerBalance(customerId));
    }

    @GetMapping("/rewards-report")
    public ResponseEntity<Map<String, Object>> getRewardsReport() {
        return ResponseEntity.ok(analyticsService.getRewardsReport());
    }

    @GetMapping("/film-in-stock")
    public ResponseEntity<Map<String, Object>> getFilmInStock(@RequestParam Integer filmId,
                                                              @RequestParam Integer storeId) {
        return ResponseEntity.ok(analyticsService.getFilmInStock(filmId, storeId));
    }

    @GetMapping("/film-not-in-stock")
    public ResponseEntity<Map<String, Object>> getFilmNotInStock(@RequestParam Integer filmId,
                                                                 @RequestParam Integer storeId) {
        return ResponseEntity.ok(analyticsService.getFilmNotInStock(filmId, storeId));
    }
}