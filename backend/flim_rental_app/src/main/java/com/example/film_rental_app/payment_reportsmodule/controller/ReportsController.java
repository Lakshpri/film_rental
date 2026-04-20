package com.example.film_rental_app.payment_reportsmodule.controller;

import com.example.film_rental_app.payment_reportsmodule.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/reports")
public class ReportsController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/customer-list")
    public ResponseEntity<Map<String, Object>> getCustomerList() {
        return ResponseEntity.ok(reportService.getCustomerList());
    }

    @GetMapping("/film-list")
    public ResponseEntity<Map<String, Object>> getFilmList() {
        return ResponseEntity.ok(reportService.getFilmList());
    }

    @GetMapping("/staff-list")
    public ResponseEntity<Map<String, Object>> getStaffList() {
        return ResponseEntity.ok(reportService.getStaffList());
    }

    @GetMapping("/sales-by-store")
    public ResponseEntity<Map<String, Object>> getSalesByStore() {
        return ResponseEntity.ok(reportService.getSalesByStore());
    }

    @GetMapping("/sales-by-category")
    public ResponseEntity<Map<String, Object>> getSalesByCategory() {
        return ResponseEntity.ok(reportService.getSalesByCategory());
    }

    @GetMapping("/actor-info")
    public ResponseEntity<Map<String, Object>> getActorInfo() {
        return ResponseEntity.ok(reportService.getActorInfo());
    }
}
